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
     * Galois table used for mixColumns
     */
    public static final int[][] gal = 
        {{0x02, 0x03, 0x01, 0x01},
        {0x01, 0x02, 0x03, 0x01},
        {0x01, 0x01, 0x02, 0x03},
        {0x03, 0x01, 0x01, 0x02}};

    /**
     * Inverse Galois table used for invMixColumns
     */
    public static final int[][] invgal = 
        {{0x0e, 0x0b, 0x0d, 0x09},
        {0x09, 0x0e, 0x0b, 0x0d},
        {0x0d, 0x09, 0x0e, 0x0b},
        {0x0b, 0x0d, 0x09, 0x0e}};


    public void subBytes() {

    }
    
    public void shiftRows() {
        
    }

    public void mixColumns(int[][] state) {
        int [][] tState = new[4][4];
        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(arr[i], 0, tarr[i], 0, 4);
        }
        
    }

    public void addRoundKeyEnc() {
        
    }

    public void addRoundKeyDec() {
        
    }
}
