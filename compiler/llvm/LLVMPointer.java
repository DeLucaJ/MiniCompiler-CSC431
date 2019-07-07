package llvm;

public class LLVMPointer implements LLVMType
{
    public LLVMType pointerType;

    public LLVMPointer(LLVMType pointerType)
    {
        this.pointerType = pointerType;
    }

    public String llvm()
    {
        return pointerType.llvm() + "*";
    }

    public LLVMType getPointerType()
    {
        return this.pointerType;
    }
}