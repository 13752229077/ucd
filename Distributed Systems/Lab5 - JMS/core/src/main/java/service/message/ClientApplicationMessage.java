package service.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import service.core.ClientInfo;
import service.core.Quotation;

import java.util.ArrayList;

/** I utilised the library, Lombok, to create getter, setters and constructors at runtime **/
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ClientApplicationMessage {
    public ClientInfo info;
    public ArrayList<Quotation> quotations;


}
