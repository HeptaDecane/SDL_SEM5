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
        loadShortlisted();
        loadEnrolled();
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
            System.out.println("Applicants: Empty");
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

    protected static void loadShortlisted(){
        try(
            FileInputStream file = new FileInputStream("shortlisted.dat");
            BufferedInputStream buffer = new BufferedInputStream(file);
            ObjectInputStream iStream = new ObjectInputStream(buffer)
        ){
            while(true){
                Applicant applicant = (Applicant) iStream.readObject();
                shortlisted.put(applicant.getApplicationId(),applicant);
            }
        }catch (EOFException e){
            System.out.println("Loaded Shortlisted Applicants");
        }catch (FileNotFoundException e){
            System.out.println("Shortlisted Applicants: Empty");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    protected static void storeShortlisted(){
        try(
            FileOutputStream file = new FileOutputStream("shortlisted.dat");
            BufferedOutputStream buffer = new BufferedOutputStream(file);
            ObjectOutputStream oStream = new ObjectOutputStream(buffer)
        ){
            for(Applicant applicant : shortlisted.values())
                oStream.writeObject(applicant);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    protected static void loadEnrolled(){
        try(
                FileInputStream file = new FileInputStream("enrolled.dat");
                BufferedInputStream buffer = new BufferedInputStream(file);
                ObjectInputStream iStream = new ObjectInputStream(buffer)
        ){
            while(true){
                Applicant applicant = (Applicant) iStream.readObject();
                enrolled.add(applicant);
            }
        }catch (EOFException e){
            System.out.println("Loaded Enrolled Applicants");
        }catch (FileNotFoundException e){
            System.out.println("Enrolled Applicants: Empty");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    protected static void storeEnrolled(){
        try(
                FileOutputStream file = new FileOutputStream("enrolled.dat");
                BufferedOutputStream buffer = new BufferedOutputStream(file);
                ObjectOutputStream oStream = new ObjectOutputStream(buffer)
        ){
            for(Applicant applicant : enrolled)
                oStream.writeObject(applicant);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    protected static boolean authenticateAdmin(String username, String password){
        try(
            FileInputStream file = new FileInputStream("admin.dat");
            BufferedInputStream buffer = new BufferedInputStream(file);
            DataInputStream iStream = new DataInputStream(buffer)
        ){
            while (true){
                if(iStream.readUTF().equals(username)){
                    return iStream.readUTF().equals(password);
                }
                iStream.readUTF();
            }
        }catch (EOFException e){
            return false;
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    public static String listApplicants() {
        String result = "\nAPPLICANTS:";
        for(Applicant applicant:applicants.values()){
            result += "\nApplicant ID: "+applicant.getApplicationId();
            result += "\n\tName: "+applicant.getApplicationForm().getName();
            result += "\n\tEmail: "+applicant.getApplicationForm().getEmail();
            result += "\n\tPhone: "+applicant.getApplicationForm().getPhNo();
            result += "\n\tUIDAI: "+applicant.getApplicationForm().getUniqueIdNo();
            result += "\n\tOpted for: "+applicant.getApplicationForm().getBranchName();
            result += "\n";
        }
        return result;
    }
    public static String listShortlisted() {
        String result = "\nSHORTLISTED:";
        for(Applicant applicant:shortlisted.values()){
            result += "\nApplicant ID: "+applicant.getApplicationId();
            result += "\n\tName: "+applicant.getApplicationForm().getName();
            result += "\n\tEmail: "+applicant.getApplicationForm().getEmail();
            result += "\n\tPhone: "+applicant.getApplicationForm().getPhNo();
            result += "\n\tUIDAI: "+applicant.getApplicationForm().getUniqueIdNo();
            result += "\n\tOpted for: "+applicant.getApplicationForm().getBranchName();
            result += "\n";
        }
        return result;
    }




}
