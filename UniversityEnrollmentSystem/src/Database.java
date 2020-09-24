import java.sql.*;

public abstract class Database {
    static Connection connection = null;

    static final String username = "31165";
    static final String password = "31165@mysql";
    static final String url = "jdbc:mysql://localhost:3306/UniversityEnrollmentSystem";

    static {
        try {
            connection = DriverManager.getConnection(url,username,password);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private Database(){
        System.out.println("private constructor");
    }

    public static ResultSet executeQuery(String sql){
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static int executeUpdate(String sql){
        try{
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public static boolean execute(String sql){
        try {
            Statement statement = connection.createStatement();
            return statement.execute(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void addApplicant(Applicant applicant){
        String sql;

        String hsc_reg_no = applicant.getApplicationForm().getHsc().getRegNo();
        String board = applicant.getApplicationForm().getHsc().getBoard();
        double percentage = applicant.getApplicationForm().getHsc().getPercentage();
        sql = String.format(
            "insert into hsc(hsc_reg_no, board, percentage) " +
            "values ('%s','%s',%f)",hsc_reg_no,board,percentage
        );
        executeUpdate(sql);

        String entrance_reg_no = applicant.getApplicationForm().getExamination().getRegNo();
        double percentile = applicant.getApplicationForm().getExamination().getPercentile();
        double score = applicant.getApplicationForm().getExamination().getObtainedMarks();
        sql = String.format(
           "insert into entrance(entrance_reg_no, percentile, score) " +
           "values ('%s',%f,%f)",entrance_reg_no,percentile,score
        );
        executeUpdate(sql);

        String unique_id = applicant.getApplicationForm().getUniqueIdNo();
        String first_name = applicant.getApplicationForm().getName().getFirst();
        String middle_name = applicant.getApplicationForm().getName().getMiddle();
        String last_name = applicant.getApplicationForm().getName().getLast();
        String email = applicant.getApplicationForm().getEmail();
        String phone = applicant.getApplicationForm().getPhNo();
        String branch_name = applicant.getApplicationForm().getBranchName();
        sql = String.format(
            "insert into application_form (unique_id, first_name, middle_name, last_name, email, phone, entrance_reg_no, hsc_reg_no, branch_name) " +
            "values ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",unique_id,first_name,middle_name,last_name,email,phone,entrance_reg_no,hsc_reg_no,branch_name
        );
        executeUpdate(sql);

        String applicant_id = applicant.getApplicationId();
        String password = applicant.getPassword();
        Applicant.Status status = applicant.getStatus();
        sql = String.format(
            "insert into applicant (applicant_id, password, unique_id, status) " +
            "values ('%s','%s','%s','%s')",applicant_id,password,unique_id,status
        );
        executeUpdate(sql);

    }

    public static void main(String[] args) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select version()");
            System.out.println("Database connected");
            while(resultSet.next())
                System.out.println("MySQL: "+resultSet.getString(1));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
