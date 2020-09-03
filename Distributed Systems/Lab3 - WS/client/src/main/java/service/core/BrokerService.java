package service.core;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.LinkedList;

@WebService
public interface BrokerService {

    @WebMethod
    LinkedList<Quotation> getQuotations(ClientInfo info, String args);
}
