/**
 * Created by lynx on 07/10/16.
 */
public class Cipher {

    private byte mKey[];
    private byte mInputFile[];
    private byte mState[];
    private byte mOut[];

    public Cipher(byte [] key, byte [] inputFile) {
    	this.mKey = key;
    	this.mInputFile = inputFile;
    }

    /**
     * Galois matrix used for multiplying in mixColumnsEnc
     */
    public static final int[][] galoisMatrix = 
        {{0x02, 0x03, 0x01, 0x01},
        {0x01, 0x02, 0x03, 0x01},
        {0x01, 0x01, 0x02, 0x03},
        {0x03, 0x01, 0x01, 0x02}};

    /**
     * Inverse Galois matrix used for multiplying in mixColumnsDec
     */
    public static final int[][] invGaloisMatrix = 
        {{0x0e, 0x0b, 0x0d, 0x09},
        {0x09, 0x0e, 0x0b, 0x0d},
        {0x0d, 0x09, 0x0e, 0x0b},
        {0x0b, 0x0d, 0x09, 0x0e}};


    public void subBytes() {

    }
    
    public void shiftRows() {
        
    }

    public void mixColumnsEnc(int[][] state) {
        int [][] tState = new int[4][4];
        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(state[i], 0, tState[i], 0, 4);
        }
        for(int i = 0; i < 4; i++)
        {   
            for(int j = 0; j < 4; j++)
            {
                state[i][j] = matMult(tState, galoisMatrix, i, j);
            }
        }
    }

    private int matMult(int[][] tState, int[][] gal, int i, int j) {
        int result = 0;
        for(int k = 0; k < 4; k++)
        {
            //result ^= multHelper(tState[
        }
        return 0;
    }

    private int multHelper(int sEntry, int gEntry) {
        return 0;
    }

    public void addRoundKeyEnc() {
        
    }

    public void addRoundKeyDec() {
        
    }
}
