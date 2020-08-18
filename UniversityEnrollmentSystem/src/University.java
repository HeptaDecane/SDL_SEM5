import java.io.Serializable;
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

    public static void addBranch(Branch branch){
        branches.add(branch);
    }

    public static String generateApplicationId(Applicant newApplicant){
        Date date = Calendar.getInstance().getTime();
        String prefix = new SimpleDateFormat("ddMMyyyy").format(date);
        String suffix = String.format("%05d",applicants.size()+1);
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

    public static Applicant.Status checkStatus(String id){
        if(applicants.containsKey(id))
            return applicants.get(id).getStatus();
        else
            return Applicant.Status.NOT_FOUND;
    }

    public static Applicant fetchApplicant(String id){
        return applicants.get(id);
    }

    public static class Branch implements Serializable {
        String name;
        int seats;
        int lockedSeats;
        int cutOff;

        public Branch(String name){
            this(name,0,0);
        }
        public Branch(String name,int seats,int cutOff){
            this.name = name;
            this.seats = seats;
            lockedSeats = 0;
            this.cutOff = cutOff;
        }

        public String getName() {
            return name;
        }public int getCutOff() {
            return cutOff;
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
}

