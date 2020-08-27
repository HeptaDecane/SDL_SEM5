import java.util.Scanner;
import java.util.Set;

public class StudentPortal {
    Scanner input = new Scanner(System.in);

    private ApplicationForm applicationForm;
    private Applicant applicant;
    private String firstName,password,lastName,uniqueId,email,phNo,regNo,branchName,hscBoard,hscReg;
    private Double percentile,obtainedMarks,hscPercentage;
    private Examination examination;
    private static Set<University.Branch> branches;

    static {
        branches = University.getBranches();
    }

    private boolean selectBranch(){
        System.out.print("Select: ");   branchName = input.nextLine();
        for(University.Branch branch:branches){
            if(branchName.equals(branch.getName()))
                return true;
        }
        return false;
    }
    private boolean setPassword(){
        System.out.print("Password: ");     password = input.nextLine();
        if(password.length()>=8)
            return true;
        return false;
    }

    private void generateApplicant(){
        examination = new Examination(University.entrance,University.maxMarks);
        examination.setRegNo(regNo);
        examination.setObtainedMarks(obtainedMarks);
        examination.setPercentile(percentile);

        applicationForm = new ApplicationForm();
        applicationForm.setName(new Applicant.Name(firstName,lastName));
        applicationForm.setId(Applicant.UniqueId.UIDAI,uniqueId);
        applicationForm.setEmail(email);
        applicationForm.setPhNo(phNo);
        applicationForm.setHsc(hscBoard,hscReg,hscPercentage);
        applicationForm.setExamination(examination);
        applicationForm.setBranchName(branchName);

        applicant = new Applicant(applicationForm);
        applicant.setPassword(password);
    }

    public String register(){

        System.out.print("First Name: ");   firstName = input.nextLine();
        System.out.print("Last Name: ");    lastName = input.nextLine();
        System.out.print("UIDAI No: ");     uniqueId = input.nextLine();
        System.out.print("Email: ");        email = input.nextLine();
        System.out.print("Phone: ");        phNo = input.nextLine();

        while (true){
            if(setPassword())
                break;
            System.out.println("Password must be at least 8 characters long");
        }

        System.out.println("Entrance Details: "+University.entrance);
        System.out.print("Reg No: ");           regNo = input.nextLine();
        System.out.print("Obtained Marks: ");   obtainedMarks = input.nextDouble();
        System.out.print("Percentile: ");       percentile = input.nextDouble();
        input.nextLine();

        System.out.print("HSC Board: ");        hscBoard = input.nextLine();
        System.out.print("HSC Reg: ");          hscReg = input.nextLine();
        System.out.print("HSC Percentage: ");   hscPercentage = input.nextDouble();
        input.nextLine();

        System.out.println("Branches Offered: ");
        for(University.Branch branch:branches)
            System.out.println("\t"+branch);
        while (true){
            if(selectBranch())
                break;
        }


        generateApplicant();
        System.out.println("Status: "+applicant.apply());
        return applicant.getApplicationId();
    }

    public Applicant fetchApplicant(String id,String password){
        return University.fetchApplicant(id,password);
    }

    public boolean fillEnrollmentForm(Applicant applicant){
        EnrollmentForm enrollmentForm = applicant.getEnrollmentForm();
        if(enrollmentForm == null)
            return false;

        System.out.print("Placeholder: ");
        String placeholder = input.nextLine();
        enrollmentForm.setPlaceholder(placeholder);
        if(!enrollmentForm.isValid())
            return false;

        applicant.setStatus(Applicant.Status.UNDER_VERIFICATION);
        applicant.setEnrollmentForm(enrollmentForm);
        return true;
    }
}
