public class Main {
    public static void main(String[] args) {
        Applicant test = new Applicant();
        test.setName("john","doe");
        System.out.println(test);

        Examination e1 = new Examination("foo","1234");
        Examination e2 = new Examination("bar","456");
        test.addExamination(e1);
        test.addExamination(e2);
        test.printExams();
        System.out.println(test.getStatus());


    }
}
