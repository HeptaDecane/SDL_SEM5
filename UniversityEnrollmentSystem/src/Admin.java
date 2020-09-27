import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends University{
    private final String username;
    private final String name;
    private final String password;
    private String sql;

    private Admin(){
        this.username = null;
        this.name = null;
        this.password = null;
    }

    private Admin(String username,String name, String password){
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public static Admin accessAdmin(String username, String password){
        try{
            String sql = String.format("select * from admin where username='%s' and password='%s'",username,password);
            ResultSet resultSet = Database.executeQuery(sql);
            if(resultSet.next()){
                String name = resultSet.getString("name");
                Admin admin = new Admin(username,name,password);
                return admin;
            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void shortlistApplicants(){
        try {
            ResultSet resultSet = Database.executeQuery(
            "select applicant_id,score,percentage,branch_name from " +
                "applicant natural join application_form natural join entrance natural join hsc " +
                "where status = 'APPLIED' order by branch_name, score desc"
            );
            while (resultSet.next()){
                String applicantId = resultSet.getString("applicant_id");
                double score = resultSet.getDouble("score");
                double percentage = resultSet.getDouble("percentage");
                String branchName = resultSet.getString("branch_name");

                Branch branch = getBranch(branchName);
                if(percentage<75 || score<branch.cutOff) {
                    sql = String.format(
                            "update applicant set status='REJECTED' "+
                                    "where applicant_id='%s'",applicantId
                    );
                    Database.executeUpdate(sql);
                }
                else if(branch.lockedSeats < branch.seats) {
                    sql = String.format(
                            "update applicant set status='SHORTLISTED' "+
                                    "where applicant_id='%s'",applicantId
                    );
                    Database.executeUpdate(sql);
                    branch.lockSeat();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean enrollApplicant(String id){
        try {
            sql = String.format(
                    "select status,branch_name from applicant natural join application_form " +
                            "where applicant_id='%s'",id
            );
            ResultSet resultSet = Database.executeQuery(sql);

            if(!resultSet.next())
                return false;

            Applicant.Status status = Applicant.Status.valueOf(resultSet.getString("status"));
            String branchName = resultSet.getString("branch_name");
            Branch branch = getBranch(branchName);
            if(status != Applicant.Status.UNDER_VERIFICATION)
                return false;

            if(branch.lockedSeats <= branch.allocatedSeats)
                return false;

            String enrollmentID = generateEnrollmentId(branch);
            sql = String.format(
                    "update applicant set status='ENROLLED', enrollment_id='%s' " +
                            "where applicant_id='%s'",enrollmentID,id
            );
            Database.executeUpdate(sql);
            branch.allocateSeat();
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    public Applicant.Status checkStatus(String id){
        try{
            sql = String.format(
                "select status from applicant "+
                "where applicant_id='%s'",id
            );
            ResultSet resultSet = Database.executeQuery(sql);
            if(resultSet.next())
                return Applicant.Status.valueOf(resultSet.getString("status"));
            return Applicant.Status.NOT_FOUND;
        }catch (SQLException e){
            e.printStackTrace();
            return Applicant.Status.NOT_FOUND;
        }
    }

    public String listApplicants(){
        try {
            ResultSet resultSet = Database.executeQuery(
                    "select applicant_id from applicant where status='APPLIED' or status='REJECTED'"
            );
            String result = "\nAPPLICANTS:";
            Applicant applicant;
            while (resultSet.next()){
                String applicant_id = resultSet.getString("applicant_id");
                applicant = Database.getApplicantObject(applicant_id);
                result += "\nApplicant ID: "+applicant.getApplicationId();
                result += "\n\tName: "+applicant.getApplicationForm().getName();
                result += "\n\tEmail: "+applicant.getApplicationForm().getEmail();
                result += "\n\tPhone: "+applicant.getApplicationForm().getPhNo();
                result += "\n\tUIDAI: "+applicant.getApplicationForm().getUniqueIdNo();
                result += "\n\tOpted for: "+applicant.getApplicationForm().getBranchName();
                result += "\n";
            }
            return result;
        }catch (SQLException e){
            e.printStackTrace();
            return "\nAPPLICANTS:";
        }

    }
    public String listShortlisted(){
        try {
            ResultSet resultSet = Database.executeQuery(
            "select applicant_id from applicant where status='SHORTLISTED'"
            );
            String result = "\nSHORTLISTED:";
            Applicant applicant;
            while (resultSet.next()){
                String applicant_id = resultSet.getString("applicant_id");
                applicant = Database.getApplicantObject(applicant_id);
                result += "\nApplicant ID: "+applicant.getApplicationId();
                result += "\n\tName: "+applicant.getApplicationForm().getName();
                result += "\n\tEmail: "+applicant.getApplicationForm().getEmail();
                result += "\n\tPhone: "+applicant.getApplicationForm().getPhNo();
                result += "\n\tUIDAI: "+applicant.getApplicationForm().getUniqueIdNo();
                result += "\n\tOpted for: "+applicant.getApplicationForm().getBranchName();
                result += "\n";
            }
            return result;
        }catch (SQLException e){
            e.printStackTrace();
            return "\nSHORTLISTED:";
        }
    }

    public String viewStats(){
        String result = "STATS:\n";
        for(Branch branch : branches){
            result += branch.getName();
            result += "\tTotal: "+branch.seats;
            result += "\tLocked: "+branch.lockedSeats;
            result += "\tAllocated: "+branch.allocatedSeats+"\n";
        }
        return result;
    }

    public String viewEnrollmentForms(){
        try{
            String result = "ENROLLMENT FORMS:\n";
            ResultSet resultSet = Database.executeQuery("select * from enrollment_form");
            while (resultSet.next()){
                result += "\n    Applicant ID: " + resultSet.getString("applicant_id") + "\n";
                result += "\tForm: " + resultSet.getString("form") + "\n";
                result += "\tHSC Mark Sheet: " + resultSet.getString("hsc_mark_sheet") + "\n";
                result += "\tEntrance Mark Sheet: " + resultSet.getString("entrance_mark_sheet") + "\n";
            }
            return result;
        }catch (SQLException e){
            e.printStackTrace();
            return "ENROLLMENT FORMS:\n";
        }
    }

    public String listQueries(){
        String result = "";
        try{
            ResultSet resultSet = Database.executeQuery(
                "select * from connection where username is null " +
                    "and (select count(*) from conversation where conversation.ticket_no=connection.ticket_no)>0 "+
                    "and resolved=false"
            );
            result += "New Tickets:\n";
            while (resultSet.next())
                result += "\t"+resultSet.getString("ticket_no")+"\n";

            sql = String.format("select * from connection where username='%s' " +
                "and resolved=false",username
            );
            resultSet = Database.executeQuery(sql);
            result += "\nActive Tickets:\n";
            while (resultSet.next())
                result += "\t"+resultSet.getString("ticket_no")+"\n";
            return result;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void registerNewAdmin(String username,String name,String password){
        sql = String.format(
            "insert into admin (username, name, password) " +
            "values ('%s','%s','%s')",username,name,password
        );
        Database.executeUpdate(sql);
    }

    public void issueEnrollmentForms(){
        Database.execute("call issue_enrollment_forms()");
    }

    @Override
    public String toString() {
        return username;
    }

    public static void main(String[] args) {
        Admin admin = new Admin();
        admin.registerNewAdmin("heptadecane","Sooraj VS","testpass");
    }
}
