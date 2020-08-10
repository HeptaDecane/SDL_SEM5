import java.io.Serializable;

public class Examination implements Serializable {
    private String title;
    private String regNo;
    private double maxMarks;
    private double obtainedMarks;
    private double percentile;

    public Examination(String title){
        this.title = title;
    }

// GETTERS
    public String getTitle() {
        return title;
    }
    public String getRegNo() {
        return regNo;
    }
    public double getMaxMarks() {
        return maxMarks;
    }
    public double getObtainedMarks() {
        return obtainedMarks;
    }
    public double getPercentile() {
        return percentile;
    }

// SETTERS
    public void setTitle(String title) {
        this.title = title;
    }
    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
    public void setMaxMarks(double maxMarks) {
        this.maxMarks = maxMarks;
    }
    public void setObtainedMarks(double obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }
    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (this.getClass().equals(obj.getClass())){
            Examination first = this;
            Examination second = (Examination) obj;
            return first.title.equals(second.title);
        }
        return false;
    }

    //equals,hashcodes

}
