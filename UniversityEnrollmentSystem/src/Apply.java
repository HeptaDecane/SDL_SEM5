public interface Apply {
    Applicant.Status lock();
    Applicant.Status apply();
    void fillApplicationForm(ApplicationForm applicationForm);
}
