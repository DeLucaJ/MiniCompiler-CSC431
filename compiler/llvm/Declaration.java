package llvm;

public class Declaration implements Element
{
    private final String name;
    private final Type type;

    public Declaration(String name, Type type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public Type getType()
    {
        return this.type;
    }

    //possible problems
    public String llvm()
    {
        return "@" + this.name + " = common global " + this.type.llvm() + " null, align 4";
    }
}