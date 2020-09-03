package service.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.*;

import javax.xml.ws.Service;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public class Broker implements ServiceListener {
	// list of service paths resolved to prevent duplicates
	private LinkedList<String> servicesResolved = new LinkedList<>();
	// static list of QuoterServices created from paths
	private static LinkedList<QuoterService> quotationServices = new LinkedList<>();

	/** I amended this method to work with both JaxWS versions of the Quoter Quotation Services as
	 *  well as with JmDNS versions. If the argument
	 *  "http://localhost:9001/quote,http://localhost:9002/quote,http://localhost:9003/quote"
	 *  is passed when running client then getQuotations will create those services.
	 *  By default the quotationsServices list is used which assumes the serviceResolved method
	 *  has found some usable paths **/
	@WebMethod
	public LinkedList<Quotation> getQuotations(ClientInfo info, String args) throws MalformedURLException {
		LinkedList<Quotation> quotations = new LinkedList<>();
		if(args.equals("")) {
			for (QuoterService qs : quotationServices) {
				quotations.add(qs.generateQuotation(info));
			}
		}
		else {
			String[] hosts = args.split(",");
			for (String host : hosts) {
				quotations.add(createService(host).generateQuotation(info));
			}
		}
		return quotations;
	}

	/** As I was leaving the implementation of JaxWS I refactored this method to prevent duplicate code **/
	private void invokeBrokerService(String url) throws MalformedURLException {
		quotationServices.add(createService(url));
	}

	/** This method is used by implementations, JaxWS and JmDNS, to create a service based on a path **/
	private QuoterService createService(String url) throws MalformedURLException {
		URL wsdlUrl = new URL(url);

		QName serviceName = new QName("http://core.service/", "QuoterService");

		Service service = Service.create(wsdlUrl, serviceName);

		QName portName = new QName("http://core.service/", "QuoterPort");
		return service.getPort(portName, QuoterService.class);
	}

	@Override
	public void serviceAdded(ServiceEvent event) {
		System.out.println("Service added: " + event.getInfo());
	}

	@Override
	public void serviceRemoved(ServiceEvent event) {
		System.out.println("Service removed: " + event.getInfo());
	}

	/** I utilised a LinkedList of already resolved services to prevent duplication of services **/
	@Override
	public void serviceResolved(ServiceEvent event) {
		String path = event.getInfo().getPropertyString("path");
		if (path != null && !servicesResolved.contains(path)) {
			try {
				invokeBrokerService(path);
				servicesResolved.add(path);
				System.out.println("Service resolved: " + event.getInfo());
			} catch (Exception e) {
				System.out.println("Problem with service: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		try {
			// Create a JmDNS instance
			JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
			// Add a service listener
			jmdns.addServiceListener("_quote._tcp.local.", new Broker());
			// Wait a bit
			Thread.sleep(30000);
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Endpoint.publish("http://0.0.0.0:9000/broker", new Broker());
	}
}
