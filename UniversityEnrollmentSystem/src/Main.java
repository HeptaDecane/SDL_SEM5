import java.util.Scanner;

public class Main {
    public static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        StudentPortal studentPortal = new StudentPortal();

        System.out.println("Applicant ID: "+studentPortal.register());

//        System.out.println(studentPortal.checkStatus("I2508202000001"));

//        Applicant applicant = studentPortal.fetchApplicant("I2708202000001","root1234");
//        if(applicant != null)
//            System.out.println(applicant.getStatus());

//        Admin admin = new Admin("admin");
//        admin.shortlistApplicants();

        Data.listApplicants();
//        Data.listShortlisted();

//        University.Admin admin = University.accessAdmin("admin01","testpass08");
//        System.out.println(admin);
//        admin.registerNewAdmin("admin08","testpass08");
    }

}
