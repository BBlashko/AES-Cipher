import java.io.*;

public class AES {

    private String key;
    private String inputFile;
    private String outputFile;

    public AES(String key, String inputFile) {
        this.key = key;
        this.inputFile = inputFile;
    }

	public void firstRoundEncryption() {

	}

	public void firstRoundDecryption() {
		
	}

    public void subBytes() {

    }
    
    public void shiftRows() {
        
    }

    public void mixColumns() {
        
    }

    public void addRoundKey() {
        
    }

    public static void main(String[] args)
    {
        System.out.println("Hello AES-Cipher!");
        // Read in args

        if (args.length != 3) {
            System.out.println("Invalid Number of Args: " + args.length);
            System.exit(0);
        }

        if (args[0].charAt(0) == 'e') {
            System.out.println("Encrypting InputFile...");
            //Enc here

        } else if (args[0].charAt(0) == 'd') {
            System.out.println("Decrypting InputFile...");
            if (args[2].substring(args[2].length() - 3) != "enc"){
                System.out.println("Can't decrypt file: File not encrypted");
                System.exit(0);
            }

            // Dec here
        } else {
            System.out.println("Invalid Arguments");
            System.exit(0);
        }



        // for 

    }
}
