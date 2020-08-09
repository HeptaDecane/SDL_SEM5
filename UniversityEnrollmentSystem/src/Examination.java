public class Examination {
    private String title;
    private String regNo;
    private String rollNo;
    private float maxMarks;
    private float obtainedMarks;
    private float percentile;

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
    public String getRollNo() {
        return rollNo;
    }
    public float getMaxMarks() {
        return maxMarks;
    }
    public float getObtainedMarks() {
        return obtainedMarks;
    }
    public float getPercentile() {
        return percentile;
    }

// SETTERS
    public void setTitle(String title) {
        this.title = title;
    }
    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
    public void setMaxMarks(float maxMarks) {
        this.maxMarks = maxMarks;
    }
    public void setObtainedMarks(float obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }
    public void setPercentile(float percentile) {
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
