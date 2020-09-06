import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends StudentPortal {
    public static Scanner scanner = new Scanner(System.in);

    private static DataOutputStream outputStream = null;
    private static DataInputStream inputStream = null;
    private static Socket clientSocket = null;

    private static Applicant applicant = null;
    private static final StudentPortal studentPortal = new StudentPortal();
    private static boolean adminAuthenticated = false;
    private static boolean studentAuthenticated = false;
    private static Applicant.Status status = null;
    private static University.Admin admin = null;

    private static String ack,response;

    public static void main(String[] args) {
        while (true){
            try(ServerSocket serverSocket = new ServerSocket(5000)){
                System.out.println(ANSI.YELLOW+"\nlistening @ port:5000"+ANSI.RESET);
                clientSocket = serverSocket.accept();
                System.out.println(ANSI.GREEN+"connected: "+clientSocket.getInetAddress()+":"+clientSocket.getPort()+ANSI.RESET);
                inputStream = new DataInputStream(clientSocket.getInputStream());
                outputStream = new DataOutputStream(clientSocket.getOutputStream());
                homePage();
            }catch (EOFException e){
                System.out.println(ANSI.RED+"499 client closed request"+ANSI.RESET);
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println(ANSI.YELLOW+"closed: "+clientSocket.getInetAddress()+":"+clientSocket.getPort()+ANSI.RESET);
        }
    }

    public static void homePage() throws Exception{
        System.out.println("home/");
        int choice = inputStream.readInt();
        while (choice !=0){
            switch (choice){
                case 1:
                    applicantPortalView();
                break;

                case 2:
                    System.out.println("\n/admin-login");
                    String username = inputStream.readUTF();
                    System.out.println("username: "+username);
                    String password = inputStream.readUTF();
                    System.out.println("password: "+ new String("*").repeat(password.length()));
                    admin = University.accessAdmin(username,password);
                    if(admin == null) {
                        outputStream.writeBoolean(false);
                        System.out.println(ANSI.RED+"401 unauthorized"+ANSI.RESET);
                    }
                    else {
                        outputStream.writeBoolean(true);
                        System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                        adminView();
                    }
                break;

                case 3:
                    helpCenter();
                break;

                default:
                    System.out.println(ANSI.RED+"404 not found"+ANSI.RESET);
                    outputStream.writeUTF("Invalid Selection");
            }
            choice = inputStream.readInt();
        }
    }

    public static void applicantPortalView() throws Exception{
        System.out.println("\napplicants-portal/");
        int choice = inputStream.readInt();
        while (choice!=0){
            switch (choice){
                case 1:
                    System.out.println("\n/applicant-login");
                    String id = inputStream.readUTF();
                    System.out.println("id: "+id);
                    String password = inputStream.readUTF();
                    System.out.println("password: "+ new String("*").repeat(password.length()));
                    applicant = studentPortal.fetchApplicant(id,password);
                    if(applicant == null) {
                        outputStream.writeBoolean(false);
                        System.out.println(ANSI.RED+"401 unauthorized"+ANSI.RESET);
                    }
                    else {
                        System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                        outputStream.writeBoolean(true);
                        applicantView();
                    }
                break;

                case 2:
                    System.out.println("\n/applicant-registration");
                    applicantRegistration();
                    String applicantId = studentPortal.register();
                    if(applicantId == null) {
                        System.out.println(ANSI.RED+"400 bad request"+ANSI.RESET);
                        outputStream.writeUTF("\nRegistration Failed\nInvalid/Duplicate Credentials");
                    }
                    else {
                        System.out.println(ANSI.GREEN+"201 created"+ANSI.RESET);
                        outputStream.writeUTF("\nRegistration Successful\nApplicantID: " + applicantId);
                    }
                    break;

                default:
                    System.out.println(ANSI.RED+"404 not found"+ANSI.RESET);
                    outputStream.writeUTF("Invalid Selection");
            }
            choice = inputStream.readInt();
        }
        System.out.println("\nhome/");
    }

    public static void applicantView() throws Exception{
        System.out.println("\napplicants-page/");
        ack="";
        ack += "\nLogged in as:";
        ack += "\nApplicant ID: "+applicant.getApplicationId();
        ack += "\n\tName: "+applicant.getApplicationForm().getName();
        ack += "\n\tEmail: "+applicant.getApplicationForm().getEmail();
        ack += "\n\tPhone: "+applicant.getApplicationForm().getPhNo();
        ack += "\n\tUIDAI: "+applicant.getApplicationForm().getUniqueIdNo();
        ack += "\n\tOpted for: "+applicant.getApplicationForm().getBranchName();
        outputStream.writeUTF(ack);

        int choice = inputStream.readInt();
        while (choice!=0){
            switch (choice){
                case 1:
                    System.out.println("/check-status");
                    outputStream.writeUTF("Status: "+applicant.getStatus());
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                    break;

                case 2:
                    System.out.println("/float-seat");
                    status = applicant.hover();
                    if(status != Applicant.Status.FLOATED) {
                        System.out.println(ANSI.RED+"403 forbidden"+ANSI.RESET);
                        outputStream.writeUTF("Status SHORTLISTED Required");
                    }
                    else {
                        System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                        outputStream.writeUTF("Success\nStatus: " + status);
                    }
                    break;

                case 3:
                    System.out.println("/lock-seat");
                    status = applicant.lock();
                    if(status != Applicant.Status.LOCKED) {
                        System.out.println(ANSI.RED+"403 forbidden"+ANSI.RESET);
                        outputStream.writeUTF("Status SHORTLISTED/FLOATED Required");
                    }
                    else {
                        System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                        outputStream.writeUTF("Success\nStatus: " + status);
                    }
                    break;

                case 4:
                    System.out.println("/fill-enrollment-details");
                    if(applicant.getEnrollmentForm() == null) {
                        System.out.println(ANSI.RED+"403 forbidden"+ANSI.RESET);
                        outputStream.writeBoolean(false);
                    }
                    else {
                        outputStream.writeBoolean(true);
                        fillEnrollmentForm();
                        if(studentPortal.submitEnrollmentForm(applicant)) {
                            System.out.println(ANSI.GREEN+"202 accepted"+ANSI.RESET);
                            outputStream.writeUTF("Submitted Enrollment Form");
                        }
                        else {
                            System.out.println(ANSI.RED+"400 bad request"+ANSI.RESET);
                            outputStream.writeUTF("Invalid Enrollment Details");
                        }
                    }
                    break;

                case 5:
                    System.out.println("/enrollment-details");
                    outputStream.writeUTF("Enrollment ID: "+applicant.getEnrollmentId());
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                    break;

                default:
                    System.out.println(ANSI.RED+"404 not found"+ANSI.RESET);
                    outputStream.writeUTF("Invalid Selection");
            }
            choice = inputStream.readInt();
        }
        System.out.println("\napplicants-portal/");
    }

    public static void adminView() throws Exception {
        System.out.println("\nadmins-page/");
        ack = "\nLogged in as: "+admin.getUsername();
        outputStream.writeUTF(ack);

        int choice = inputStream.readInt();
        while (choice != 0){
            switch (choice){
                case 1:
                    System.out.println("/list-applicants");
                    outputStream.writeUTF(admin.listApplicants());
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                break;

                case 2:
                    System.out.println("/list-shortlisted");
                    outputStream.writeUTF(admin.listShortlisted());
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                break;

                case 3:
                    System.out.println("/shortlist");
                    admin.shortlistApplicants();
                    outputStream.writeUTF("Applicants Shortlisted");
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                break;

                case 4:
                    System.out.println("/check-status");
                    String id = inputStream.readUTF();
                    outputStream.writeUTF("Status: "+admin.checkStatus(id));
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                break;

                case 5:
                    System.out.println("/register-admin");
                    String username = inputStream.readUTF();
                    System.out.println("username: "+username);
                    String password = inputStream.readUTF();
                    System.out.println("password: "+ new String("*").repeat(password.length()));
                    if(password.length()<8){
                        System.out.println(ANSI.RED+"400 bad request"+ANSI.RESET);
                        outputStream.writeUTF("Password must be at least 8 characters long");
                    }
                    else {
                        admin.registerNewAdmin(username, password);
                        System.out.println(ANSI.GREEN+"201 created"+ANSI.RESET);
                        outputStream.writeUTF("Success\nNew Admin " + username + " Registered");
                    }
                break;

                case 6:
                    System.out.println("/issue-enrollment-forms");
                    admin.issueEnrollmentForms();
                    outputStream.writeUTF("Success\nIssued Enrollment-Forms to LOCKED Applicants");
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                break;

                case 7:
                    System.out.println("/view-stats");
                    outputStream.writeUTF(admin.viewSeatStatus());
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                break;

                case 8:
                    System.out.println("/list-enrollment-forms");
                    outputStream.writeUTF(admin.viewEnrollmentForms());
                    System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                break;

                case 9:
                    System.out.println("/enroll");
                    String applicantId = inputStream.readUTF();
                    if(admin.enrollApplicant(applicantId)) {
                        System.out.println(ANSI.GREEN+"200 ok"+ANSI.RESET);
                        outputStream.writeUTF("Success\nEnrolled: " + applicantId);
                    }
                    else {
                        System.out.println(ANSI.RED+"403 forbidden"+ANSI.RESET);
                        outputStream.writeUTF("Invalid Status for " + applicantId);
                    }
                break;

                default:
                    System.out.println(ANSI.RED+"404 not found"+ANSI.RESET);
                    outputStream.writeUTF("Invalid Selection");
            }
            choice = inputStream.readInt();
        }
        System.out.println("\nhome/");
    }

    public static void helpCenter() throws Exception{
        System.out.println("\nHELP CENTER");
        String client;
        System.out.println(ANSI.CYAN+"Client: ");
        response = inputStream.readUTF();
        System.out.println(response+ANSI.RESET);
        client = inputStream.readUTF();

        String username,password,message;
        System.out.println("\nAdmin Credentials Required:");
        while (true) {
            System.out.print("username: ");
            username = scanner.nextLine();
            System.out.print("password: ");
            password = readPassword();
            admin = University.accessAdmin(username,password);
            if(admin == null)
                System.out.println(ANSI.CYAN+"Incorrect Username, Password. Try Again"+ANSI.RESET);
            else
                break;
        }
        outputStream.writeUTF(admin.getUsername());
        System.out.println(ANSI.GREEN+"\nConnected to Client ("+client+")\n"+ANSI.RESET);
        while (true){
            System.out.print(ANSI.YELLOW+client+": ..."+ANSI.RESET);
            response = inputStream.readUTF();
            System.out.println("\b\b\b"+ANSI.CYAN+response+ANSI.RESET);
            if(response.equals("exit()"))
                break;
            System.out.print("You: ");
            message = scanner.nextLine();
            outputStream.writeUTF(message);
            if(message.equals("exit()"))
                break;
        }
        System.out.println("home/");
    }

    public static void applicantRegistration() throws Exception{
        firstName = inputStream.readUTF();
        lastName = inputStream.readUTF();
        uniqueId = inputStream.readUTF();
        email = inputStream.readUTF();
        phNo = inputStream.readUTF();
        while (true){
            password = inputStream.readUTF();
            outputStream.writeBoolean(studentPortal.validPassword());
            if (studentPortal.validPassword())
                break;
        }
        outputStream.writeUTF("Entrance Details: "+University.entrance);
        regNo = inputStream.readUTF();
        obtainedMarks = inputStream.readDouble();
        percentile = inputStream.readDouble();

        hscBoard = inputStream.readUTF();
        hscReg = inputStream.readUTF();
        hscPercentage = inputStream.readDouble();

        ack = "";
        for(University.Branch branch:branches)
            ack += "\t"+branch+"\n";
        outputStream.writeUTF(ack);
        while (true){
            branchName = inputStream.readUTF();
            outputStream.writeBoolean(studentPortal.validBranch());
            if (studentPortal.validBranch())
                break;
        }
    }

    public static void fillEnrollmentForm() throws Exception{
        placeholder = inputStream.readUTF();
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
