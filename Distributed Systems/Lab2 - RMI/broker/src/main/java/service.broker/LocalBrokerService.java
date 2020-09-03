package service.broker;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService {

	public List<Quotation> getQuotations(ClientInfo info)  {
		List<Quotation> quotations = null;
		
		try {
			quotations = new LinkedList<>();
			Registry registry = LocateRegistry.getRegistry(1099);
			for (String name : registry.list()) {
				if (name.startsWith("qs-")) {
					QuotationService service = (QuotationService) registry.lookup(name);
					quotations.add(service.generateQuotation(info));
				}
			}
		}catch(Exception e ){
			e.printStackTrace();
		}

		return quotations;
	}

	public void registerService(String name, QuotationService service){

		try {
			LocateRegistry.getRegistry(1099).bind(name, service);
		}catch(Exception e) { e.printStackTrace(); }

	}
}
