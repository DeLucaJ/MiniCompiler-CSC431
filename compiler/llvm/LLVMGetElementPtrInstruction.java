package llvm;

public class LLVMGetElementPtrInstruction implements LLVMInstruction
{
    private final String opcode;
    private final LLVMValue target;
    private final LLVMPointer pointerType;
    private final LLVMValue pointerValue;
    private final String index;

    public LLVMGetElementPtrInstruction(LLVMValue target, LLVMPointer pointerType, LLVMValue pointerValue, String index)
    {
        this.opcode = "getelementptr";
        this.target = target;
        this.pointerType = pointerType;
        this.pointerValue = pointerValue;
        this.index = index;
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s, i1 0, i32 %s",
            this.target.llvm(),
            this.opcode,
            this.pointerType.llvm(),
            this.pointerValue.llvm(),
            this.index
        );
    }
}