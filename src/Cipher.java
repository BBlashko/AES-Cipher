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

    public void subBytes() {

    }
    
    public void shiftRows() {
        
    }

    public void mixColumns() {
        
    }

    public void addRoundKeyEnc() {
        
    }

    public void addRoundKeyDec() {
        
    }
}
