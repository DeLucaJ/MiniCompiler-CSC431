package llvm;

public class LLVMAllocationInstruction implements LLVMInstruction
{
    private final LLVMValue target;
    private final String opcode;
    private final LLVMType type;

    public LLVMAllocationInstruction(LLVMValue target, LLVMType type)
    {
        this.target = target;
        this.type = type;
        this.opcode = "alloca";
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s",
            this.target.llvm(),
            this.opcode,
            this.type.llvm()
        );
    }
}