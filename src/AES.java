import java.io.*;
import java.util.ArrayList;

public class AES {
    private Integer [] key;
    private ArrayList<Integer[]> inputFile;
    private char [] outputFile;

    private int [][][] stateArrays;

    private AESConfig aesConfig;
    private Cipher cipher;

    public AES(String[] args)
    {
        aesConfig = new AESConfig(args);
        try {
            this.key = this.hexToBinary(new File("./" + aesConfig.getKeyFilename())).get(0);
            this.inputFile = this.hexToBinary(new File("./" + aesConfig.getInputFilename()));
        } catch (IOException e) {
            System.out.println("Invalid input files: " + e);
            System.exit(1);
        }

       this.cipher = new Cipher(key);
    }

    private ArrayList<Integer[]> hexToBinary(File file) throws IOException
    {
        ArrayList<Integer[]> binaryInputArray = new ArrayList<Integer[]>();

        try (InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in);
            Reader buffer = new BufferedReader(reader)) {

            int r;
            int row_index = 0;
            int col_index = 0;
            ArrayList<Integer> row = new ArrayList<Integer>();

            while ((r = buffer.read()) != -1 ) {
                char ch = (char) r;

                if (ch != '\n') { //loop over a single line of hex characters
                    //check for non hex characters/line lengths here?
                    Integer binaryInt = Integer.parseInt(String.valueOf(ch), 16);
                    row.add(col_index, binaryInt);
                    col_index++;

                } else {
                    Integer [] row_array = (Integer []) row.toArray(new Integer[row.size()]);
                    binaryInputArray.add(row_index, row_array);
                    row = new ArrayList<Integer>();
                    row_index++;
                    col_index = 0;
                }
            }

            Integer [] row_array = (Integer []) row.toArray(new Integer[row.size()]);
            binaryInputArray.add(row_index, row_array);
        }
        return binaryInputArray;
    }

    private void createStateArrays() { 
        this.stateArrays = new int [inputFile.size() * 2][4][4];

        //create an array of 4x4 arrays for use with Cipher
        for (int i = 0; i < inputFile.size(); i++) {
            Integer [] row = inputFile.get(i);
            for (int j = 0; j < 2; j++) {
                int row_ctr = 0;
                for (int k = 0; k < 16; k++) {
                    if (k > 0 && k % 4 == 0) row_ctr++;
                    this.stateArrays[((i*2) + j)][row_ctr][k % 4] = row[k + (j*16)];
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


    private void encrypt(Integer [] state) 
    { 

    }

    private void decrypt(Integer [] state)
    {

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
