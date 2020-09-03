package service;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.message.QuotationRequestMessage;
import service.message.QuotationResponseMessage;

import javax.jms.*;
import java.util.HashMap;

public class Broker {
    static Session session;
    static MessageProducer producer;
    static int counter=0;
    static HashMap<QuotationResponseMessage, Integer> quotesMap = new HashMap<QuotationResponseMessage, Integer>();
    public static void main(String[] args) {
        try {
            String host = "localhost";
            if (args.length > 0) {
                host = args[0];
            }
            ActiveMQConnectionFactory factory =
                    new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
            Connection connection = factory.createConnection();
            connection.setClientID("broker");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            Topic requestsQueue = session.createTopic("REQUESTS");
            Topic quotationQueue = session.createTopic("QUOTATIONS");

            Topic topic = session.createTopic("APPLICATIONS");
            producer = session.createProducer(topic);

            MessageConsumer requestsConsumer = session.createConsumer(requestsQueue);

            // 2 consumers request consumer and quotation consumer
            // cache(broker generated request id, ClientApplicationMessage)
            while (true) {
                MessageConsumer quotationConsumer = session.createConsumer(quotationQueue);
                quotationConsumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {

                        System.out.println("QUOTE");
                        try {
                            if (message instanceof ObjectMessage) {

                                // It’s an Object Message
                                Object content = ((ObjectMessage) message).getObject();
                                if (content instanceof QuotationResponseMessage) {
                                    quotesMap.put((QuotationResponseMessage) content, counter);
                                    quotationConsumer.close();
//                                    System.out.println("QOUTATION RESPONSE MESSAGE");
                                    QuotationResponseMessage qrm = (QuotationResponseMessage) content;
                                    System.out.println("QRM= "+qrm.quotation.price+" "+qrm.quotation.company+" "+qrm.quotation.reference);
                                } else {
                                    System.out.println("Unknown message type: " +
                                            message.getClass().getCanonicalName());
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                // Get the next message from the APPLICATION topic
                requestsConsumer.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message message) {

                        try {
                            if (message instanceof ObjectMessage) {
                                System.out.println("Instance "+counter);
                                // It’s an Object Message
                                Object content = ((ObjectMessage) message).getObject();
                                if (content instanceof QuotationRequestMessage) {
                                    // It’s a Quotation Request Message
                                    Thread thread = new Broker().run(message);
                                    System.out.println("Time Up");
                                }
                                else {
                                    System.out.println("Unknown message type: " +
                                            message.getClass().getCanonicalName());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });


//                for(QuotationResponseMessage key:quotesMap.keySet()){
//                    System.out.println("Map "+key.id+" "+key.quotation.company);
//                }
            }

//


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Thread run(Object message) throws JMSException, InterruptedException {
        Object content = ((ObjectMessage) message).getObject();

        QuotationRequestMessage request = (QuotationRequestMessage) content;

        System.out.println("In thread " + counter++);
        System.out.println(request.info.name+" "+request.info.licenseNumber+" "+request.id);
        //Message request = session.createObjectMessage((QuotationRequestMessage) message);
        producer.send((ObjectMessage) message);
        producer.close();
        Thread.sleep(2000);
        return Thread.currentThread();
    }
}
