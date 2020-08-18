import java.io.Serializable;

public class Applicant implements Apply,Enroll,Comparable<Applicant>,Serializable {
    private String applicationId;
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

// SETTER
    public void setStatus(Status status){
        this.status = status;
    }

// APPLY INTERFACE
    public void setApplicationForm(ApplicationForm applicationForm) {
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
        if(status==Status.SHORTLISTED)
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
        PENDING,APPLIED,SHORTLISTED,ACCEPTED,REJECTED,LOCKED,UNDER_VERIFICATION,ENROLLED,NOT_FOUND
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
