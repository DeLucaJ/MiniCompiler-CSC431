package llvm;

public class LLVMNullValue extends LLVMValue
{
    public LLVMNullValue(LLVMPointer pointerType)
    {
        super(pointerType);
    }

    public String llvm()
    {
        return "null";
    }
}