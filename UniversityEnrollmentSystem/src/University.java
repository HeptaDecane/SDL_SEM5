import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class University extends Data {

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
                    "where (status='SHORTLISTED' or status='ENROLLED') and branch_name='%s'",branch.name
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
        for(Applicant applicant:applicants.values()){
            if(newApplicant.equals(applicant))
                return false;
        }
        return true;
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
        Applicant applicant;
        if(applicants.containsKey(id))
            applicant = applicants.get(id);
        else if(shortlisted.containsKey(id))
            applicant = shortlisted.get(id);
        else if(enrolledContainsKey(id))
            applicant = enrolledGet(id);
        else
            return null;

        if(applicant.matchPassword(password))
            return applicant;
        else return null;
    }

    private static boolean enrolledContainsKey(String id){
        for(Applicant applicant : enrolled)
            if(applicant.getApplicationId().equals(id))
                return true;

        return false;
    }
    private static Applicant enrolledGet(String id){
        for(Applicant applicant : enrolled)
            if(applicant.getApplicationId().equals(id))
                return applicant;

        return null;
    }

    public static Admin accessAdmin(String username, String password){
        if(authenticateAdmin(username,password))
            return new Admin(username);
        else
            return null;
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

        private Admin(String username){
            this.username = username;
        }

        public String getUsername() {
            return username;
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
                return true;

            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }


        public Applicant.Status checkStatus(String id){
            if(applicants.containsKey(id))
                return applicants.get(id).getStatus();
            else if(shortlisted.containsKey(id))
                return shortlisted.get(id).getStatus();
            else if(enrolledContainsKey(id))
                return enrolledGet(id).getStatus();
            else
                return Applicant.Status.NOT_FOUND;
        }

        public String listApplicants(){
            return Data.listApplicants();
        }
        public String listShortlisted(){
            return Data.listShortlisted();
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
            String result = "ENROLLMENT FORMS:\n";
            for(Applicant applicant : shortlisted.values()){
                if(applicant.getStatus() == Applicant.Status.UNDER_VERIFICATION) {
                    result += applicant.getApplicationId() + "\n";
                    result += "\t" + applicant.getEnrollmentForm().getPlaceholder() + "\n";
                }
            }
            return result;
        }

        public void registerNewAdmin(String username, String password){
            try(
                FileOutputStream file = new FileOutputStream("admin.dat",true);
                DataOutputStream oStream = new DataOutputStream(file)
            ){
                oStream.writeUTF(username);
                oStream.writeUTF(password);
            }catch (Exception e){
                System.out.println(e);
            }
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
        Admin admin = new Admin("admin");
//        admin.registerNewAdmin("superuser","testpass");
//        admin.shortlistApplicants();
//        System.out.println(admin.enrollApplicant("I2409202000012"));
//        System.out.println(generateEnrollmentId(getBranch("COMP")));


//        Applicant applicant = Database.getApplicantObject("I2409202000004");
//        if (applicant != null) {
//            System.out.println(applicant.getApplicationForm().getName());
//            System.out.println(applicant.getEnrollmentForm());
//        }
//        else
//            System.out.println(Applicant.Status.NOT_FOUND);

//        admin.issueEnrollmentForms();
        System.out.println(admin.viewStats());
    }

}

