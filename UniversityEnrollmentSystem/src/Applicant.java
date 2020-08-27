import java.io.Serializable;

public class Applicant implements Comparable<Applicant>,Serializable {
    private String applicationId;
    private String password;
    private ApplicationForm applicationForm;

    private EnrollmentForm enrollmentForm;
    private String enrollmentId;

    private Status status;

    public Applicant(){
        applicationForm = new ApplicationForm();
        status = Status.PENDING;
        enrollmentForm = null;
    }
    public Applicant(ApplicationForm applicationForm){
        this.applicationForm = applicationForm;
        status = Status.PENDING;
        enrollmentForm = null;
    }

// GETTER
    public Status getStatus() {
        return status;
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

    public String getEnrollmentId() {
        return enrollmentId;
    }

    //    public String getPassword() {
//        return password;
//    }

    // SETTER
    public void setStatus(Status status){
        this.status = status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    // APPLY INTERFACE
    public void setApplicationForm(ApplicationForm applicationForm) {
        this.applicationForm = applicationForm;
    }

    public void setEnrollmentForm(EnrollmentForm enrollmentForm) {
        this.enrollmentForm = enrollmentForm;
        Data.storeShortlisted();
    }


    public Status apply(){
        if(!applicationForm.isValid())
            return Status.FAILED;
        if(!University.isUnique(this))
            return Status.FAILED;
        applicationId = University.generateApplicationId();
        University.addApplicant(this);
        status = Status.APPLIED;
        return status;
    }


    public Status hover(){
        if(status == Status.SHORTLISTED)
            status = Status.FLOATED;

        Data.storeShortlisted();
        return status;
    }

    public Status lock(){
        if(status==Status.SHORTLISTED || status==Status.FLOATED)
            status = Status.LOCKED;

        Data.storeShortlisted();
        return status;
    }




    public boolean matchPassword(String password){
        return this.password.equals(password);
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

    @Override
    public int compareTo(Applicant that) {
        Examination exam1 = this.applicationForm.getExamination();
        Examination exam2 = that.applicationForm.getExamination();

        if(exam1.getPercentile()>exam2.getPercentile())
            return -1;
        else if(exam1.getPercentile()<exam2.getPercentile())
            return 1;
        else
            if(exam1.getObtainedMarks()>exam2.getObtainedMarks())
                return -1;
            else if(exam1.getObtainedMarks()<exam2.getObtainedMarks())
                return 1;
            else return (this.applicationId.compareTo(that.applicationId));
    }

    public static enum UniqueId{
        UIDAI,PASSPORT,PAN,DRIVING_LICENSE,VOTER_ID
    }

    public static enum Status{
        PENDING,APPLIED,SHORTLISTED,LOCKED,REJECTED,FLOATED,UNDER_VERIFICATION,ENROLLED,NOT_FOUND,FAILED
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
