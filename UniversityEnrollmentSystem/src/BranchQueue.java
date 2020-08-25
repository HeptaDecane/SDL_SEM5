import java.util.PriorityQueue;

public class BranchQueue{
    private String branchName;
    private PriorityQueue<Applicant> queue;

    public BranchQueue(String branchName){
        this.branchName = branchName;
        queue = new PriorityQueue<>();
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public boolean add(Applicant applicant) { return queue.add(applicant); }
    public Applicant peek() { return queue.peek(); }
    public Applicant poll() { return queue.poll(); }
    public int size() { return queue.size(); }
    public boolean contains(Applicant applicant) { return queue.contains(applicant); }
    public boolean remove(Applicant applicant) { return queue.remove(applicant); }
    public boolean offer(Applicant applicant) { return queue.offer(applicant); }
    public void clear() { queue.clear(); }
}
