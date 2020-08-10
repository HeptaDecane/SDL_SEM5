import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class University extends Data {


    public static boolean isUnique(Applicant newApplicant){
        for(Applicant applicant:applicants.values()){
            if(newApplicant.equals(applicant))
                return false;
        }
        return true;
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

    public static enum Branches{
        BRANCH1,BRANCH2,BRANCH3,BRANCH4,BRANCH5,BRANCH6
    }

    public static class Branch{
        String name;
        int seats;
        int lockedSeats;
        List<CutOff> cutOffs;

        public Branch(String name,int seats){
            this.name = name;
            this.seats = seats;
            lockedSeats = 0;
            cutOffs = new ArrayList<>();
        }
        public void addCutoff(CutOff cutOff){
            cutOffs.add(cutOff);
        }
    }
    public static class CutOff{
        String title;
        int score;
        public CutOff(String title,int score){
            this.title = title;
            this.score = score;
        }
    }
}

