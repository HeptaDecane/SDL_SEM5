import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class University extends Data {

    protected static Set<Branch> branches;
    public static String entrance;
    public static double maxMarks;
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
        for(Applicant applicant : shortlisted.values()){
            ApplicationForm applicationForm = applicant.getApplicationForm();
            Branch branch = getBranch(applicationForm.getBranchName());
            branch.lockedSeats++;
        }
        for(Applicant applicant : enrolled){
            ApplicationForm applicationForm = applicant.getApplicationForm();
            Branch branch = getBranch(applicationForm.getBranchName());
            branch.lockedSeats++;
            branch.allocatedSeats++;
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

    public static String generateApplicationId(){
        Date date = Calendar.getInstance().getTime();
        String prefix = new SimpleDateFormat("ddMMyyyy").format(date);
        String suffix = String.format("%05d",applicants.size()+shortlisted.size()+1);
        return "I"+prefix+suffix;
    }

    public static String generateEnrollmentId(Branch branch){
        Date date = Calendar.getInstance().getTime();
        String prefix = new SimpleDateFormat("yy").format(date);
        String suffix = String.format("%03d",branch.allocatedSeats+1);
        return branch.getName()+"2K"+prefix+suffix;
    }

    public static boolean addApplicant(Applicant applicant){
        if(applicant.getApplicationId()==null)
            return false;
        String key = applicant.getApplicationId();
        applicant.setStatus(Applicant.Status.APPLIED);
        applicants.put(key,applicant);
        storeApplicants();
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
            for(Applicant applicant:Data.applicants.values()){
                ApplicationForm applicationForm = applicant.getApplicationForm();
                Examination examination = applicationForm.getExamination();
                ApplicationForm.HSC hsc = applicationForm.getHsc();
                String branchName = applicationForm.getBranchName();
                Branch branch = getBranch(branchName);

                if(hsc.getPercentage()<75 || examination.getObtainedMarks()<branch.cutOff){
                    applicant.setStatus(Applicant.Status.REJECTED);
                    continue;
                }

                for(BranchQueue branchQueue: branchQueues) {
                    if (branchQueue.getBranchName().equals(branchName)){
                        branchQueue.add(applicant);
                        break;
                    }
                }
            }

            for(BranchQueue branchQueue:branchQueues){
                Branch branch = getBranch(branchQueue.getBranchName());
                while(branchQueue.size() > 0){
                    if(branch.lockedSeats >= branch.seats)
                        break;
                    Applicant applicant = branchQueue.poll();
                    String applicantId = applicant.getApplicationId();

                    applicant.setStatus(Applicant.Status.SHORTLISTED);
                    shortlisted.put(applicantId,applicant);
                    branch.lockSeat();
                    applicants.remove(applicantId);
                }
            }
            storeApplicants();
            storeShortlisted();

            for(BranchQueue branchQueue : branchQueues)
                branchQueue.clear();
        }

        public boolean enrollApplicant(String id){
            Applicant applicant = shortlisted.get(id);
            if(applicant == null)
                return false;
            if(applicant.getStatus() != Applicant.Status.UNDER_VERIFICATION)
                return false;

            ApplicationForm applicationForm = applicant.getApplicationForm();
            String branchName = applicationForm.getBranchName();
            Branch branch = getBranch(branchName);

            String enrollmentID = generateEnrollmentId(branch);
            applicant.setEnrollmentId(enrollmentID);

            shortlisted.remove(id);
            enrolled.add(applicant);
            applicant.setStatus(Applicant.Status.ENROLLED);
            applicant.setEnrollmentForm(null);
            branch.allocateSeat();

            storeShortlisted();
            storeEnrolled();
            return true;
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

        public String viewSeatStatus(){
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
            for(Applicant applicant : shortlisted.values()){
                if(applicant.getStatus() == Applicant.Status.LOCKED)
                    applicant.setEnrollmentForm(new EnrollmentForm());
            }
        }

        @Override
        public String toString() {
            return username;
        }
    }

}

