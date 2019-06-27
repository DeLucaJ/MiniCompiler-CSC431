package llvm;

public class LLVMLoadInstruction implements LLVMInstruction
{
    public final String opcode;
    public final LLVMValue target;
    public final LLVMValue pointer;
    public final LLVMPointer pointerType;

    public LLVMLoadInstruction(LLVMValue target, LLVMValue pointer, LLVMPointer pointertype)
    {
        this.target = target;
        this.pointer = pointer;
        this.pointerType = pointertype;
        this.opcode = "load";
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s",
            this.target.llvm(),
            this.opcode,
            this.pointerType.llvm(),
            this.pointer.llvm()
        );
    }
}