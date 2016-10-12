import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AES {
    public final int numberOfRounds = 14;

    private int [] key;
    private ArrayList<Integer[]> inputFile;
    private char [] outputFile;

    private int [][][] stateArrays;

    private AESConfig aesConfig;
    private Cipher cipher;

    public AES(String[] args)
    {
        aesConfig = new AESConfig(args);
        try {
            Integer [] temp_key = this.hexToBinary(new File("./" + aesConfig.getKeyFilename())).get(0);
            this.key = Arrays.stream(temp_key).mapToInt(Integer::intValue).toArray();
            this.inputFile = this.hexToBinary(new File("./" + aesConfig.getInputFilename()));
        } catch (IOException e) {
            System.out.println("Invalid input files: " + e);
            System.exit(1);
        }

       this.cipher = new Cipher(key);
    }

    //Converts an input file of hexadecimal numbers into Integers of size byte
    private ArrayList<Integer[]> hexToBinary(File file) throws IOException
    {
        ArrayList<Integer[]> binaryInputArray = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {
                int length = line.length();
                Integer[] row = new Integer[length / 2];

                for (int i = 0; i < length; i += 2) {
                    row[i / 2] = ((Character.digit(line.charAt(i), 16) << 4)
                            + Character.digit(line.charAt(i+1), 16));
                }
                binaryInputArray.add(row);
            }
        } catch (Exception e) {
            ErrorLog.printError("Failed to open BufferedReader stream.", e);
        } finally
        {
            br.close();
        }

        return binaryInputArray;
    }

    private void createStateArrays() {
        this.stateArrays = new int [inputFile.size()][4][4];

        //create an array of 4x4 arrays for use with Cipher
        for (int i = 0; i < inputFile.size(); i++) {
            Integer [] row = inputFile.get(i);

            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    this.stateArrays[i][j][k] = row[(j*4) + k];
                }
            }
        }
    }

    private void printStateArrays() {
        String output = "{";
         for (int i = 0; i < this.stateArrays.length; i++) {
            output += " {\n";
            for (int j = 0; j < 4; j++) {
                output += " {";
                for (int k = 0; k < 4; k++) {
                    output += " " + this.stateArrays[i][j][k];
                }
                output += " }\n";
            }
            output += " }";
        }
        output += " }";
        System.out.println(output);
    }

    //*** NOT COMPLETE ***
    //Rough outline of encrypt
    private void encrypt(int [][] state)
    {
        int [][] roundKey = new int [4][4]; //CHANGE ME
        // Initial Round
        this.cipher.keyExpansion(numberOfRounds);
        this.cipher.addRoundKey(state, roundKey); //Takes in first part of round key from key expansion
        for (int i = 0; i < numberOfRounds - 2; i++) {
            this.cipher.subBytesEnc(state);
            this.cipher.shiftRowsEnc(state);
            this.cipher.mixColumnsEnc(state);
            this.cipher.addRoundKey(state, roundKey); //Takes in part of round key from key expansion
        }

        // Final Round
        this.cipher.subBytesEnc(state);
        this.cipher.shiftRowsEnc(state);
        this.cipher.addRoundKey(state, roundKey);
    }

    //*** NOT COMPLETE ***
    //Rough outline of decrypt
    private void decrypt(int [][] state)
    {
        int [][] roundKey = new int [4][4]; //CHANGE ME
        // Initial Round
        this.cipher.keyExpansion(numberOfRounds);
        this.cipher.addRoundKey(state, roundKey); //Takes in first part of round key from key expansion
        for (int i = numberOfRounds - 2; i >= 0; i--) {
            this.cipher.shiftRowsDec(state);
            this.cipher.subBytesDec(state);
            this.cipher.addRoundKey(state, roundKey); //Takes in part of round key from key expansion
            this.cipher.mixColumnsDec(state);
        }

        // Final Round
        this.cipher.shiftRowsDec(state);
        this.cipher.subBytesDec(state);
        this.cipher.addRoundKey(state, roundKey);
    }

    private void outputFile()
    {
        //convert from integers back to hex for output
    }

    public static void main(String[] args)
    {
        //Initialize AES
        AES aes = new AES(args);

        // Create initial state arrays for input
        aes.createStateArrays();
        aes.printStateArrays();

        //Perform cipher encrypt or decrypt with each state array

        //output file
    }
}
