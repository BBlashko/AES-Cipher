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

    public int[][] keyExpansion(int numRounds) {
        int [][] expandedKeys = new int[numRounds + 1][32];
        return expandedKeys;
    }

	/*
	 * subByteEnc: Replaces all elements in state array with values in sBox[][].
     */
    public void subBytesEnc(int[][] state) {
    	for (int i = 0; i < 4; i++) {
    		for (int j= 0; j < 4; j++) {
    			int hexVal = state[i][j];
    			state[i][j] = LookupTables.sBox[hexVal / 16][hexVal % 16];
    		}
    	}
    }

	/*
	 * subBytesDec: Inverse of subBytesEc. Replaces all elements in state array with values in invSBox[][].
     */
    public void subBytesDec(int[][] state) {
    	for (int i = 0; i < 4; i++) {
    		for (int j= 0; j < 4; j++) {
    			int hexVal = state[i][j];
    			state[i][j] = LookupTables.invSBox[hexVal / 16][hexVal % 16];
    		}
    	}
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
                state[i][j] = matMult(tState, LookupTables.galoisMatrix, i, j);
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

    /* Idea for this helper function was borrow from here:
     * https://github.com/rishidewan33/Advanced-Encryption-Standard-Algorithm/blob/master/src/AES.java
     */
    private int multHelper(int t, int g) {
        int c1 = t / 16;
        int c2 = t % 16;
        if (g == 1) {
            return t;
        } else if (g == 2) {
            return LookupTables.mult2[c1][c2];
        } else if (g == 3) {
            return LookupTables.mult3[c1][c2];
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
                state[i][j] = matMultDec(tState, LookupTables.invGaloisMatrix, i, j);
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
            return LookupTables.mult9[c1][c2];
        } else if (g == 0xb) {
            return LookupTables.mult11[c1][c2];
        } else if (g == 0xd) {
            return LookupTables.mult13[c1][c2];
        } else if (g == 0xe) {
            return LookupTables.mult14[c1][c2];
        }
        return 0;
    }

    /**
	 * addRoundKey: state array is XOR'd with a portion of the key, the round key
	 * Note: addRoundKey is the inverse of itself (because XOR is the inverse of itself) and so is
	 * the same for encryption and decryption.
     */
    public void addRoundKey(int[][] state, int[][] roundKey) {
        for (int i = 0; i < 4; i++) {
        	for (int j = 0; j < 4; j++) {
        		state[i][j] ^= roundKey[i][j];
        	}
        }
    }
}
