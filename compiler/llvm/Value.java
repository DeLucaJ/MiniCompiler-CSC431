package llvm;

public abstract class Value implements Element
{
    private Type type;

    public Value(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return this.type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public String llvm()
    {
        return null;
    }
}