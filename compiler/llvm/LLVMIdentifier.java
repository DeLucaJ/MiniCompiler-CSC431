package llvm;

public class LLVMIdentifier extends LLVMNamedValue
{
    private final boolean global;

    public LLVMIdentifier(LLVMType type, String name, boolean global)
    {
        super(type, name);
        this.global = global;
    }

    public boolean isGlobal()
    {
        return this.global;
    }

    public String llvm()
    {
        String tag = this.global ? "@" : "%";
        return String.format("%s%s", tag, this.getName());
    }
}