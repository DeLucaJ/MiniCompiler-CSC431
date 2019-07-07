package llvm;

public class Pointer implements Type
{
    public Type pointerType;

    public Pointer(Type pointerType)
    {
        this.pointerType = pointerType;
    }

    public String llvm()
    {
        return pointerType.llvm() + "*";
    }

    public Type getPointerType()
    {
        return this.pointerType;
    }
}