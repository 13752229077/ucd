package client;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.NoSuchQuotationException;
import service.core.Quotation;

import java.lang.reflect.Array;
import java.net.ConnectException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    /**
     * Main to test broker and service using sample ClientInfo
     * Prints each client along with their respective quotations
     * Added try, catch for ConnectionRefused Exception and RESPONSE 500 error
     * **/
    public static void main(String[] args){
        RestTemplate restTemplate = new RestTemplate();
        String host = "http://localhost:8080/applications";
        try {
            for (ClientInfo info : clients) {
                HttpEntity<ClientInfo> request = new HttpEntity<>(info);
                restTemplate.postForObject(host, request, ClientApplication.class);
            }
            ClientApplication[] applications = restTemplate.getForObject(host, ClientApplication[].class);
            if (applications != null) {
                for (ClientApplication application : applications) {
                    displayProfile(application);
                    for (Quotation quote : application.getQuotations())
                        displayQuotation(quote);
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage()+"\nAre you sure the broker service and/or quotations services are running?");
        }
    }

    // Added line for Application number in output so passed ClientApplication instead of just ClientInfo
    /**
     * Display the client info nicely.
     *
     * @param application
     */
    public static void displayProfile(ClientApplication application) {
        ClientInfo info = application.getInfo();
        System.out.println("\n|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("| Application Number: " + String.format("%1$-15s", application.getApplicationNo()) +
                " |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.getName()) +
                        " | Gender: " + String.format("%1$-27s", (info.getGender()==ClientInfo.MALE?"Male":"Female")) +
                        " | Age: " + String.format("%1$-30s", info.getAge())+" |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) +
                        " | No Claims: " + String.format("%1$-24s", info.getNoClaims()+" years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.getPoints())+" |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.getCompany()) +
                        " | Reference: " + String.format("%1$-24s", quotation.getReference()) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice()))+" |");
        System.out.println("|=================================================================================================================|");
    }
    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
    };
}
