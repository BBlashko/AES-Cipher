import java.io.*;
import java.util.ArrayList;

public class AES {
    private Integer [] key;
    private ArrayList<Integer[]> inputFile;
    private String outputFile;

    private AESConfig aesConfig;
    private Cipher cipher;

    public AES(String[] args)
    {
        aesConfig = new AESConfig(args);
        try {
            key = this.hexToBinary(new File("./" + aesConfig.getKeyFilename())).get(0);
            inputFile = this.hexToBinary(new File("./" + aesConfig.getInputFilename()));
        } catch (IOException e) {
            System.out.println("Invalid input files: " + e);
            System.exit(1);
        }

       cipher = new Cipher(key);
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

    private void encrypt()
    {

    }

    private void decrypt()
    {

    }

    private void outputFile()
    {

    }

    public static void main(String[] args)
    {
        //Initialize AES
        AES aes = new AES(args);

        //Perform cipher
        //loop over lines of input file here and put into 4X4 arrays?

        //right output file
    }
}
