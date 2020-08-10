import java.util.Scanner;

public class Main {
    public static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        ApplicationForm applicationForm;
        Applicant applicant = new Applicant();


        String firstName,lastName,uniqueId,email,phNo,regNo,title,hscBoard,hscReg;
        Double percentile,maxMarks,obtainedMarks,hscPercentage;

        System.out.print("First Name: ");firstName = input.nextLine();
        System.out.print("Last Name: ");lastName = input.nextLine();
        System.out.print("UIDAI No: ");uniqueId = input.nextLine();
        System.out.print("Email: ");email = input.nextLine();
        System.out.print("Phone: ");phNo = input.nextLine();

        System.out.print("Exam1: ");title = input.nextLine();
        System.out.print("Reg No: ");regNo = input.nextLine();
        System.out.print("Max Marks: ");maxMarks = input.nextDouble();
        System.out.print("Obtained Marks: ");obtainedMarks = input.nextDouble();
        System.out.print("Percentile: ");percentile = input.nextDouble();
        input.nextLine();

        Examination exam1 = new Examination(title);
        exam1.setRegNo(regNo);
        exam1.setMaxMarks(maxMarks);
        exam1.setObtainedMarks(obtainedMarks);
        exam1.setPercentile(percentile);

        System.out.print("Exam2: ");title = input.nextLine();
        System.out.print("Reg No: ");regNo = input.nextLine();
        System.out.print("Max Marks: ");maxMarks = input.nextDouble();
        System.out.print("Obtained Marks: ");obtainedMarks = input.nextDouble();
        System.out.print("Percentile: ");percentile = input.nextDouble();
        input.nextLine();

        Examination exam2 = new Examination(title);
        exam2.setRegNo(regNo);
        exam2.setMaxMarks(maxMarks);
        exam2.setObtainedMarks(obtainedMarks);
        exam2.setPercentile(percentile);

        System.out.print("HSC Board: ");hscBoard = input.nextLine();
        System.out.print("HSC Reg: ");hscReg = input.nextLine();
        System.out.print("HSC Percentage: ");hscPercentage = input.nextDouble();

        applicationForm = new ApplicationForm();
        applicationForm.setName(new Applicant.Name(firstName,lastName));
        applicationForm.setId(Applicant.UniqueId.UIDAI,uniqueId);
        applicationForm.setEmail(email);
        applicationForm.setPhNo(phNo);
        applicationForm.addExamination(exam1);
        applicationForm.addExamination(exam2);
        applicationForm.setHsc(hscBoard,hscReg,hscPercentage);

        applicant.fillApplicationForm(applicationForm);
        System.out.println(applicant.apply());

    }

}
