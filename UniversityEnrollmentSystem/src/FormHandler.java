import java.io.*;

public class FormHandler <E extends Form> {
    private E form;
    private String applicantId;
    private String enrollmentId;

    public FormHandler(String applicantId,String enrollmentId){
        this(null,applicantId,enrollmentId);
    }
    public FormHandler(E form,String applicantId,String enrollmentId){
        this.form = form;
        this.applicantId = applicantId;
        this.enrollmentId = enrollmentId;
    }

    public void setForm(E form) {
        this.form = form;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }
    public String getApplicantId() {
        return applicantId;
    }
    public E getForm() {
        return form;
    }

    public void saveForm(){
        try(
            FileOutputStream file = new FileOutputStream("forms.dat",true);
            BufferedOutputStream buffer = new BufferedOutputStream(file);
            ObjectOutputStream oStream = new ObjectOutputStream(buffer)
        ){
            oStream.writeUTF(enrollmentId);
            oStream.writeUTF(applicantId);
            oStream.writeObject(form);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static Form fetchForm(String id){
        try(
            FileInputStream file = new FileInputStream("forms.dat");
            BufferedInputStream buffer = new BufferedInputStream(file);
            ObjectInputStream iStream = new ObjectInputStream(buffer)
        ){
            while (true){
                String applicantId = iStream.readUTF();
                String enrollmentId = iStream.readUTF();
                Form form = (Form) iStream.readObject();

                if(applicantId.equals(id) || enrollmentId.equals(id))
                    return form;
            }
        }catch (EOFException e){
            return null;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
