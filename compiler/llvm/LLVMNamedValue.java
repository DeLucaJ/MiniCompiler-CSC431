package llvm;

public abstract class LLVMNamedValue extends LLVMValue
{
    private String name;

    public LLVMNamedValue(LLVMType type, String name) 
    {
        super(type);
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String llvm()
    {
        return null;
    }
}