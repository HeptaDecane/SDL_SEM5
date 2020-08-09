public interface Enroll {
    Applicant.Status accept();
    Applicant.Status enroll();
    void fillEnrollmentForm(EnrollmentForm enrollmentForm);
}
