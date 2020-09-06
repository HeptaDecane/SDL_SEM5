import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public static Scanner scanner = new Scanner(System.in);
    private static DataOutputStream outputStream = null;
    private static DataInputStream inputStream = null;
    private static String response = "";

    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",5000)){
            System.out.println(ANSI.GREEN+"connected: "+socket.getInetAddress()+":"+socket.getPort()+ANSI.RESET);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            homePage();
        }catch (SocketException e){
            System.out.println(ANSI.RED+"500 internal server error"+ANSI.RESET);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void homePage() throws Exception{
        System.out.println("\nHOME PAGE");
        System.out.println("1. Student Portal\n2. Admin Login\n0. Exit");
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        outputStream.writeInt(choice);
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
                    outputStream.writeUTF(username);

                    System.out.print("Password: ");
                    String password = readPassword();
                    outputStream.writeUTF(password);

                    boolean validCredentials = inputStream.readBoolean();
                    if(validCredentials)
                        adminView();
                    else
                        System.out.println("Incorrect ApplicantId, Password. Try Again");

                    break;

                default:
                    response = inputStream.readUTF();
                    System.out.println(response);
            }
            System.out.println("\nHOME PAGE");
            System.out.println("1. Student Portal\n2. Admin Login\n0. Exit");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            outputStream.writeInt(choice);
        }
    }

    public static void applicantPortalView() throws Exception{
        System.out.println("\nAPPLICANT PORTAL");
        System.out.println("1. Applicant Login\n2. Applicant Register\n0. Back");
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        outputStream.writeInt(choice);
        while (choice != 0) {
            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.println("\nAPPLICANT LOGIN");
                    System.out.print("ApplicantID: ");
                    String id = scanner.nextLine();
                    outputStream.writeUTF(id);

                    System.out.print("Password: ");
                    String password = readPassword();
                    outputStream.writeUTF(password);

                    boolean validCredentials = inputStream.readBoolean();
                    if(validCredentials)
                        applicantView();
                    else
                        System.out.println("Incorrect ApplicantId, Password. Try Again");

                    break;

                case 2:
                    scanner.nextLine();
                    fillApplicationForm();
                    response = inputStream.readUTF();
                    System.out.println(response);
                    break;

                default:
                    response = inputStream.readUTF();
                    System.out.println(response);
            }
            System.out.println("\nAPPLICANT PORTAL");
            System.out.println("1. Applicant Login\n2. Applicant Register\n0. Back");
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            outputStream.writeInt(choice);

        }
    }

    public static void applicantView() throws Exception{
        System.out.println("APPLICANTS PAGE");
        response = inputStream.readUTF();
        System.out.println(response);

        printApplicantOptions();
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        outputStream.writeInt(choice);

        while (choice != 0){
            switch (choice){
                case 4:
                    boolean enrollmentForm = inputStream.readBoolean();
                    if(enrollmentForm) {
                        scanner.nextLine();
                        fillEnrollmentForm();
                        response = inputStream.readUTF();
                        System.out.println(response);
                    }
                    else
                        System.out.println("Enrollment Form Not Issued");
                break ;

                default:
                    response = inputStream.readUTF();
                    System.out.println(response);
            }

            printApplicantOptions();
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            outputStream.writeInt(choice);
        }

    }

    public static void adminView() throws Exception{
        System.out.println("ADMINS PAGE");
        response = inputStream.readUTF();
        System.out.println(response);

        printAdminOptions();
        System.out.print("\ninput> ");
        int choice = scanner.nextInt();
        outputStream.writeInt(choice);

        while (choice != 0){
            switch (choice){

                case 4:
                case 9:
                    scanner.nextLine();
                    System.out.print("Applicant ID: ");
                    String id = scanner.nextLine();
                    outputStream.writeUTF(id);
                    response = inputStream.readUTF();
                    System.out.println(response);
                break;

                case 5:
                    scanner.nextLine();
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    outputStream.writeUTF(username);

                    System.out.print("Password: ");
                    String password = readPassword();
                    outputStream.writeUTF(password);

                    response = inputStream.readUTF();
                    System.out.println(response);
                break;

                default:
                    response = inputStream.readUTF();
                    System.out.println(response);

            }
            printAdminOptions();
            System.out.print("\ninput> ");
            choice = scanner.nextInt();
            outputStream.writeInt(choice);
        }

    }

    public static void fillApplicationForm() throws Exception{
        System.out.println("\nAPPLICANT REGISTRATION");

        System.out.print("First Name: ");
        outputStream.writeUTF(scanner.nextLine());
        System.out.print("Last Name: ");
        outputStream.writeUTF(scanner.nextLine());
        System.out.print("UIDAI No: ");
        outputStream.writeUTF(scanner.nextLine());
        System.out.print("Email: ");
        outputStream.writeUTF(scanner.nextLine());
        System.out.print("Phone: ");
        outputStream.writeUTF(scanner.nextLine());

        while (true){
            System.out.print("Password: ");
            outputStream.writeUTF(scanner.nextLine());
            boolean validPassword = inputStream.readBoolean();
            if(validPassword)
                break;
            System.out.println("Password must be at least 8 characters long");
        }

        response = inputStream.readUTF();
        System.out.println(response);
        System.out.print("Reg No: ");
        outputStream.writeUTF(scanner.nextLine());
        System.out.print("Obtained Marks: ");
        outputStream.writeDouble(scanner.nextDouble());
        System.out.print("Percentile: ");
        outputStream.writeDouble(scanner.nextDouble());

        scanner.nextLine();
        System.out.print("HSC Board: ");
        outputStream.writeUTF(scanner.nextLine());
        System.out.print("HSC Reg: ");
        outputStream.writeUTF(scanner.nextLine());
        System.out.print("HSC Percentage: ");
        outputStream.writeDouble(scanner.nextDouble());

        scanner.nextLine();
        System.out.println("Branches Offered: ");
        response = inputStream.readUTF();
        System.out.println(response);
        while (true){
            System.out.print("Select: ");
            outputStream.writeUTF(scanner.nextLine());
            boolean validBranch = inputStream.readBoolean();
            if(validBranch)
                break;
            System.out.println("Invalid Selection");
        }
    }

    public static void fillEnrollmentForm() throws Exception{

        System.out.println("\nENROLLMENT FORM");
        System.out.print("Placeholder: ");
        outputStream.writeUTF(scanner.nextLine());
    }

    public static void printAdminOptions(){
        System.out.println("\nADMIN PAGE");
        System.out.println("1. List Applicants");
        System.out.println("2. List Shortlisted");
        System.out.println("3. Shortlist Applicants");
        System.out.println("4. Check Applicant Status");
        System.out.println("5. Register New Admin");
        System.out.println("6. Issue Enrollment Forms");
        System.out.println("7. View Seat Status");
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
