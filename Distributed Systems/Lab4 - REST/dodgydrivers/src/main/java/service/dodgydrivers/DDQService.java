package service.dodgydrivers;

import org.springframework.web.bind.annotation.*;
import service.core.AbstractQuotationService;
import service.core.ClientInfo;
import service.core.NoSuchQuotationException;
import service.core.Quotation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of Quotation Service for Dodgy Drivers Insurance Company
 *
 * @author Rem
 *
 */
@RestController
public class DDQService extends AbstractQuotationService {
	// All references are to be prefixed with an DD (e.g. DD001000)
	private static final String PREFIX = "DD";
	private static final String COMPANY = "Dodgy Drivers Corp.";
	private Map<String, Quotation> quotations = new HashMap<>();

	/** @param info
	 *  This POST method creates and returns a single Quotation for a given client's info **/
	@RequestMapping(value="/quotations",method= RequestMethod.POST)
	public Quotation createQuotation(@RequestBody ClientInfo info) {
		Quotation quotation = generateQuotation(info);
		quotations.put(quotation.getReference(), quotation);
		return quotation;
	}

	/** @param reference
	 * @exception NoSuchQuotationException
	 * This GET method returns a single quotation for a given reference number **/
	@RequestMapping(value="/quotations/{reference}",method=RequestMethod.GET)
	public Quotation getResource(@PathVariable("reference") String reference) {
		Quotation quotation = quotations.get(reference);
		if (quotation == null) throw new NoSuchQuotationException();
		return quotation;
	}

	/** This GET method returns an ArrayList of all quotations previously POSTed **/
	@RequestMapping(value="/quotations",method=RequestMethod.GET)
	public ArrayList<Quotation> listQuotations() {
		ArrayList<Quotation> list = new ArrayList<>();
		for(Quotation quotation: quotations.values()) list.add(quotation);
		return list;
	}

	/**
	 * Quote generation:
	 * 5% discount per penalty point (3 points required for qualification)
	 * 50% penalty for <= 3 penalty points
	 * 10% discount per year no claims
	 */
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 800 and 1000
		double price = generatePrice(800, 200);

		// 5% discount per penalty point (3 points required for qualification)
		int points = info.getPoints();
		int discount = (points > 3) ? 5*points:-50;

		// Add a no claims discount
		discount += getNoClaimsDiscount(info);

		// Generate the quotation and send it back

		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 10*info.getNoClaims();
	}
}
