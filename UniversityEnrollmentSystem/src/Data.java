import java.io.*;
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
        loadApplicants();
    }

    protected static void loadApplicants(){
        try(
            FileInputStream file = new FileInputStream("applicants.dat");
            BufferedInputStream buffer = new BufferedInputStream(file);
            ObjectInputStream iStream = new ObjectInputStream(buffer)
        ){
            while(true){
                Applicant applicant = (Applicant) iStream.readObject();
                applicants.put(applicant.getApplicationId(),applicant);
            }
        }catch (EOFException e){
            System.out.println("Loaded Applicants");
        }catch (FileNotFoundException e){
            System.out.println("No Applicants");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    protected static void storeApplicants(){
        try(
            FileOutputStream file = new FileOutputStream("applicants.dat");
            BufferedOutputStream buffer = new BufferedOutputStream(file);
            ObjectOutputStream oStream = new ObjectOutputStream(buffer)
        ){
            for(Applicant applicant : applicants.values())
                oStream.writeObject(applicant);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        for(Applicant applicant:applicants.values()){
            System.out.println("Applicant ID: "+applicant.getApplicationId());
            System.out.println("\tName: "+applicant.getApplicationForm().getName());
            System.out.println("\tEmail: "+applicant.getApplicationForm().getEmail());
            System.out.println("\tPhone: "+applicant.getApplicationForm().getPhNo());
            System.out.println("\tUIDAI"+applicant.getApplicationForm().getUniqueIdNo());
            System.out.println();
        }
    }
}
