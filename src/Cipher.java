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

    public int[][][] keyExpansion(int numRounds) {
        int [] generatedKeyValues = new int[(numRounds + 1) * 16];

        //first int [] contains the original key
        //EG. the first 32 Bytes == 32
        for (int i = 0; i < 32; i++)
        {
            generatedKeyValues[i] = mKey[i];
        }

        int temp[] = new int[4];
        int rConIter = 1;
        int generatedBytes = 32;
        int maxBytes = 240;
        int k;
        //generate the rest of the expanded keys
        while (generatedBytes < maxBytes)
        {
            for (k = 0; k < 4; k++)
            {
                temp[k] = generatedKeyValues[k + generatedBytes - 4];
            }
            //Every 32 bytes, do keyExpansionCore
            if (generatedBytes % 32 == 0)
            {
                temp = keyExpansionCore(temp, rConIter++);
            }
            else if (generatedBytes % 32 == 16)
            {
                for (k = 0; k < 4; k++)
                {
                    temp[k] = LookupTables.sBox[temp[k] / 16][temp[k] % 16];
                }
            }
            for (k = 0; k < 4; k++)
            {
                generatedKeyValues[generatedBytes] = generatedKeyValues[generatedBytes - 32] ^ temp[k];
                generatedBytes++;
            }
        }

        int [][][] expandedKeys = new int[numRounds + 1][4][4];
        for (k = 0; k < generatedKeyValues.length; k++)
        {
            expandedKeys[k / 16][(k % 16) / 4][(k % 16) % 4] = generatedKeyValues[k];
        }
        return expandedKeys;
    }

    private int[] keyExpansionCore(int [] subBytes, int i)
    {
        //rotate left 1 byte
        subBytes = rotateLeft(subBytes);

        //s-box
        for (int k = 0; k < 4; k++)
        {
            subBytes[k] = LookupTables.sBox[subBytes[k] / 16][subBytes[k] % 16];
        }

        // Rcon
        subBytes[0] ^= LookupTables.rCon[i / 16][i % 16];

        return subBytes;
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
                state[j][i] = matMult(tState, LookupTables.galoisMatrix, i, j);
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
                state[j][i] = matMultDec(tState, LookupTables.invGaloisMatrix, i, j);
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

    private int [] rotateLeft(int [] bytesIn)
    {
        int temp = bytesIn[0];
        bytesIn[0] = bytesIn[1];
        bytesIn[1] = bytesIn[2];
        bytesIn[2] = bytesIn[3];
        bytesIn[3] = temp;

        return bytesIn;
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
