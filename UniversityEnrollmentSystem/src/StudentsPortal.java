import java.util.Scanner;

public class StudentsPortal {
    Scanner input = new Scanner(System.in);

    ApplicationForm applicationForm;
    Applicant applicant;
    String firstName,lastName,uniqueId,email,phNo,regNo,title,hscBoard,hscReg;
    Double percentile,maxMarks,obtainedMarks,hscPercentage;
    Examination examination;

    public Applicant.Status fillApplicationForm(){
        System.out.print("First Name: ");   firstName = input.nextLine();
        System.out.print("Last Name: ");    lastName = input.nextLine();
        System.out.print("UIDAI No: ");     uniqueId = input.nextLine();
        System.out.print("Email: ");        email = input.nextLine();
        System.out.print("Phone: ");        phNo = input.nextLine();

        System.out.print("Reg No: ");           regNo = input.nextLine();
        System.out.print("Obtained Marks: ");   obtainedMarks = input.nextDouble();
        System.out.print("Percentile: ");       percentile = input.nextDouble();
        input.nextLine();

        examination = new Examination(University.entrance,University.maxMarks);
        examination.setRegNo(regNo);
        examination.setObtainedMarks(obtainedMarks);
        examination.setPercentile(percentile);

        System.out.print("HSC Board: ");        hscBoard = input.nextLine();
        System.out.print("HSC Reg: ");          hscReg = input.nextLine();
        System.out.print("HSC Percentage: ");   hscPercentage = input.nextDouble();

        applicationForm = new ApplicationForm();
        applicationForm.setName(new Applicant.Name(firstName,lastName));
        applicationForm.setId(Applicant.UniqueId.UIDAI,uniqueId);
        applicationForm.setEmail(email);
        applicationForm.setPhNo(phNo);
        applicationForm.setHsc(hscBoard,hscReg,hscPercentage);

        applicant = new Applicant();
        applicant.setApplicationForm(applicationForm);

        return applicant.apply();
    }
}
