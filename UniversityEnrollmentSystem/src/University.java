import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class University {

    protected static Set<Branch> branches;
    public static String entrance;
    public static double maxMarks;
    private static String sql;
    protected static List<BranchQueue> branchQueues;


    static {
        entrance = "JEE";
        maxMarks = 360;
        branches = new HashSet<>();
        branches.add(new Branch("COMP",300,300));
        branches.add(new Branch("ENTC",150,250));
        branches.add(new Branch("CHEM",200,200));
        branches.add(new Branch("CIVIL",200,180));

        branchQueues = new ArrayList<>();
        for(Branch branch:branches){
            branchQueues.add(new BranchQueue(branch.name));
        }

        //syncSeatData();
        for(Branch branch : branches){
            try {
                sql = String.format(
                    "select count(applicant_id) from applicant natural join application_form " +
                    "where status in ('SHORTLISTED','FLOATED','LOCKED','UNDER_VERIFICATION','ENROLLED') " +
                    "and branch_name='%s'",branch.name
                );
                ResultSet resultSet = Database.executeQuery(sql);
                if(resultSet.next())
                    branch.lockedSeats = resultSet.getInt(1);

                sql = String.format(
                    "select count(applicant_id) from applicant natural join application_form " +
                    "where status='ENROLLED' and branch_name='%s'",branch.name
                );
                resultSet = Database.executeQuery(sql);
                if(resultSet.next())
                    branch.allocatedSeats = resultSet.getInt(1);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static boolean isUnique(Applicant newApplicant){
        String unique_id = newApplicant.getApplicationForm().getUniqueIdNo();
        String email = newApplicant.getApplicationForm().getEmail();
        String phone = newApplicant.getApplicationForm().getPhNo();
        String entrance_reg_no = newApplicant.getApplicationForm().getExamination().getRegNo();
        String hsc_reg_no = newApplicant.getApplicationForm().getHsc().getRegNo();

        try{
            sql = String.format(
                "select count(*) from application_form " +
                "where unique_id='%s' " +
                "or email='%s' " +
                "or phone='%s' " +
                "or entrance_reg_no='%s' " +
                "or hsc_reg_no='%s'",
                unique_id,email,phone,entrance_reg_no,hsc_reg_no
            );
            ResultSet resultSet = Database.executeQuery(sql);
            if(resultSet.next())
                return resultSet.getInt(1)==0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static Set<Branch> getBranches(){
        return Collections.unmodifiableSet(branches);
    }

    protected static Branch getBranch(String branchName){
        for(Branch branch:branches)
            if(branch.getName().equals(branchName))
                return branch;
        return null;
    }

    public static void addBranch(Branch branch){
        branches.add(branch);
    }

    public static String generateApplicationId() {
        Date date = Calendar.getInstance().getTime();
        String prefix = new SimpleDateFormat("ddMMyyyy").format(date);
        int id = 1;
        try {
            ResultSet resultSet = Database.executeQuery("select count(applicant_id) from applicant");
            resultSet.next();
            id = resultSet.getInt(1) + 1;
        }catch (SQLException e){
            e.printStackTrace();
        }

        String suffix = String.format("%05d",id);
        return "I"+prefix+suffix;
    }

    public static String generateEnrollmentId(Branch branch){
        Date date = Calendar.getInstance().getTime();
        String prefix = new SimpleDateFormat("yy").format(date);
        int id = 1;
        try {
            sql = String.format(
                "select count(applicant_id) from applicant natural join application_form "+
                "where status='ENROLLED' and branch_name='%s'",branch.name
            );
            ResultSet resultSet = Database.executeQuery(sql);
            resultSet.next();
            id = resultSet.getInt(1) + 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        String suffix = String.format("%03d",id);
        return branch.getName()+"2K"+prefix+suffix;
    }

    public static boolean addApplicant(Applicant applicant){
        if(applicant.getApplicationId()==null)
            return false;
        applicant.setStatus(Applicant.Status.APPLIED);
        Database.addApplicant(applicant);
        return false;
    }

    public static Applicant fetchApplicant(String id,String password){
        Applicant applicant = Database.getApplicantObject(id);
        if(applicant==null)
            return null;
        if(applicant.matchPassword(password))
            return applicant;
        else return null;
    }

    public static Admin accessAdmin(String username, String password){
        try{
            sql = String.format("select * from admin where username='%s' and password='%s'",username,password);
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






    public static class Branch implements Serializable {
        private static final long serialVersionUID = 73L;

        String name;
        int seats;
        int lockedSeats;
        int allocatedSeats;
        int cutOff;

        public Branch(String name,int seats,int cutOff){
            this.name = name;
            this.seats = seats;
            lockedSeats = 0;
            allocatedSeats = 0;
            this.cutOff = cutOff;
        }

        public String getName() {
            return name;
        }public int getCutOff() {
            return cutOff;
        }

        protected void lockSeat(){
            if(seats > lockedSeats)
                lockedSeats++;
        }
        protected void unlockSeat(){
            if(lockedSeats > 0)
                lockedSeats--;
        }
        protected void allocateSeat(){
            if(lockedSeats > allocatedSeats)
                allocatedSeats++;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(this.getClass().equals(obj.getClass())){
                Branch first = this;
                Branch second = (Branch) obj;
                return first.name.equals(second.name);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return name;
        }
    }



    public static class Admin{
        private final String username;
        private final String name;
        private final String password;

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
                    result += resultSet.getString("applicant_id") + "\n";
                    result += "\t" + resultSet.getString("placeholder") + "\n";
                }
                return result;
            }catch (SQLException e){
                e.printStackTrace();
                return "ENROLLMENT FORMS:\n";
            }
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
    }

    public static void main(String[] args) {
        Admin admin = new Admin();
        admin.registerNewAdmin("heptadecane","Sooraj VS","testpass");
    }

}

