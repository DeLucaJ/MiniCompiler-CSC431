package llvm;

public class LLVMImmediate extends LLVMValue
{
    private final String value;

    public LLVMImmediate(LLVMType type, String value)
    {
        super(type);
        this.value = value;
    }

    public String llvm()
    {
        return this.value;
    }

    public String getValue()
    {
        return this.value;
    }
}