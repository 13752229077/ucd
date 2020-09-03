package client;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.network.jms.JmsMesageConvertor;
import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.Constants;
import service.message.QuotationRequestMessage;
import service.message.QuotationResponseMessage;

import javax.jms.*;

public class Main {

	private static int SEED_ID = 0;
	private static Map<Long, ClientInfo> cache = new HashMap<>();

	public static void main(String[] args) {
		try{
			String host = "localhost";
			if (args.length > 0) {
				host = args[0];
			}
			ActiveMQConnectionFactory factory =
					new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
			Connection connection = factory.createConnection();
			connection.setClientID("client");
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
			Queue queue = session.createQueue("QUOTATIONS");
			Topic topic = session.createTopic("REQUESTS");
			MessageProducer producer = session.createProducer(topic);
			MessageConsumer consumer = session.createConsumer(queue);


			QuotationRequestMessage quotationRequest =
					new QuotationRequestMessage(SEED_ID++, clients[0]);
			Message request = session.createObjectMessage(quotationRequest);
			cache.put(quotationRequest.id, quotationRequest.info);
            System.out.println("b4 send");
			producer.send(request);

//			try{
//                Message message = consumer.receive();
//                System.out.println("message");
//                if (message instanceof ObjectMessage) {
//                    Object content = ((ObjectMessage) message).getObject();
//                    System.out.println("CONTENT: "+content);
//                    //System.out.println("resp: "+response);
//                    if (content instanceof QuotationResponseMessage) {
//                        QuotationResponseMessage response  = (QuotationResponseMessage) content;
//                        System.out.println("CONTENT: "+content);
//                        System.out.println("resp: "+response);
//                        ClientInfo info = cache.get(response.id);
//                        displayProfile(info);
//                        displayQuotation(response.quotation);
//                        System.out.println("\n");
//                    }
//                    message.acknowledge();
//                } else {
//                    System.out.println("Unknown message type: " +
//                            message.getClass().getCanonicalName());
//                }
//            }
//			catch (Exception e){
//			    e.printStackTrace();
//            }




		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Display the client info nicely.
	 * 
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) + 
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| License Number: " + String.format("%1$-19s", info.licenseNumber) + 
				" | No Claims: " + String.format("%1$-24s", info.noClaims+" years") +
				" | Penalty Points: " + String.format("%1$-19s", info.points)+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 * 
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) + 
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	}
	
	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")		
	};
}
