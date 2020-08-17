public interface Apply {
    Applicant.Status lock();
    Applicant.Status apply();
    void setApplicationForm(ApplicationForm applicationForm);
}
