import java.util.Scanner;

public class Main {
    public static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        StudentPortal studentPortal = new StudentPortal();
        System.out.println("Applicant ID: "+studentPortal.register());
//        System.out.println(studentPortal.checkStatus("I1808202000009"));
//        System.out.println(studentPortal.fetchApplicant("I180820200000"));
    }

}
