import java.util.HashSet;
import java.util.Set;

public class ApplicationForm {
    private Applicant.Name name;
    private Applicant.UniqueId id;
    private String uniqueIdNo;
    private HSC hsc;
    private Set<Examination> examinations;
    private String email;
    private String phNo;

    public ApplicationForm(){
        examinations = new HashSet<>();
    }

// SETTERS
    public void setName(Applicant.Name name){
        this.name = name;
    }
    public void setId(Applicant.UniqueId id,String uniqueIdNo){
        this.id = id;
        this.uniqueIdNo = uniqueIdNo;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setHsc(HSC hsc){
        this.hsc = hsc;
    }
    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }


// GETTERS
    public Applicant.Name getName() {
        return name;
    }
    public Applicant.UniqueId getId() {
        return id;
    }
    public String getUniqueIdNo() {
        return uniqueIdNo;
    }
    public Set<Examination> getExaminations() {
        return examinations;
    }
    public String getEmail() {
        return email;
    }
    public String getPhNo() {
        return phNo;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    public void addExamination(Examination examination){
        examinations.add(examination);
    }

    public boolean isValid(){
        if(name == null) return false;
        if(id == null) return false;
        if(uniqueIdNo == null) return false;
        if(examinations.size() == 0) return false;
        if(email == null) return false;
        if(phNo == null) return false;
        if(hsc == null)return false;
        return true;
    }

    final static class HSC extends Examination{
        String board;
        float percentage;

        public HSC(String board,String rollNo,float percentage){
            super("HSC");
            super.setRollNo(rollNo);
            this.board = board;
            this.percentage = percentage;
        }
    }

}
