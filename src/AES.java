import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class AES {
    //cipher data.
    private final int numberOfRounds = 14;
    private ArrayList<int[]> inputFile;

    //parsed input file line array matrices
    private int [][][] stateArrays;

    //classes
    private AESConfig aesConfig;
    private Cipher cipher;

    public AES(String[] args) {
        aesConfig = new AESConfig(args);
        System.out.println("\tConverting key to binary");
        int [] key = this.hexToBinary(new File("./" + aesConfig.getKeyFilename())).get(0);

        if (key.length != 32) {
            ErrorLog.printIncorrectKeyLength();
        }

        System.out.println("\tConverting input file to binary");
        this.inputFile = this.hexToBinary(new File("./" + aesConfig.getInputFilename()));
        for (int i = 0; i < this.inputFile.size(); i++) {
            if (this.inputFile.get(i).length != 16) {
                ErrorLog.printIncorrectInputLineLength();
            }
        }
        System.out.println("\tInitialising cipher");
        this.cipher = new Cipher(key);
    }

    //Encrypt or decrypt the input file
    public void performCipher() {
        // Create initial state arrays for input
        System.out.println("\tGenerating Round Keys");
        int [][][] roundKeys = this.cipher.keyExpansion(numberOfRounds);
        if (this.aesConfig.getOption() == AESConfig.Option.ENCRYPT) {
            System.out.println("\tEncrypting file");
            for (int i = 0; i < stateArrays.length; i++) {
                encryptState(stateArrays[i], roundKeys);
            }
        }
        else if (this.aesConfig.getOption() == AESConfig.Option.DECRYPT) {
            System.out.println("\tDecrypting file");
            for (int i = 0; i < stateArrays.length; i++) {
                decryptState(stateArrays[i], roundKeys);
            }
        }
    }

    //Parse input file and place each line of the file
    //into an array of 16 byte arrays.
    private void createStateArrays() {
        this.stateArrays = new int [inputFile.size()][4][4];

        //create an array of 4x4 arrays for use with Cipher
        for (int i = 0; i < inputFile.size(); i++) {
            int [] row = inputFile.get(i);

            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    this.stateArrays[i][j][k] = row[(j*4) + k];
                }
            }
        }
    }

    //used for debugging
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
    private void encryptState(int [][] state, int [][][] roundKeys) {
        // Initial Round
        this.cipher.addRoundKey(state, roundKeys[0]);

        // Do 13 rounds
        for (int i = 1; i < numberOfRounds; i++) {
            this.cipher.subBytesEnc(state);
            this.cipher.shiftRowsEnc(state);
            this.cipher.mixColumnsEnc(state);
            this.cipher.addRoundKey(state, roundKeys[i]);
        }

        // Final Round
        this.cipher.subBytesEnc(state);
        this.cipher.shiftRowsEnc(state);
        this.cipher.addRoundKey(state, roundKeys[numberOfRounds]);
    }

    //Decrypt each state
    private void decryptState(int [][] state, int [][][] roundKeys) {
        // Initial Round
        this.cipher.addRoundKey(state, roundKeys[14]);

        // Do 13 rounds
        for (int i = numberOfRounds - 1; i >= 1; i--) {
            this.cipher.shiftRowsDec(state);
            this.cipher.subBytesDec(state);
            this.cipher.addRoundKey(state, roundKeys[i]);
            this.cipher.mixColumnsDec(state);
        }

        // Final Round
        this.cipher.shiftRowsDec(state);
        this.cipher.subBytesDec(state);
        this.cipher.addRoundKey(state, roundKeys[0]);
    }

    private void outputFile() {

        ArrayList<String> output = new ArrayList<>();

        //convert from integers back to hex strings for output
        int stateSize = stateArrays.length;
        for (int i = 0; i < stateSize; i++) {
            output.add(convertIntMatrixToHexStringArray(stateArrays[i]));
        }

        String filename = generateFilename();
        System.out.println("\tOutputing File: " + filename);

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < stateSize; i++) {
                if (i == stateSize - 1) {
                    bw.write(output.get(i));
                }
                else {
                    bw.write(output.get(i));
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            ErrorLog.printError("Failed to output file. ", e);
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                ErrorLog.printError("Failed to close BufferedWriter.", e);
            }
        }
    }

    private String generateFilename() {
        String filename = aesConfig.getInputFilename();
        if (this.aesConfig.getOption() == AESConfig.Option.ENCRYPT) {
            filename += ".enc";
        }
        else if (this.aesConfig.getOption() == AESConfig.Option.DECRYPT) {
            filename += ".dec";
        }
        return filename;
    }

    //UTILS:
    //Converts an input file of hexadecimal numbers into ints of size byte
    private ArrayList<int[]> hexToBinary(File file) {
        ArrayList<int[]> binaryInputArray = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {
                int length = line.length();

                //check that the line has only hexadecimal characters and is divisible by 2
                if (!line.matches("^[0-9A-Fa-f]+$") || line.length() % 2 != 0) {
                    ErrorLog.printInvalidCharacters();
                }

                int[] row = new int[length / 2];
                for (int i = 0; i < length; i += 2) {
                    row[i / 2] = ((Character.digit(line.charAt(i), 16) << 4)
                            + Character.digit(line.charAt(i+1), 16));
                }
                binaryInputArray.add(row);
            }
        } catch (Exception e) {
            ErrorLog.printError("Failed to open BufferedReader stream.", e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                ErrorLog.printError("Failed to close BufferedWriter stream.", e);
            }
        }
        return binaryInputArray;
    }

    //Converts an int[][] matrix into a single hexadecimal string
    private String convertIntMatrixToHexStringArray(int[][] num) {
        int arraySize = num.length;
        String hexString = "";
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                hexString += String.format("%02X", num[i][j]);
            }
        }
        return hexString;
    }


    public static void main(String[] args) {
        //Initialize AES
        System.out.println("Initialising AES-256...");
        AES aes = new AES(args);

        //Parse input file and create 16 byte arrays
        System.out.println("Parsing input file data...");
        aes.createStateArrays();

        //Perform cipher encrypt or decrypt with each state array
        System.out.println("Performing Cipher...");
        aes.performCipher();

        //output file
        System.out.println("Outputing results to file...");
        aes.outputFile();
    }
}
