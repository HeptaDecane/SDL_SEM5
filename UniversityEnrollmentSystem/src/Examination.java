public class Examination {
    private String title;
    private String regNo;
    private String rollNo;
    private float maxMarks;
    private float obtainedMarks;
    private float percentile;

    public Examination(String title,String regNo){
        this.title = title;
        this.regNo = regNo;
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
}
