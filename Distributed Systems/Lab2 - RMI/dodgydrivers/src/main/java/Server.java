import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import service.dodgydrivers.DDQService;
import service.core.ClientInfo;
import service.core.BrokerService;
import service.core.QuotationService;
import service.core.Constants;

public class Server {
    public static void main(String args[]) {
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        QuotationService ddqService = new DDQService();
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            // Create the Remote Object
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(ddqService,0);
            // Register the object with the RMI Registry
            BrokerService broker = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
            broker.registerService(Constants.DODGY_DRIVERS_SERVICE, quotationService);

            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {Thread.sleep(1000); }
        } catch (Exception e) {
            System.out.println("Dodgydrivers trouble: " + e);
        }
    }
}