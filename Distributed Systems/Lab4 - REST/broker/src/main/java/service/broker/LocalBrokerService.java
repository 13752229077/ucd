package service.broker;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.NoSuchQuotationException;
import service.core.Quotation;

@RestController
public class LocalBrokerService {
	// Injected value with default list of hosts
	@Value("${hosts:http://localhost:8081/quotations,http://localhost:8082/quotations,http://localhost:8083/quotations}")
	private String hosts;
	// static counter for application numbers
    private static int applicationNo = 0;
	private Map<Integer, ClientApplication> applications = new HashMap<>();

	/** @param info
	 * For either the default value of hosts or the program arguments ("--hosts=http://...),
	 * this POST method will split by "," and call each service with the info passed
	 * returning the ClientApplication.
	 * All applications are stored in Map, applications, with key=applicationNo
	 * **/
	@RequestMapping(value="/applications",method= RequestMethod.POST)
	public ClientApplication getQuotations(@RequestBody ClientInfo info) {
		String[] hostServices = hosts.split(",");
		ArrayList<Quotation> quotations = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ClientInfo> request = new HttpEntity<>(info);
		for(String s : hostServices) {
			Quotation quotation =
					restTemplate.postForObject(s,
							request, Quotation.class);
			quotations.add(quotation);
		}
		ClientApplication application = new ClientApplication(applicationNo, info, quotations);
		applications.put(applicationNo++, application);

		return application;
	}

	/**  @param applicationNumber
	 * For a given application number this GET method returns that application
	 * **/
	@RequestMapping(value="/applications/{application-number}",method=RequestMethod.GET)
	public ClientApplication getResource(@PathVariable("application-number") Integer applicationNumber) {
		ClientApplication application = applications.get(applicationNumber);
		if (application == null) throw new NoSuchQuotationException();
		return application;
	}

	/** This GET method returns all stored application
	 * in the form of an ArrayList **/
	@RequestMapping(value="/applications",method=RequestMethod.GET)
	public ArrayList<ClientApplication> getResource() {
		ArrayList<ClientApplication> apps = new ArrayList<>(applications.values());
		return apps;
	}
}

