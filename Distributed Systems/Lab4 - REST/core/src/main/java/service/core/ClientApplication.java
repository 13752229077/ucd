package service.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/** I utilised the library, Lombok, to create getter, setters and constructors at runtime **/
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClientApplication {
private int applicationNo;
private ClientInfo info;
private ArrayList<Quotation> quotations;

}
