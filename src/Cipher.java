/**
 * Created by lynx on 07/10/16.
 */
public class Cipher {

    private int mKey[];
    private byte mState[];
    private byte mOut[];

    public Cipher(int [] key) {
    	this.mKey = key;
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
    
    /** Shifts the row elements to the left */
    public void shiftRowsEnc(int[][] state) {
        int [][] tState = new int[4][4];
        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(state[i], 0, tState[i], 0, 4);
        }

        for(int i = 0; i < 4; i++)
        {
            for(int j = 1; j < 4; j++)
            {
                state[i][j] = tState[(i + j) % 4][j];
            }
        }
    }

    /** Shifts the row elements to the right */
    public void shiftRowsDec(int[][] state) {
        int [][] tState = new int[4][4];
        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(state[i], 0, tState[i], 0, 4);
        }

        for(int i = 0; i < 4; i++)
        {
            for(int j = 1; j < 4; j++)
            {
                state[i][j] = tState[((i + 4) - j) % 4][j];
            }
        }
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
            result ^= multHelper(tState[j][k], gal[i][k]);
        }
        return result;
    }

    /** Idea for this helper function was borrow from here: 
        https://github.com/rishidewan33/Advanced-Encryption-Standard-Algorithm/blob/master/src/AES.java 
    */
    private int multHelper(int t, int g) {
        int c1 = t / 16;
        int c2 = t % 16;
        if (g == 1) {
            return t;
        } else if (g == 2) {
            return MultTables.mult2[c1][c2];
        } else if (g == 3) {
            return MultTables.mult3[c1][c2];
        }
        return 0;
    }

    public void mixColumnsDec(int[][] state) {
        int [][] tState = new int[4][4];
        for(int i = 0; i < 4; i++)
        {
            System.arraycopy(state[i], 0, tState[i], 0, 4);
        }
        for(int i = 0; i < 4; i++)
        {   
            for(int j = 0; j < 4; j++)
            {
                state[i][j] = matMultDec(tState, invGaloisMatrix, i, j);
            }
        }
    }

    private int matMultDec(int[][] tState, int[][] invGal, int i, int j) {
        int result = 0;
        for(int k = 0; k < 4; k++)
        {   
            result ^= multHelperDec(tState[j][k], invGal[i][k]);
        }
        return result;
    }

    private int multHelperDec(int t, int g) {
        int c1 = t / 16;
        int c2 = t % 16;
        if (g == 9) {
            return MultTables.mult9[c1][c2];
        } else if (g == 0xb) {
            return MultTables.mult11[c1][c2];
        } else if (g == 0xd) {
            return MultTables.mult13[c1][c2];
        } else if (g == 0xe) {
            return MultTables.mult14[c1][c2];
        }
        return 0;
    }
    public void addRoundKeyEnc() {
        
    }

    public void addRoundKeyDec() {
        
    }
}
