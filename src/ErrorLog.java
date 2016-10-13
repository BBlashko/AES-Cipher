/**
 * Created by lynx on 07/10/16.
 */
public class ErrorLog {

    public static void printIncorrectArgs() {
        System.err.println("[ERROR] Incorrect command arguments");
        System.err.println("Usage: AES <e/d> <keyFile> <inputFile>");
        System.exit(1);
    }

    public static void printInvalidCharacters() {
        System.err.println("[ERROR] Invalid input characters. \n" +
                "Please ensure each line of your input file and your key file only contains hexadecimal characters");
        System.exit(1);
    }

    public static void printIncorrectInputLineLength() {
        System.err.println("[ERROR] At least 1 line in your input file has incorrect length. \n" +
                "Please ensure each line has exactly 32 hexadecimal characters.");
        System.exit(1);
    }

    public static void printIncorrectKeyLength() {
        System.err.println("[ERROR] Key has incorrect length. \n" +
                "The key must contain exactly 64 hexadecimal characters.");
        System.exit(1);
    }

    public static void printError(String msg, Exception e) {
        System.err.println("[ERROR] " + msg);
        System.err.println(e);
        System.exit(1);
    }
}
