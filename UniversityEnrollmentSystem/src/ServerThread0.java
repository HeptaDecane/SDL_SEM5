import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class ServerThread0 extends Thread{
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
    private Admin admin = null;

    private String ack,response;
    private int port;

    public ServerThread0(Socket clientSocket) {
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
                    System.out.println("["+port+"] password: "+ "*".repeat(password.length()));
                    admin = Admin.accessAdmin(username,password);
                    if(admin == null) {
                        dataOutputStream.writeBoolean(false);
                        System.out.println(ANSI.RED+"["+port+"] 401 unauthorized"+ANSI.RESET);
                    }
                    else {
                        dataOutputStream.writeBoolean(true);
                        System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                        adminView(SessionHandler.getOrCreateAdminSession(admin.getUsername()));
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
                    System.out.println("["+port+"] password: "+ "*".repeat(password.length()));
                    applicant = studentPortal.fetchApplicant(id,password);
                    if(applicant == null) {
                        dataOutputStream.writeBoolean(false);
                        System.out.println(ANSI.RED+"["+port+"] 401 unauthorized"+ANSI.RESET);
                    }
                    else {
                        System.out.println(ANSI.GREEN+"["+port+"] 200 ok"+ANSI.RESET);
                        dataOutputStream.writeBoolean(true);
                        applicantView(SessionHandler.getOrCreateApplicantSession(applicant.getApplicationId()));
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

    public void applicantView(Session session) throws Exception{
        Lock lock = session.getLock();
        if(lock.tryLock(5000L, TimeUnit.MILLISECONDS)){
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
            session.updateLastActivity();
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
                session.updateLastActivity();
            }
            System.out.println("\n"+"["+port+"] applicants-portal/");
            lock.unlock();
            SessionHandler.deleteApplicantSession(applicant.getApplicationId());
        }else {
            ack="409 conflict";
            System.out.println(ANSI.RED+"["+port+"] "+ack+ANSI.RESET);
            dataOutputStream.writeUTF(ack);
        }
    }

    public void adminView(Session session) throws Exception {
        Lock lock = session.getLock();
        if(lock.tryLock(5000L,TimeUnit.MILLISECONDS)){
            System.out.println("\n"+"["+port+"] admins-page/");
            ack = "\nLogged in as: "+admin.getUsername();
            dataOutputStream.writeUTF(ack);

            int choice = dataInputStream.readInt();
            session.updateLastActivity();
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
                        String name = dataInputStream.readUTF();
                        System.out.println("["+port+"] name: "+name);
                        String password = dataInputStream.readUTF();
                        System.out.println("["+port+"] password: "+ "*".repeat(password.length()));
                        if(password.length()<8){
                            System.out.println(ANSI.RED+"["+port+"] 400 bad request"+ANSI.RESET);
                            dataOutputStream.writeUTF("Password must be at least 8 characters long");
                        }
                        else {
                            admin.registerNewAdmin(username,name,password);
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

                    case 10:
                        support(session);
                        break;

                    default:
                        System.out.println(ANSI.RED+"["+port+"] 404 not found"+ANSI.RESET);
                        dataOutputStream.writeUTF("Invalid Selection");
                }
                choice = dataInputStream.readInt();
                session.updateLastActivity();
            }
            System.out.println("\n"+"["+port+"] home/");
            lock.unlock();
            SessionHandler.deleteAdminSession(admin.getUsername());
        }
        else{
            ack="409 conflict";
            System.out.println(ANSI.RED+"["+port+"] "+ack+ANSI.RESET);
            dataOutputStream.writeUTF(ack);
        }
    }

    public void support(Session session) throws Exception{
        Support support = null;
        String ticketNo;
        System.out.println("\n"+"["+port+"] support/");
        dataOutputStream.writeUTF(admin.listQueries());
        while (true){
            ticketNo = dataInputStream.readUTF();
            session.updateLastActivity();
            if(ticketNo.equals("exit()")) {
                System.out.println("\n"+"["+port+"] admins-page/");
                return;
            }
            support = Database.getSupportObject(ticketNo);
            if(support != null){
                dataOutputStream.writeBoolean(true);
                break;
            }
            dataOutputStream.writeBoolean(false);
        }
        dataOutputStream.writeUTF(support.getConversation(true));
        while (true){
            String message = dataInputStream.readUTF();
            session.updateLastActivity();
            if (message.equals("exit()")) {
                System.out.println("\n"+"["+port+"] admins-page/");
                break;
            }
            if(support.getAdminUsername() == null)
                support.setAdminUsername(admin.getUsername());
            support.post(message,true);
        }
        System.out.println("\n"+"["+port+"] admins-page/");
    }

    public void helpCenter() throws Exception{
        System.out.println("\n"+"["+port+"] help-center/");
        int choice = dataInputStream.readInt();
        while (choice!=0){
            switch (choice){
                case 1:
                    newSupportTicket();
                break;

                case 2:
                    existingSupportTicket();
                break;

                default:
                    System.out.println(ANSI.RED+"["+port+"] 404 not found"+ANSI.RESET);
                    dataOutputStream.writeUTF("Invalid Selection");

            }
            choice = dataInputStream.readInt();
        }

        System.out.println("["+port+"] home/");
    }

    public void newSupportTicket() throws Exception{
        Support support = new Support();
        String isRegistered = dataInputStream.readUTF();
        if(isRegistered.equalsIgnoreCase("yes")){
            String applicantId = dataInputStream.readUTF();
            boolean validApplicantId = support.setApplicantId(applicantId);
            dataOutputStream.writeBoolean(validApplicantId);
            if(!validApplicantId) return;
        }
        else
            support.setClientName(dataInputStream.readUTF());

        support.generateTicketNo();
        Database.addSupportObject(support);
        dataOutputStream.writeUTF(support.getTicketNo());
        existingSupportTicket();
    }
    public void existingSupportTicket() throws Exception{
        String ticketNo = dataInputStream.readUTF();
        Support support = Database.getSupportObject(ticketNo);
        dataOutputStream.writeBoolean(support == null);
        if(support == null) return;
        int choice = dataInputStream.readInt();
        while (choice!=0){
            switch (choice){
                case 1:
                    dataOutputStream.writeUTF(support.getConversation(false));
                    dataOutputStream.writeBoolean(support.isResolved());
                    if(!support.isResolved())
                        while (true){
                            String message = dataInputStream.readUTF();
                            if(message.equals("exit()"))
                                break;
                            support.post(message,false);
                        }
                break;

                case 2:
                    if(!support.isResolved())
                        support.setResolved(true);
                    dataOutputStream.writeUTF("Ticket No: "+ticketNo+" Marked Resolved");
                break;

                default:
                    System.out.println(ANSI.RED+"["+port+"] 404 not found"+ANSI.RESET);
                    dataOutputStream.writeUTF("Invalid Selection");
            }
            choice = dataInputStream.readInt();
        }
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
        byte bytes[] = null;
        FileOutputStream fileOutputStream = null;
        int size = 0;

        try{
            receiveFile(applicant.getApplicationId()+"-form.pdf");
            receiveFile(applicant.getApplicationId()+"-hsc.pdf");
            receiveFile(applicant.getApplicationId()+"-entrance.pdf");
            studentPortal.form = "media/"+applicant.getApplicationId()+"-form.pdf";
            studentPortal.hscMarkSheet = "media/"+applicant.getApplicationId()+"-hsc.pdf";
            studentPortal.entranceMarkSheet = "media/"+applicant.getApplicationId()+"-entrance.pdf";

            dataOutputStream.writeBoolean(true);
        } catch (Exception e){
            studentPortal.form = "EMPTY";
            studentPortal.hscMarkSheet = "EMPTY";
            studentPortal.entranceMarkSheet = "EMPTY";
            dataOutputStream.writeBoolean(false);
        }
    }

    private void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream("media/"+fileName);

        long size = dataInputStream.readLong();
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;
        }
        fileOutputStream.close();
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
