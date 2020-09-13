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
        System.out.println("1. Applicant Portal\n2. Admin Login\n3. Help Center\n0. Exit");
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
                    scanner.nextLine();
                    helpCenter();
                break;

                default:
                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
            }
            System.out.println("\nHOME PAGE");
            System.out.println("1. Applicant Portal\n2. Admin Login\n3. Help Center\n0. Exit");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);
        }
    }

    public static void applicantPortalView() throws Exception{
        System.out.println("\nAPPLICANT PORTAL");
        System.out.println("1. Applicant Login\n2. Applicant Register\n0. Back");
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
            System.out.println("1. Applicant Login\n2. Applicant Register\n0. Back");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            dataOutputStream.writeInt(choice);

        }
    }

    public static void applicantView() throws Exception{
        System.out.println("APPLICANTS PAGE");
        response = dataInputStream.readUTF();
        System.out.println(ANSI.CYAN+response+ANSI.RESET);

        printApplicantOptions();
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        dataOutputStream.writeInt(choice);

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
        System.out.println("ADMINS PAGE");
        response = dataInputStream.readUTF();
        System.out.println(ANSI.CYAN+response+ANSI.RESET);

        printAdminOptions();
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        dataOutputStream.writeInt(choice);

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

                    System.out.print("Password: ");
                    String password = readPassword();
                    dataOutputStream.writeUTF(password);

                    response = dataInputStream.readUTF();
                    System.out.println(ANSI.CYAN+response+ANSI.RESET);
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
        System.out.println("HELP CENTER");
        String name, isRegistered, applicationId, admin;
        System.out.print("Name: ");
        name = scanner.nextLine();
        applicationId = "NULL";
        System.out.print("Registered applicant (yes/no)? ");
        isRegistered = scanner.nextLine();
        if(isRegistered.equalsIgnoreCase("yes")){
            System.out.print("Application ID: ");
            applicationId = scanner.nextLine();
        }

        String message = "";
        dataOutputStream.writeUTF("Name: "+name+"\nApplication ID: "+applicationId);
        dataOutputStream.writeUTF(name);

        System.out.println("Waiting for Admin to Connect...");
        admin = dataInputStream.readUTF();
        System.out.println(ANSI.GREEN+"\nConnected to Admin ("+admin+")"+ANSI.RESET);
        System.out.println(ANSI.CYAN+"Type 'exit()' to leave"+ANSI.RESET);

        System.out.println("\n"+ANSI.YELLOW+admin+": "+ANSI.CYAN+"hi! how can i help you?"+ANSI.RESET);
        while (true){
            System.out.print("You: ");
            message = scanner.nextLine();
            dataOutputStream.writeUTF(message);
            if(message.equals("exit()"))
                break;
            System.out.print(ANSI.YELLOW+admin+": ..."+ANSI.RESET);
            response = dataInputStream.readUTF();
            System.out.println("\b\b\b"+ANSI.CYAN+response+ANSI.RESET);
            if(response.equals("exit()"))
                break;
        }

    }

    public static void fillEnrollmentForm() throws Exception{

        System.out.println("\nENROLLMENT FORM");
        System.out.print("Placeholder: ");
        dataOutputStream.writeUTF(scanner.nextLine());
    }

    public static void printAdminOptions(){
        System.out.println("\nADMIN PAGE");
        System.out.println("1. List Applicants");
        System.out.println("2. List Shortlisted");
        System.out.println("3. Shortlist Applicants");
        System.out.println("4. Check Applicant Status");
        System.out.println("5. Register New Admin");
        System.out.println("6. Issue Enrollment Forms");
        System.out.println("7. View Stats");
        System.out.println("8. View Enrollment Forms");
        System.out.println("9. Enroll Applicant");
        System.out.println("0. Logout");
    }
    public static void printApplicantOptions(){
        System.out.println("\nAPPLICANT PAGE");
        System.out.println("1. Check Status");
        System.out.println("2. Float Seat");
        System.out.println("3. Lock Seat");
        System.out.println("4. Fill/Update Enrollment Details");
        System.out.println("5. View Enrollment ID");
        System.out.println("0. Logout");
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

