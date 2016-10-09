/**
 * Created by lynx on 07/10/16.
 */
public class AESConfig {

    public enum Option { ENCRYPT,
        DECRYPT}

    public AESConfig(String[] args)
    {
        readCommandArgs(args);
    }

    public String getInputFilename()
    {
        return mInputFilename;
    }

    public String getKeyFilename()
    {
        return mKeyFilename;
    }

    public Option getOption()
    {
        return mOption;
    }

    private void readCommandArgs(String [] args)
    {
        if (args.length != 4)
        {
            ErrorLog.printIncorrectArgs();
        }

        switch (args[1])
        {
            case "e":
                mOption = Option.ENCRYPT;
                break;
            case "d":
                mOption = Option.DECRYPT;
                break;
            default:
                ErrorLog.printIncorrectArgs();
        }
        mKeyFilename = args[2];
        mInputFilename = args[3];
    }

    private String mInputFilename;
    private String mKeyFilename;
    private Option mOption;
}
