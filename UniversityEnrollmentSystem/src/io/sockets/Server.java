package io.sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println("listening to port:5000");
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket+" connected.");
            ObjectOutputStream oStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream iStream = new ObjectInputStream(clientSocket.getInputStream());
            BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outStream = new PrintWriter(clientSocket.getOutputStream(),true);

            System.out.print("received: ");
            Data data = (Data) iStream.readObject();
            System.out.println(data);
            outStream.println("Object Received.");
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}
