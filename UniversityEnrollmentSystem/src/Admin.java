public class Admin extends University{
    private String username;

    public Admin(String username){
        this.username = username;
    }

    public void shortlistApplicants(){
        for(Applicant applicant:Data.applicants.values()){
            ApplicationForm applicationForm = applicant.getApplicationForm();
            Examination examination = applicationForm.getExamination();
            ApplicationForm.HSC hsc = applicationForm.getHsc();
            String branchName = applicationForm.getBranchName();
            Branch branch = getBranch(branchName);

            if(hsc.getPercentage()<75 || examination.getObtainedMarks()<branch.cutOff){
                applicant.setStatus(Applicant.Status.REJECTED);
                continue;
            }

            for(BranchQueue branchQueue: branchQueues) {
                if (branchQueue.getBranchName().equals(branchName)){
                    branchQueue.add(applicant);
                    break;
                }
            }
        }

        for(BranchQueue branchQueue:branchQueues){
            Branch branch = getBranch(branchQueue.getBranchName());
            while(branchQueue.size() > 0){
                if(branch.lockedSeats >= branch.seats)
                    break;
                Applicant applicant = branchQueue.poll();
                String applicantId = applicant.getApplicationId();

                applicant.setStatus(Applicant.Status.SHORTLISTED);
                shortlisted.put(applicantId,applicant);
                branch.lockSeat();
                applicants.remove(applicantId);
            }
        }
        storeApplicants();
        storeShortlisted();

        for(BranchQueue branchQueue : branchQueues)
            branchQueue.clear();
    }

}
