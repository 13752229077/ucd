package service.core;

import lombok.*;

/**
 * Interface to define the state to be stored in ClientInfo objects
 * 
 * @author Rem
 *
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClientInfo {
	public static final char MALE = 'M';
	public static final char FEMALE	= 'F';
	private String name;
	private char gender;
	private int age;
	private int points;
	private int noClaims;
	private String licenseNumber;


}
