import service.auldfellas.AFQService;
import service.core.BrokerService;
import service.core.Constants;
import service.core.QuotationService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
public class Server {
    public static void main(String args[]) {
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        QuotationService afqService = new AFQService();
        try {
// Connect to the RMI Registry - creating the registry will be the
// responsibility of the broker.
            Registry registry = LocateRegistry.getRegistry(host, 1099);
// Create the Remote Object
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(afqService,0);
// Register the object with the RMI Registry
            BrokerService broker = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
            broker.registerService(Constants.AULD_FELLAS_SERVICE, quotationService);
            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {Thread.sleep(1000); }
        } catch (Exception e) {
            System.out.println("Auldfellas trouble: " + e);
        }
    }
}