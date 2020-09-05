package io.sockets;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Age: ");
        int age = scanner.nextInt();
        Data data = new Data(firstName,lastName,age);

        try(Socket socket = new Socket("localhost",5000)){
            ObjectOutputStream oStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream iStream = new ObjectInputStream(socket.getInputStream());
            BufferedReader inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outStream = new PrintWriter(socket.getOutputStream(),true);
            oStream.writeObject(data);
            String response = inStream.readLine();
            System.out.println(response);
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }

    }
}
