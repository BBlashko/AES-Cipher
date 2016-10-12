/**
 * Created by lynx on 07/10/16.
 */
public class ErrorLog {

    public static void printIncorrectArgs()
    {
        System.err.println("[ERROR] Incorrect command arguments");
        System.out.println("Usage: AES <e/d> <keyFile> <inputFile>");
        System.exit(1);
    }

    public static void printError(String msg, Exception e)
    {
        System.err.println("[ERROR] " + msg);
        System.err.println(e);
        System.exit(1);
    }
}
