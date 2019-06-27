package llvm;

public class LLVMStructure implements LLVMType
{
    private String name;

    public LLVMStructure(String name)
    {
        this.name = name;
    }

    public String llvm()
    {
        return "%struct." + this.name;
    }
}