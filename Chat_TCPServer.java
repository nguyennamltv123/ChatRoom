package BTTH1;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Chat_TCPServer {

    private final ServerSocket serverSocket;

    public Chat_TCPServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (true) {
                // Will be closed in the Client Handler.
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                DataInputStream dis = new DataInputStream(socket.getInputStream());               
                ClientHandler clientHandler = new ClientHandler(socket, dis.readUTF());
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }


    // Run the program.
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6000);
        Chat_TCPServer server = new Chat_TCPServer(serverSocket);
        server.startServer();
    }

}


class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String clientUsername;

 
    public ClientHandler(Socket socket, String username) {
        try {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.clientUsername = username;
            // Add the new client handler to the array so they can receive messages from others.
            clientHandlers.add(this);
            System.out.println(clientHandlers.get(0));
            sendMessage("SERVER: " + clientUsername + " has entered the chat!");
        } catch (IOException e) {
            
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        try {
        	while (true) {
                messageFromClient = dis.readUTF();
                sendMessage(messageFromClient);
                System.out.println(messageFromClient);
            }
        }catch(Exception e) {
        }
    }

 
    public void sendMessage(String messageToSend) {
        for (ClientHandler item : clientHandlers) {
            try {
            	System.out.println(item);
                if (!item.clientUsername.equals(clientUsername)) {
                    item.dos.writeUTF(messageToSend);
                    item.dos.flush();
                }
            } catch (IOException e) {

            }
        }
    }
}