import java.text.SimpleDateFormat;
import java.util.*;

public class University extends Data {

    private Set<Branch> branches;
    private String name;
    public static String entrance = "JEE";
    public static double maxMarks = 360;

    public University(String name){
        this.name = name;
        branches = new HashSet<>();
    }

    public static boolean isUnique(Applicant newApplicant){
        for(Applicant applicant:applicants.values()){
            if(newApplicant.equals(applicant))
                return false;
        }
        return true;
    }

    public Set<Branch> getBranches(){
        return Collections.unmodifiableSet(branches);
    }

    public void addBranch(Branch branch){
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
        applicants.put(key,applicant);
        storeApplicants();
        return false;
    }

    public static class Branch{
        String name;
        String code;
        int seats;
        int lockedSeats;
        int cutOff;

        public Branch(String code,String name,int seats,int cutOff){
            this.code = code;
            this.name = name;
            this.seats = seats;
            lockedSeats = 0;
            this.cutOff = cutOff;
        }

        public String getName() {
            return name;
        }public String getCode() {
            return code;
        }public int getCutOff() {
            return cutOff;
        }
    }
}

