import java.io.Console;
import java.util.Scanner;

public class CLI {
    public static Scanner input = new Scanner(System.in);

    private static Applicant applicant = null;
    private static final StudentPortal studentPortal = new StudentPortal();
    private static boolean adminAuthenticated = false;
    private static boolean studentAuthenticated = false;
    private static Applicant.Status status = null;
    private static University.Admin admin = null;

    public static void main(String[] args) {


        System.out.println("1. Student Portal\n2. Admin Login\n0. Exit");
        int choice = input.nextInt();
        while (choice != 0) {
            switch (choice){
                case 1:
                    studentPortalView();
                break;

                case 2:
                    input.nextLine();
                    System.out.print("Username: ");
                    String username = input.nextLine();
                    System.out.print("Password: ");
                    String password = readPassword();
                    admin = University.accessAdmin(username,password);
                    if(admin == null)
                        System.out.println("Incorrect Admin Credentials");
                    else
                        adminView();
                break;

                default:
                    System.out.println("Invalid Selection");
            }
            System.out.println("1. Student Portal\n2. Admin Login\n0. Exit");
            choice = input.nextInt();
        }

    }

    public static void printAdminOptions(){
        System.out.println("1. List Applicants");
        System.out.println("2. List Shortlisted");
        System.out.println("3. Shortlist Applicants");
        System.out.println("4. Check Applicant Status");
        System.out.println("5. Register New Admin");
        System.out.println("6. Enroll Applicants");
        System.out.println("0. Logout");
    }
    public static void printApplicantOptions(){
        System.out.println("1. Check Status");
        System.out.println("2. Lock Seat");
        System.out.println("3. Accept Seat");
        System.out.println("4. Fill Enrollment Details");
        System.out.println("0. Logout");
    }

    public static void studentPortalView(){
        System.out.println("1. Applicant Login\n2. Applicant Register\n0. Back");
        int choice = input.nextInt();

        while (choice != 0) {
            switch (choice){
                case 1:
                    input.nextLine();
                    System.out.print("ApplicantID: ");
                    String id = input.nextLine();
                    System.out.print("Password: ");
                    String password = readPassword();
                    applicant = studentPortal.fetchApplicant(id,password);
                    if(applicant == null)
                        System.out.println("Incorrect ApplicantId, Password. Try Again");
                    else
                        applicantView();
                    break;

                case 2:
                    String applicantId = studentPortal.register();
                    if(applicantId == null)
                        System.out.println("Registration Failed\nInvalid/Duplicate Credentials");
                    else
                        System.out.println("Registration Successful\nApplicantID: "+applicantId);
                    break;

                default:
                    System.out.println("Invalid Selection");
            }
            System.out.println("1. Applicant Login\n2. Applicant Register\n0. Back");
            choice = input.nextInt();
        }
    }

    public static void applicantView(){

        printApplicantOptions();
        int choice = input.nextInt();
        while (choice != 0){
            switch (choice){
                case 1:
                    System.out.println(applicant.getStatus());
                break;

                case 2:
                    status = applicant.lock();
                    if(status != Applicant.Status.LOCKED)
                        System.out.println("Status SHORTLISTED Required");
                    else
                        System.out.println("Success\nStatus: "+status);
                break;

                case 3:
                    status = applicant.accept();
                    if(status != Applicant.Status.ACCEPTED)
                        System.out.println("Status SHORTLISTED/LOCKED Required");
                    else
                        System.out.println("Success\nStatus: "+status);
                break;

                case 4:
                    if(applicant.getEnrollmentForm() == null)
                        System.out.println("Enrollment Form Not Issued");
                    else
                        System.out.println("Enrollment Form Filling Procedure");

                default:
                    System.out.println("Invalid Selection");

            }
            printApplicantOptions();
            choice = input.nextInt();
        }

    }

    public static void adminView(){
        printAdminOptions();
        int choice = input.nextInt();

        while (choice != 0){
            switch (choice){
                case 1:
                    admin.listApplicants();
                break;

                case 2:
                    admin.listShortlisted();
                break;

                case 3:
                    admin.shortlistApplicants();
                break;

                case 4:
                    input.nextLine();
                    System.out.print("Applicant ID: ");
                    String id = input.nextLine();
                    System.out.println(admin.checkStatus(id));
                break;

                case 5:
                    input.nextLine();
                    System.out.print("Username: ");
                    String username = input.nextLine();
                    System.out.print("Password: ");
                    String password = readPassword();
                    admin.registerNewAdmin(username,password);
                    System.out.println("New Admin "+username+" Registered");
                break;

                case 6:
                    System.out.println("Not Implemented");
                break;

                default:
                    System.out.println("Invalid Selection");
            }
            printAdminOptions();
            choice = input.nextInt();
        }
    }

    public static String readPassword() {
        Console console = System.console();

        String password = null;
        try{
            char[] ch = console.readPassword();
            password = new String(ch);
        }catch(Exception e){
            password = input.nextLine();
        }
        return password;
    }
}
