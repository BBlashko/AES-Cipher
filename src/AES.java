import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AES {
    public final int numberOfRounds = 14;

    private int [] key;
    private ArrayList<Integer[]> inputFile;

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
            ErrorLog.printError("Invalid input files: ", e);
        }
        this.cipher = new Cipher(key);
    }

    public void performCipher()
    {
        // Create initial state arrays for input
        int [][][] roundKeys = this.cipher.keyExpansion(numberOfRounds);
        if (this.aesConfig.getOption() == AESConfig.Option.ENCRYPT)
        {
            for (int i = 0; i < stateArrays.length; i++)
            {
                encryptState(stateArrays[i], roundKeys);
            }
        }
        else if (this.aesConfig.getOption() == AESConfig.Option.DECRYPT)
        {
            // decrypt();
        }
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

    //Converts an int[][] matrix into a single hexadecimal string
    private String convertIntMatrixToHexStringArray(int[][] num)
    {
        int arraySize = num.length;
        String hexString = "";
        for (int i = 0; i < arraySize; i++)
        {
            for (int j = 0; j < arraySize; j++)
            {
                hexString += String.format("%02X", num[i][j]);
            }
        }
        return hexString;
    }

    //Parse input file and place each line of the file
    //into an array of 16 byte arrays.
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

    //Encrypt each state
    private void encryptState(int [][] state, int [][][] roundKeys)
    {
        // Initial Round
        this.cipher.addRoundKey(state, roundKeys[0]); //Takes in first part of round key from key expansion

        // Do 13 rounds
        for (int i = 1; i < numberOfRounds; i++) {
            this.cipher.subBytesEnc(state);
            this.cipher.shiftRowsEnc(state);
            this.cipher.mixColumnsEnc(state);
            this.cipher.addRoundKey(state, roundKeys[i]); //Takes in part of round key from key expansion
        }

        // Final Round
        this.cipher.subBytesEnc(state);
        this.cipher.shiftRowsEnc(state);
        this.cipher.addRoundKey(state, roundKeys[numberOfRounds]);
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

    private void outputFile() {

        ArrayList<String> output = new ArrayList<>();

        //convert from integers back to hex strings for output
        int stateSize = stateArrays.length;
        for (int i = 0; i < stateSize; i++) {
            output.add(convertIntMatrixToHexStringArray(stateArrays[i]));
        }

        String filename = generateFilename();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filename));
            for (String line : output)
            {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            ErrorLog.printError("Failed to output file. ", e);
        } finally {
            try {
                bw.close();
            } catch (IOException e)
            {
                ErrorLog.printError("Failed to close BufferedWriter.", e);
            }
        }
    }

    private String generateFilename()
    {
        String filename = aesConfig.getInputFilename();
        if (this.aesConfig.getOption() == AESConfig.Option.ENCRYPT)
        {
            filename += ".enc";
        }
        else if (this.aesConfig.getOption() == AESConfig.Option.DECRYPT)
        {
            filename += ".dec";
        }
        return filename;
    }

    public static void main(String[] args) {
        //Initialize AES
        AES aes = new AES(args);

        //Parse input file and create 16 byte arrays
        aes.createStateArrays();
        //aes.printStateArrays();

        //Perform cipher encrypt or decrypt with each state array
        aes.performCipher();

        //output file
        aes.outputFile();
    }
}
