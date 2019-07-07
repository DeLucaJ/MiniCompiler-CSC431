package llvm;

public abstract class NamedValue extends Value
{
    private String name;

    public NamedValue(Type type, String name) 
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