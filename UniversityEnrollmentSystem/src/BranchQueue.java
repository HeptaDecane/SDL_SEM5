import java.util.PriorityQueue;

public class BranchQueue{
    private String branch;
    private PriorityQueue<Applicant> queue;

    public BranchQueue(String branch){
        this.branch = branch;
        queue = new PriorityQueue<>();
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
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
