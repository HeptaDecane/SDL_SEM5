import java.util.LinkedHashSet;
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


    public Applicant(){
        applicationId = "UNASSIGNED";
        examinations = new LinkedHashSet<>();
        status = Status.PENDING;
        reservation = null;
    }

    public void setName(String firstName,String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void setId(UniqueId id,String uniqueIdNo){
        this.id = id;
        this.uniqueIdNo = uniqueIdNo;
    }
    public void setApplicationId(String applicationId){
        this.applicationId = applicationId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public void addExamination(Examination examination){
        examinations.add(examination);
    }

    public Status accept(){
        if(status == Status.SHORTLISTED)
            status = Status.ACCEPTED;
        return status;
    }

    public Status lock(){
        if(status == Status.SHORTLISTED)
            status = Status.LOCKED;
        return status;
    }

    public Status apply(){
            status = Status.APPLIED;
            // some more logic here
        return status;
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
