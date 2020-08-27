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
        branches.add(new Branch("CS",300,300));
        branches.add(new Branch("ENTC",150,250));
        branches.add(new Branch("CHEM",200,200));
        branches.add(new Branch("CIVIL",200,180));

        branchQueues = new ArrayList<>();
        for(Branch branch:branches){
            branchQueues.add(new BranchQueue(branch.name));
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

    public static String generateApplicationId(Applicant newApplicant){
        Date date = Calendar.getInstance().getTime();
        String prefix = new SimpleDateFormat("ddMMyyyy").format(date);
        String suffix = String.format("%05d",applicants.size()+shortlisted.size()+1);
        return "I"+prefix+suffix;
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
            applicant = applicants.get(id);
        else
            return null;

        if(applicant.matchPassword(password))
            return applicant;
        else return null;
    }

    public static Admin accessAdmin(String username, String password){
        if(authenticateAdmin(username,password))
            return new Admin(username);
        else
            return null;
    }






    public static class Branch implements Serializable {
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

        public Applicant.Status checkStatus(String id){
            if(applicants.containsKey(id))
                return applicants.get(id).getStatus();
            else if(shortlisted.containsKey(id))
                return shortlisted.get(id).getStatus();
            else
                return Applicant.Status.NOT_FOUND;
        }

        public void listApplicants(){
            Data.listApplicants();
        }
        public void listShortlisted(){
            Data.listShortlisted();
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

        @Override
        public String toString() {
            return username;
        }
    }
}

