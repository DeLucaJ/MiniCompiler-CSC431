package llvm;

public abstract class LLVMValue implements LLVMElement
{
    private LLVMType type;

    public LLVMValue(LLVMType type)
    {
        this.type = type;
    }

    public LLVMType getType()
    {
        return this.type;
    }

    public String llvm()
    {
        return null;
    }
}