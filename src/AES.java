import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AES {

    private byte[] key;
    private byte[] inputFile;
    private String outputFile;

    private AESConfig aesConfig;
    private Cipher cipher;

    public AES(String[] args) throws IOException
    {

        aesConfig = new AESConfig(args);
        key = Files.readAllBytes(new File("./" + aesConfig.getKeyFilename()).toPath());
        inputFile = Files.readAllBytes(new File("./" + aesConfig.getInputFilename()).toPath());
        cipher = new Cipher(key, inputFile);
    }

    private void outputFile()
    {

    }

    public static void main(String[] args)
    {
        //Initialize AES
        try {
            AES aes = new AES(args);
        } catch (IOException e) {
            System.out.println("Invalid arguments: " + e);
        }


        //Perform cipher

        //right output file
    }
}
