package llvm;

public class LLVMRegister extends LLVMNamedValue
{
    public LLVMRegister(LLVMType type, String name)
    {
        super(type, name);
    }

    public String llvm()
    {
        return "%" + String.format("%s", this.getName());
    }
}