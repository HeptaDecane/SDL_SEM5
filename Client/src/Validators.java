public abstract class Validators {
    public static boolean isAlpha(String string){
        if (string.isBlank()) return false;
        return string.matches("^[a-zA-Z]*$");
    }
    public static boolean isAlpha(String string,int min, int max){
        String regex = String.format("[a-zA-Z]{%d,%d}",min,max);
        return string.matches(regex);
    }
    public static boolean isNum(String string){
        if (string.isBlank()) return false;
        return string.matches("^[0-9]*$");
    }
    public static boolean isNum(String string,int min, int max){
        String regex = String.format("[0-9]{%d,%d}",min,max);
        return string.matches(regex);
    }
    public static boolean isAlNum(String string){
        if (string.isBlank()) return false;
        return string.matches("^[a-zA-Z0-9]*$");
    }
    public static boolean isAlNum(String string,int min, int max){
        String regex = String.format("[a-zA-Z0-9]{%d,%d}",min,max);
        return string.matches(regex);
    }
    public static boolean isEmail(String string){
        return string.matches("^(.+)@(.+)$");
    }
    public static boolean isValidPassword(String password1, String password2, int min){
        return password1.equals(password2) && password1.length()>=min;
    }
    public static boolean isFloat(String string){
        return string.matches("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)");
    }
    public static boolean isFloat(String string, double min, double max){
        if(!string.matches("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)"))
            return false;
        double num = Double.parseDouble(string);
        return num>=min && num<=max;
    }
}
