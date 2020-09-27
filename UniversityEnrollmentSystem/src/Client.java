import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public static Scanner scanner = new Scanner(System.in);
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    private static ObjectOutputStream objectOutputStream = null;
    private static ObjectInputStream objectInputStream = null;
    private static String response = "";

    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",5000)){
            System.out.println(ANSI.GREEN+"connected: "+socket.getInetAddress()+":"+socket.getPort()+ANSI.RESET);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            homeView();
        }catch (SocketException | EOFException e){
            System.out.println(ANSI.RED+"500 internal server error"+ANSI.RESET);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void homeView() throws Exception{
        System.out.println("\nHOME PAGE");
        System.out.println("01. Applicant Portal\n02. Admin Login\n03. Help Center\n00. Exit");
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        dataOutputStream.writeInt(choice);
        while (choice != 0) {
            switch (choice){
                case 1:
                    applicantPortalView();
                    break;

                case 2:
                    scanner.nextLine();
                    System.out.println("\nADMIN LOGIN");

                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    dataOutputStream.writeUTF(username);

                    System.out.print("Password: ");
                    String password = readPassword();
                    dataOutputStream.writeUTF(password);

                    boolean validCredentials = dataInputStream.readBoolean();
                    if(validCredentials)
                        adminView();
                    else
                        System.out.println(ANSI.CYAN+"Incorrect Username, Password. Try Again"+ANSI.RESET);

                    break;

                case 3:
                    helpCenter();
                break;

                default:
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
            }
            System.out.println("\nHOME PAGE");
            System.out.println("01. Applicant Portal\n02. Admin Login\n03. Help Center\n00. Exit");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }
    }

    public static void applicantPortalView() throws Exception{
        System.out.println("\nAPPLICANT PORTAL");
        System.out.println("01. Applicant Login\n02. Applicant Register\n00. Back");
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        dataOutputStream.writeInt(choice);
        while (choice != 0) {
            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.println("\nAPPLICANT LOGIN");
                    System.out.print("ApplicantID: ");
                    String id = scanner.nextLine();
                    dataOutputStream.writeUTF(id);

                    System.out.print("Password: ");
                    String password = readPassword();
                    dataOutputStream.writeUTF(password);

                    boolean validCredentials = dataInputStream.readBoolean();
                    if(validCredentials)
                        applicantView();
                    else
                        System.out.println(ANSI.CYAN+"Incorrect ApplicantId, Password. Try Again"+ANSI.RESET);

                    break;

                case 2:
                    scanner.nextLine();
                    fillApplicationForm();
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
                    break;

                default:
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
            }
            System.out.println("\nAPPLICANT PORTAL");
            System.out.println("01. Applicant Login\n02. Applicant Register\n00. Back");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);

        }
    }

    public static void applicantView() throws Exception{
        response = dataInputStream.readUTF();
        int choice;
        if(response.equals("409 conflict")) {
            System.out.println(ANSI.CYAN+"Multiple sessions detected"+ANSI.RESET);
            System.out.println(ANSI.CYAN+"Try logout from active session"+ANSI.RESET);
            choice = 0;
        }
        else {
            System.out.println("APPLICANTS PAGE");
            System.out.println(ANSI.CYAN+response+ANSI.RESET);
            printApplicantOptions();
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }

        while (choice != 0){
            switch (choice){
                case 4:
                    boolean enrollmentForm = dataInputStream.readBoolean();
                    if(enrollmentForm) {
                        scanner.nextLine();
                        fillEnrollmentForm();
                        response = dataInputStream.readUTF();
                        System.out.println(ANSI.CYAN+response+ANSI.RESET);
                    }
                    else
                        System.out.println(ANSI.CYAN+"Enrollment Form Not Issued"+ANSI.RESET);
                break ;

                default:
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
            }

            printApplicantOptions();
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }

    }

    public static void adminView() throws Exception{
        response = dataInputStream.readUTF();
        int choice;
        if(response.equals("409 conflict")) {
            System.out.println(ANSI.CYAN+"Multiple sessions detected"+ANSI.RESET);
            System.out.println(ANSI.CYAN+"Try logout from active session"+ANSI.RESET);
            choice = 0;
        }
        else {
            System.out.println("ADMINS PAGE");
            System.out.println(ANSI.CYAN+response+ANSI.RESET);
            printAdminOptions();
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }

        while (choice != 0){
            switch (choice){

                case 4:
                case 9:
                    scanner.nextLine();
                    System.out.print("Applicant ID: ");
                    String id = scanner.nextLine();
                    dataOutputStream.writeUTF(id);
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
                break;

                case 5:
                    scanner.nextLine();
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    dataOutputStream.writeUTF(username);

                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    dataOutputStream.writeUTF(name);

                    System.out.print("Password: ");
                    String password = readPassword();
                    dataOutputStream.writeUTF(password);

                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
                break;

                case 10:
                    scanner.nextLine();
                    support();
                    break;

                default:
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);

            }
            printAdminOptions();
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }

    }

    public static void support() throws Exception{
        response = dataInputStream.readUTF();
        System.out.println(ANSI.CYAN+response+ANSI.RESET);
        while (true) {
            System.out.print("Ticket No: ");
            String ticketNo = scanner.nextLine();
            dataOutputStream.writeUTF(ticketNo);
            if (ticketNo.equals("exit()")) return;
            if(dataInputStream.readBoolean()) break;
        }
        System.out.println("\nConversation:");
        response = dataInputStream.readUTF();
        System.out.print(response);
        while (true){
            System.out.print("You: ");
            String message = scanner.nextLine();
            dataOutputStream.writeUTF(message);
            if(message.equals("exit()")) break;
        }
    }

    public static void fillApplicationForm() throws Exception{
        System.out.println("\nAPPLICANT REGISTRATION");

        SerializedApplicationForm applicationForm = (SerializedApplicationForm) objectInputStream.readObject();
        applicationForm.enterData();
        objectOutputStream.writeObject(applicationForm);

        System.out.println("Branches Offered: ");
        response = dataInputStream.readUTF();
        System.out.println(ANSI.CYAN+response+ANSI.RESET);
        while (true){
            System.out.print("Select: ");
            dataOutputStream.writeUTF(scanner.nextLine());
            boolean validBranch = dataInputStream.readBoolean();
            if(validBranch)
                break;
            System.out.println(ANSI.CYAN+"Invalid Selection"+ANSI.RESET);
        }
    }

    public static void helpCenter() throws Exception{
        System.out.println("\nHELP CENTER");
        System.out.println("01. New Support Ticket\n02. Existing Support Ticket\n00. Back");
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        dataOutputStream.writeInt(choice);
        while (choice != 0) {
            switch (choice){
                case 1:
                    scanner.nextLine();
                    newSupportTicket();
                break;

                case 2:
                    scanner.nextLine();
                    System.out.print("Ticket No: ");
                    String ticketNo = scanner.nextLine();
                    existingSupportTicket(ticketNo);
                break;

                default:
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
            }
            System.out.println("\nHELP CENTER");
            System.out.println("01. New Support Ticket\n02. Existing Support Ticket\n00. Back");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }
    }

    public static void newSupportTicket() throws Exception{
        String isRegistered,applicantId,clientName;
        System.out.println("\nTICKET CREATION");
        System.out.print("Registered applicant (yes/no)? ");
        isRegistered = scanner.nextLine();
        dataOutputStream.writeUTF(isRegistered);
        if(isRegistered.equalsIgnoreCase("yes")){
            System.out.print("Applicant ID: ");
            applicantId = scanner.nextLine();
            dataOutputStream.writeUTF(applicantId);
            if(!dataInputStream.readBoolean()) {
                System.out.println(ANSI.CYAN+"Invalid Applicant ID"+ANSI.RESET);
                return;
            }
        }
        else {
            System.out.print("Name: ");
            clientName = scanner.nextLine();
            dataOutputStream.writeUTF(clientName);
        }
        String ticketNo = dataInputStream.readUTF();
        System.out.println(ANSI.CYAN+"Ticket No: "+ticketNo+ANSI.RESET);
        System.out.println(ANSI.CYAN+"Note this for further reference"+ANSI.RESET);
        existingSupportTicket(ticketNo);
    }

    public static void existingSupportTicket(String ticketNo) throws Exception{
        dataOutputStream.writeUTF(ticketNo);
        if(dataInputStream.readBoolean()){
            System.out.println(ANSI.CYAN+"Invalid Ticket No."+ANSI.RESET);
            return;
        }
        System.out.println(ANSI.CYAN+"\nTICKET: "+ticketNo+ANSI.RESET);
        System.out.println("01. Conversation\n02. Mark Resolved\n00. Back");
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        dataOutputStream.writeInt(choice);
        while (choice!=0){
            switch (choice){
                case 1:
                    System.out.print(dataInputStream.readUTF());
                    scanner.nextLine();
                    if (dataInputStream.readBoolean())
                        System.out.print(ANSI.CYAN+"\nEND"+ANSI.RESET);
                    else
                        while (true){
                            System.out.print("You: ");
                            String message = scanner.nextLine();
                            dataOutputStream.writeUTF(message);
                            if(message.equals("exit()")) break;
                        }

                break;

                default:
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
            }
            System.out.println("\n01. Conversation\n02. Mark Resolved\n00. Back");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }

    }

    public static void fillEnrollmentForm() throws Exception{
        System.out.println("\nENROLLMENT FORM");

        System.out.print("Form: ");
        String form = scanner.nextLine();
        dataOutputStream.writeUTF(form);

        System.out.print("HSC Mark Sheet: ");
        String hscMarkSheet = scanner.nextLine();
        dataOutputStream.writeUTF(hscMarkSheet);

        System.out.print("Entrance Mark Sheet: ");
        String entranceMarkSheet = scanner.nextLine();
        dataOutputStream.writeUTF(entranceMarkSheet);
    }

    public static void printAdminOptions(){
        System.out.println("\nADMIN PAGE");
        System.out.println("01. List Applicants");
        System.out.println("02. List Shortlisted");
        System.out.println("03. Shortlist Applicants");
        System.out.println("04. Check Applicant Status");
        System.out.println("05. Register New Admin");
        System.out.println("06. Issue Enrollment Forms");
        System.out.println("07. View Stats");
        System.out.println("08. View Enrollment Forms");
        System.out.println("09. Enroll Applicant");
        System.out.println("10. Client Support");
        System.out.println("00. Logout");
    }
    public static void printApplicantOptions(){
        System.out.println("\nAPPLICANT PAGE");
        System.out.println("01. Check Status");
        System.out.println("02. Float Seat");
        System.out.println("03. Lock Seat");
        System.out.println("04. Fill/Update Enrollment Details");
        System.out.println("05. View Enrollment ID");
        System.out.println("00. Logout");
    }

    public static String readPassword() {
        Console console = System.console();

        String password = null;
        try{
            char[] ch = console.readPassword();
            password = new String(ch);
        }catch(Exception e){
            password = scanner.nextLine();
        }
        return password;
    }
}

