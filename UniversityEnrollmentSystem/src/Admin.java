public class Admin extends University{
    private String username;

    public Admin(String username){
        this.username = username;
    }

    public void startEnrollment(){
        for(Applicant applicant:Data.applicants.values()){
            String branchName = applicant.getApplicationForm().getBranch();
            BranchQueue branchQueue = null;
            Branch branch = null;

            for(BranchQueue i: branchQueues)
                if(i.getBranch().equals(branchName))
                    branchQueue = i;

            branchQueue.add(applicant);
        }
        for(BranchQueue branchQueue:branchQueues){
            while(true) {
                Applicant applicant = branchQueue.poll();
                if(applicant == null)   break;
            }
        }
    }

}
