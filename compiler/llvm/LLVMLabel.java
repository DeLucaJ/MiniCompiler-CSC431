package llvm;

public class LLVMLabel extends LLVMNamedValue
{
    public LLVMLabel(String name)
    {
        super(new LLVMLabelType(), name);
    }

    public String llvm()
    {
        return "%" + this.getName();
    }
}