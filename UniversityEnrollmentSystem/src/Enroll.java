public interface Enroll {
    Applicant.Status lock();
    Applicant.Status enroll();
    void fillEnrollmentForm(EnrollmentForm enrollmentForm);
}
