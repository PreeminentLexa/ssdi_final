import javax.swing.text.Utilities;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int port = 8888;
    public static void main(String[] args) {
        Server server = new Server();
    }
    public Server() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            System.out.println("Unable to start the server on port "+port);
            e.printStackTrace();
            return;
        }
        System.out.println("The server has been started on port "+port);
        while(true){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("An error occurred in connecting a user.");
                e.printStackTrace();
                continue;
            }
            ConnectedClient client = new ConnectedClient(clientSocket);
            new Thread(client).start();
        }
    }
}
