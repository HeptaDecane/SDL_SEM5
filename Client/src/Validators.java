public abstract class Validators {
    public static boolean isAlpha(String string){
        if (string.isBlank()) return false;
        return string.matches("^[a-zA-Z]*$");
    }
    public static boolean isAlpha(String string,int min, int max){
        String regex = String.format("[a-zA-Z]{%d,%d}",min,max);
        return string.matches(regex);
    }
    public static boolean isAlNum(String string){
        return string.matches("^[a-zA-Z0-9]*$");
    }
    public static boolean isAlNum(String string,int min, int max){
        String regex = String.format("[a-zA-Z0-9]{%d,%d}",min,max);
        return string.matches(regex);
    }
}
