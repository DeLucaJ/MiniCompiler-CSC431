package llvm;

public class NullValue extends Value
{
    public NullValue(Pointer pointerType)
    {
        super(pointerType);
    }

    public String llvm()
    {
        return "null";
    }
}