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
        if (args.length != 3)
        {
            ErrorLog.printIncorrectArgs();
        }

        switch (args[0])
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
        mKeyFilename = args[1];
        mInputFilename = args[2];
    }

    private String mInputFilename;
    private String mKeyFilename;
    private Option mOption;
}
