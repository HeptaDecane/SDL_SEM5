import java.io.Serializable;

public class EnrollmentForm extends Form implements Serializable {
    private String placeholder="";

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isValid(){
        return placeholder.equalsIgnoreCase("documents");
    }
}
