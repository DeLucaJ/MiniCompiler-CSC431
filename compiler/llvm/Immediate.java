package llvm;

public class Immediate extends Value
{
    private final String value;

    public Immediate(Type type, String value)
    {
        super(type);
        this.value = value;
    }

    public String llvm()
    {
        return this.value;
    }

    public String getValue()
    {
        return this.value;
    }
}