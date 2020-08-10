import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Applicant implements Apply,Enroll,Serializable {
    private String applicationId;
    private ApplicationForm applicationForm;
    private Status status;
    private List<University.Branches> offeredBranches;
    private University.Branches selectedBranch;
    private EnrollmentForm enrollmentForm;
    private String enrollmentId;

    public Applicant(){
        applicationForm = new ApplicationForm();
        status = Status.PENDING;

        offeredBranches = new ArrayList<>();
        enrollmentForm = null;
    }

// GETTER
    public Status getStatus() {
        return status;
    }
    public List<University.Branches> getOfferedBranches() {
        return offeredBranches;
    }
    public ApplicationForm getApplicationForm(){
        return applicationForm;
    }
    public EnrollmentForm getEnrollmentForm() {
        return enrollmentForm;
    }
    public String getApplicationId(){
        return applicationId;
    }

// SETTER
    public void setStatus(Status status){
        this.status = status;
    }

// APPLY INTERFACE
    @Override
    public void fillApplicationForm(ApplicationForm applicationForm) {
        this.applicationForm = applicationForm;
    }

    @Override
    public Status apply(){
        if(!applicationForm.isValid())
            return status;
        if(!University.isUnique(this))
            return status;
        applicationId = University.generateApplicationId(this);
        University.addApplicant(this);
        status = Status.APPLIED;
        return status;
    }

    @Override
    public Status lock(){
        if(status == Status.SHORTLISTED)
            status = Status.LOCKED;
        return status;
    }

// ENROLL INTERFACE
    @Override
    public Status accept(){
        if(status==Status.SHORTLISTED && selectedBranch!=null)
            status = Status.ACCEPTED;

        return status;
    }

    @Override
    public void fillEnrollmentForm(EnrollmentForm enrollmentForm) {
        this.enrollmentForm = enrollmentForm;
    }


    @Override
    public Status enroll(){
        return status;
    }


    public boolean selectBranch(University.Branches branch){
        if(offeredBranches.contains(branch)) {
            selectedBranch = branch;
            return true;
        }
        selectedBranch = null;
        return false;
    }

// METHODS
    @Override
    public String toString() {
        if(applicationId==null)
            return status.toString();
        return applicationId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (this.getClass().equals(obj.getClass())){
            Applicant first = this;
            Applicant second = (Applicant) obj;
            if(first.applicationForm.getUniqueIdNo().equals(second.applicationForm.getUniqueIdNo()))
                return true;
            if(first.applicationForm.getEmail().equals(second.applicationForm.getEmail()))
                return true;
            if(first.applicationForm.getPhNo().equals(second.applicationForm.getPhNo()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return applicationId.hashCode();
    }

    public static enum UniqueId{
        UIDAI,PASSPORT,PAN,DRIVING_LICENSE,VOTER_ID
    }

    public static enum Status{
        PENDING,APPLIED,SHORTLISTED,ACCEPTED,REJECTED,LOCKED,UNDER_VERIFICATION,ENROLLED
    }

    final static class Name implements Serializable{
        private String first;
        private String middle;
        private String last;

        public Name(String first,String last){
            this.first = first;
            this.last = last;
        }
        public Name(String first,String middle,String last){
            this(first,last);
            this.middle = middle;
        }

        public String getFirst() {
            return first;
        }
        public String getMiddle() {
            return middle;
        }
        public String getLast() {
            return last;
        }

        @Override
        public String toString() {
            if (middle == null)
                return first + " " + last;
            else
                return first + " " + middle + " " + last;
        }
    }
}
