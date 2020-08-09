import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Data {
    protected static Map<String,Applicant> applicants;
    protected static Map<String,Applicant> shortlisted;
    protected static List<Applicant> enrolled;

    static {
        applicants = new HashMap<>();
        shortlisted = new HashMap<>();
        enrolled = new LinkedList<>();
    }

    protected static void storeApplicant(){
        System.out.println("working");
    }

}
