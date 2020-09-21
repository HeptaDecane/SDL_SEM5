import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ServerThread extends Thread{
    public Scanner scanner = new Scanner(System.in);

    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;
    private Socket clientSocket = null;

    private Applicant applicant = null;
    private final StudentPortal studentPortal = new StudentPortal();
    private boolean adminAuthenticated = false;
    private boolean studentAuthenticated = false;
    private Applicant.Status status = null;
    private University.Admin admin = null;

    private String ack,response;
    private int port;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try{
            port = clientSocket.getPort();
            System.out.println(ANSI.GREEN+"connected: "+clientSocket.getInetAddress()+":"+port+ANSI.RESET);
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            homePage();
        }catch (EOFException | SocketException e){
            System.out.println(ANSI.RED+"["+port+"] 499 client closed request"+ANSI.RESET);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println(ANSI.YELLOW+"closed: "+clientSocket.getInetAddress()+":"+port+ANSI.RESET);
            try {
                clientSocket.close();
            }catch (IOException e){ }
        }
    }

    public void homePage() throws Exception{
        System.out.println("["+port+"] home/");
        int choice = dataInputStream.readInt();
        while (choice !=0){
            switch (choice){
                case 1:
                    applicantPortalView();
                break;

                case 2:
                    System.out.println("\n"+"["+port+"] /admin-login");
                    String username = dataInputStream.readUTF();
                    System.out.println("["+port+"] username: "+username);
                    String password = dataInputStream.readUTF();
                    System.out.println("["+port+"] password: "+ new String("*").repeat(password.length()));
                    admin = University.accessAdmin(username,password);
                    if(admin == null) {
                        dataOutputStream.writeBoolean(false);
                        System.out.println(ANSI.RED+"["+port+"] 401 unauthorized"+ANSI.RESET);
                    }
                    else {
                        dataOutputStream.writeBoolean(true);
                        System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                        adminView();
                    }
                break;

                case 3:
                    helpCenter();
                break;

                default:
                    System.out.println(ANSI.RED+"["+port+"] 404 not found"+ANSI.RESET);
                    dataOutputStream.writeUTF("Invalid Selection");
            }
            choice = dataInputStream.readInt();
        }
    }

    public void applicantPortalView() throws Exception{
        System.out.println("\n"+"["+port+"] applicants-portal/");
        int choice = dataInputStream.readInt();
        while (choice!=0){
            switch (choice){
                case 1:
                    System.out.println("\n"+"["+port+"] /applicant-login");
                    String id = dataInputStream.readUTF();
                    System.out.println("["+port+"] id: "+id);
                    String password = dataInputStream.readUTF();
                    System.out.println("["+port+"] password: "+ new String("*").repeat(password.length()));
                    applicant = studentPortal.fetchApplicant(id,password);
                    if(applicant == null) {
                        dataOutputStream.writeBoolean(false);
                        System.out.println(ANSI.RED+"["+port+"] 401 unauthorized"+ANSI.RESET);
                    }
                    else {
                        System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                        dataOutputStream.writeBoolean(true);
                        applicantView();
                    }
                break;

                case 2:
                    System.out.println("\n"+"["+port+"] /applicant-registration");
                    applicantRegistration();
                    String applicantId = studentPortal.register();
                    if(applicantId == null) {
                        System.out.println(ANSI.RED+"["+port+"] 400 bad request"+ANSI.RESET);
                        dataOutputStream.writeUTF("\nRegistration Failed\nInvalid/Duplicate Credentials");
                    }
                    else {
                        System.out.println(ANSI.GREEN+"["+port+"] 201 created"+ANSI.RESET);
                        dataOutputStream.writeUTF("\nRegistration Successful\nApplicantID: " + applicantId);
                    }
                    break;

                default:
                    System.out.println(ANSI.RED+"["+port+"] 404 not found"+ANSI.RESET);
                    dataOutputStream.writeUTF("Invalid Selection");
            }
            choice = dataInputStream.readInt();
        }
        System.out.println("\n"+"["+port+"] home/");
    }

    public void applicantView() throws Exception{
        System.out.println("\n"+"["+port+"] applicants-page/");
        ack="";
        ack += "\nLogged in as:";
        ack += "\nApplicant ID: "+applicant.getApplicationId();
        ack += "\n\tName: "+applicant.getApplicationForm().getName();
        ack += "\n\tEmail: "+applicant.getApplicationForm().getEmail();
        ack += "\n\tPhone: "+applicant.getApplicationForm().getPhNo();
        ack += "\n\tUIDAI: "+applicant.getApplicationForm().getUniqueIdNo();
        ack += "\n\tOpted for: "+applicant.getApplicationForm().getBranchName();
        dataOutputStream.writeUTF(ack);

        int choice = dataInputStream.readInt();
        while (choice!=0){
            switch (choice){
                case 1:
                    System.out.println("["+port+"] /check-status");
                    dataOutputStream.writeUTF("Status: "+applicant.getStatus());
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                    break;

                case 2:
                    System.out.println("["+port+"] /float-seat");
                    status = applicant.hover();
                    if(status != Applicant.Status.FLOATED) {
                        System.out.println(ANSI.RED+"["+port+"] 403 forbidden"+ANSI.RESET);
                        dataOutputStream.writeUTF("Status SHORTLISTED Required");
                    }
                    else {
                        System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                        dataOutputStream.writeUTF("Success\nStatus: " + status);
                    }
                    break;

                case 3:
                    System.out.println("["+port+"] /lock-seat");
                    status = applicant.lock();
                    if(status != Applicant.Status.LOCKED) {
                        System.out.println(ANSI.RED+"["+port+"] 403 forbidden"+ANSI.RESET);
                        dataOutputStream.writeUTF("Status SHORTLISTED/FLOATED Required");
                    }
                    else {
                        System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                        dataOutputStream.writeUTF("Success\nStatus: " + status);
                    }
                    break;

                case 4:
                    System.out.println("["+port+"] /fill-enrollment-details");
                    if(applicant.getEnrollmentForm() == null) {
                        System.out.println(ANSI.RED+"["+port+"] 403 forbidden"+ANSI.RESET);
                        dataOutputStream.writeBoolean(false);
                    }
                    else {
                        dataOutputStream.writeBoolean(true);
                        fillEnrollmentForm();
                        if(studentPortal.submitEnrollmentForm(applicant)) {
                            System.out.println(ANSI.GREEN+"["+port+"] 202 accepted"+ANSI.RESET);
                            dataOutputStream.writeUTF("Submitted Enrollment Form");
                        }
                        else {
                            System.out.println(ANSI.RED+"["+port+"] 400 bad request"+ANSI.RESET);
                            dataOutputStream.writeUTF("Invalid Enrollment Details");
                        }
                    }
                    break;

                case 5:
                    System.out.println("["+port+"] /enrollment-details");
                    dataOutputStream.writeUTF("Enrollment ID: "+applicant.getEnrollmentId());
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                    break;

                default:
                    System.out.println(ANSI.RED+"["+port+"] 404 not found"+ANSI.RESET);
                    dataOutputStream.writeUTF("Invalid Selection");
            }
            choice = dataInputStream.readInt();
        }
        System.out.println("\n"+"["+port+"] applicants-portal/");
    }

    public void adminView() throws Exception {
        System.out.println("\n"+"["+port+"] admins-page/");
        ack = "\nLogged in as: "+admin.getUsername();
        dataOutputStream.writeUTF(ack);

        int choice = dataInputStream.readInt();
        while (choice != 0){
            switch (choice){
                case 1:
                    System.out.println("["+port+"] /list-applicants");
                    dataOutputStream.writeUTF(admin.listApplicants());
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                break;

                case 2:
                    System.out.println("["+port+"] /list-shortlisted");
                    dataOutputStream.writeUTF(admin.listShortlisted());
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                break;

                case 3:
                    System.out.println("["+port+"] /shortlist");
                    admin.shortlistApplicants();
                    dataOutputStream.writeUTF("Applicants Shortlisted");
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                break;

                case 4:
                    System.out.println("["+port+"] /check-status");
                    String id = dataInputStream.readUTF();
                    dataOutputStream.writeUTF("Status: "+admin.checkStatus(id));
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                break;

                case 5:
                    System.out.println("["+port+"] /register-admin");
                    String username = dataInputStream.readUTF();
                    System.out.println("["+port+"] username: "+username);
                    String password = dataInputStream.readUTF();
                    System.out.println("["+port+"] password: "+ new String("*").repeat(password.length()));
                    if(password.length()<8){
                        System.out.println(ANSI.RED+"["+port+"] 400 bad request"+ANSI.RESET);
                        dataOutputStream.writeUTF("Password must be at least 8 characters long");
                    }
                    else {
                        admin.registerNewAdmin(username, password);
                        System.out.println(ANSI.GREEN+"["+port+"] 201 created"+ANSI.RESET);
                        dataOutputStream.writeUTF("Success\nNew Admin " + username + " Registered");
                    }
                break;

                case 6:
                    System.out.println("["+port+"] /issue-enrollment-forms");
                    admin.issueEnrollmentForms();
                    dataOutputStream.writeUTF("Success\nIssued Enrollment-Forms to LOCKED Applicants");
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                break;

                case 7:
                    System.out.println("["+port+"] /view-stats");
                    dataOutputStream.writeUTF(admin.viewStats());
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                break;

                case 8:
                    System.out.println("["+port+"] /list-enrollment-forms");
                    dataOutputStream.writeUTF(admin.viewEnrollmentForms());
                    System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                break;

                case 9:
                    System.out.println("["+port+"] /enroll");
                    String applicantId = dataInputStream.readUTF();
                    if(admin.enrollApplicant(applicantId)) {
                        System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                        dataOutputStream.writeUTF("Success\nEnrolled: " + applicantId);
                    }
                    else {
                        System.out.println(ANSI.RED+"["+port+"] 403 forbidden"+ANSI.RESET);
                        dataOutputStream.writeUTF("Invalid Status for " + applicantId);
                    }
                break;

                default:
                    System.out.println(ANSI.RED+"["+port+"] 404 not found"+ANSI.RESET);
                    dataOutputStream.writeUTF("Invalid Selection");
            }
            choice = dataInputStream.readInt();
        }
        System.out.println("\n"+"["+port+"] home/");
    }

    public void helpCenter() throws Exception{
        System.out.println("\nHELP CENTER");
        String client;
        System.out.println(ANSI.CYAN+"Client: ");
        response = dataInputStream.readUTF();
        System.out.println(response+ANSI.RESET);
        client = dataInputStream.readUTF();

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
        dataOutputStream.writeUTF(admin.getUsername());
        System.out.println(ANSI.GREEN+"\nConnected to Client ("+client+")\n"+ANSI.RESET);
        while (true){
            System.out.print(ANSI.YELLOW+client+": ..."+ANSI.RESET);
            response = dataInputStream.readUTF();
            System.out.println("\b\b\b"+ANSI.CYAN+response+ANSI.RESET);
            if(response.equals("exit()"))
                break;
            System.out.print("You: ");
            message = scanner.nextLine();
            dataOutputStream.writeUTF(message);
            if(message.equals("exit()"))
                break;
        }
        System.out.println("["+port+"] home/");
    }

    public void applicantRegistration() throws Exception{
        SerializedApplicationForm applicationForm = new SerializedApplicationForm();
        applicationForm.setExam(University.entrance);
        objectOutputStream.writeObject(applicationForm);
        applicationForm = (SerializedApplicationForm) objectInputStream.readObject();

        studentPortal.firstName = applicationForm.getFirstName();
        studentPortal.lastName = applicationForm.getLastName();
        studentPortal.uniqueId = applicationForm.getUniqueId();
        studentPortal.email = applicationForm.getEmail();
        studentPortal.phNo = applicationForm.getPhNo();
        studentPortal.password = applicationForm.getPassword();
        studentPortal.regNo = applicationForm.getRegNo();
        studentPortal.obtainedMarks = applicationForm.getObtainedMarks();
        studentPortal.percentile = applicationForm.getPercentile();
        studentPortal.hscBoard = applicationForm.getHscBoard();
        studentPortal.hscReg = applicationForm.getHscRegNo();
        studentPortal.hscPercentage = applicationForm.getHscPercentage();

        ack = "";
        for(University.Branch branch:StudentPortal.branches)
            ack += "\t"+branch+"\n";
        dataOutputStream.writeUTF(ack);
        while (true){
            studentPortal.branchName = dataInputStream.readUTF();
            dataOutputStream.writeBoolean(studentPortal.validBranch());
            if (studentPortal.validBranch())
                break;
        }

        applicationForm.setBranchName(studentPortal.branchName);
        System.out.println(ANSI.CYAN+applicationForm+ANSI.RESET);
    }

    public void fillEnrollmentForm() throws Exception{
        studentPortal.placeholder = dataInputStream.readUTF();
    }

    public String readPassword() {
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
