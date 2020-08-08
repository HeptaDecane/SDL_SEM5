import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Applicant {
    private String firstName;
    private String lastName;
    private UniqueId id;
    private String uniqueIdNo;
    private String applicationId;
    private Set<Examination> examinations;
    private String email;
    private String phNo;
    private Status status;
    private Reservation reservation;

    private List<University.Branches> offeredBranches;
    private University.Branches selectedBranch;
    private EnrollmentForm enrollmentForm;
    private String enrollmentId;

    public Applicant(){
        applicationId = "UNASSIGNED";
        examinations = new LinkedHashSet<>();
        status = Status.PENDING;
        reservation = null;
        offeredBranches = new ArrayList<>();
        enrollmentForm = null;
    }

    //------------------------------------- GETTERS -------------------------------------//
    public Reservation getReservation() {
        return reservation;
    }
    public Status getStatus() {
        return status;
    }
    public List<University.Branches> getOfferedBranches() {
        return offeredBranches;
    }
    public EnrollmentForm getEnrollmentForm() {
        return enrollmentForm;
    }
    //-----------------------------------------------------------------------------------//

    //------------------------------------- SETTERS -------------------------------------//
    public void setName(String firstName,String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void setId(UniqueId id,String uniqueIdNo){
        this.id = id;
        this.uniqueIdNo = uniqueIdNo;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }
    public void setReservation(Reservation reservation){
        this.reservation = reservation;
    }
    //-----------------------------------------------------------------------------------//

    public void addExamination(Examination examination){
        examinations.add(examination);
    }

    public boolean selectBranch(University.Branches branch){
        if(offeredBranches.contains(branch)) {
            selectedBranch = branch;
            return true;
        }
        selectedBranch = null;
        return false;
    }

    public Status accept(){
        if(status==Status.SHORTLISTED && selectedBranch!=null)
            status = Status.ACCEPTED;
        // Enrollment ID generation
        return status;
    }

    public Status lock(){
        if(status == Status.SHORTLISTED)
            status = Status.LOCKED;
        return status;
    }

    public Status apply(){
            status = Status.APPLIED;
            // Application ID generation
        return status;
    }

    public void printExams(){
        for(Examination examination:examinations){
            System.out.println(examination);
        }
    }

    //-----------------------------------------------------------------------------------//

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }

    public static enum UniqueId{
        UIDAI,PASSPORT,PAN,DRIVING_LICENSE,VOTER_ID
    }

    public static enum Status{
        PENDING,APPLIED,SHORTLISTED,ACCEPTED,REJECTED,LOCKED
    }

    public static enum Reservation{
        OBC,SC,ST,NT,PWD
    }
}
