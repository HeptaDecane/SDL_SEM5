import java.util.Scanner;
import java.util.Set;

public class StudentPortal {
    Scanner input = new Scanner(System.in);

    private ApplicationForm applicationForm;
    private Applicant applicant;
    protected static String firstName,password,lastName,uniqueId,email,phNo,regNo,branchName,hscBoard,hscReg;
    protected static Double percentile,obtainedMarks,hscPercentage;
    protected static String placeholder;
    private Examination examination;
    protected static Set<University.Branch> branches;

    static {
        branches = University.getBranches();
    }

    protected boolean validBranch(){
        for(University.Branch branch:branches){
            if(branchName.equals(branch.getName()))
                return true;
        }
        return false;
    }
    protected boolean validPassword(){
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
        generateApplicant();
        applicant.apply();
        return applicant.getApplicationId();
    }

    public Applicant fetchApplicant(String id,String password){
        return University.fetchApplicant(id,password);
    }

    protected boolean submitEnrollmentForm(Applicant applicant){
        EnrollmentForm enrollmentForm = applicant.getEnrollmentForm();
        if(enrollmentForm == null)
            return false;

        enrollmentForm.setPlaceholder(placeholder);
        if(!enrollmentForm.isValid())
            return false;

        applicant.setStatus(Applicant.Status.UNDER_VERIFICATION);
        applicant.setEnrollmentForm(enrollmentForm);
        return true;
    }
}
