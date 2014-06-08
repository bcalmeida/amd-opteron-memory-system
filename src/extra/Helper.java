package extra;

public class Helper {

    public static boolean LOGGING = false;

    public static void errorLog(Object caller, String error) {
        String className = caller.getClass().getSimpleName();
        System.out.println("ERROR at " + className + ": " + error);
    }

    public static void log(Object caller, String text) {
        if(LOGGING) {
            String className = caller.getClass().getSimpleName();
            System.out.println("LOG at " + className + ": " + text);
        }
    }
}
