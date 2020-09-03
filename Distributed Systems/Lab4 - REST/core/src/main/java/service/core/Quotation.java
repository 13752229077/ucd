package service.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to store the quotations returned by the quotation services
 * 
 * @author Rem
 *
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Quotation {
	private String company;
	private String reference;
	private double price;
}
