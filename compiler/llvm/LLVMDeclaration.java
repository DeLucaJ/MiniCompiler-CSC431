package llvm;

public class LLVMDeclaration implements LLVMElement
{
    private final String name;
    private final LLVMType type;

    public LLVMDeclaration(String name, LLVMType type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public LLVMType getType()
    {
        return this.type;
    }

    //possible problems
    public String llvm()
    {
        return "@" + this.name + " = common global " + this.type.llvm() + "null, align 4";
    }
}