package llvm;

public class LLVMBitcastInstruction implements LLVMInstruction
{
    private final LLVMValue target;
    private final String opcode;
    private final LLVMType type1;
    private final LLVMValue value;
    private final LLVMType type2;

    public LLVMBitcastInstruction(LLVMValue target, LLVMType type1, LLVMValue value, LLVMType type2)
    {
        this.target = target;
        this.opcode = "bitcast";
        this.type1 = type1;
        this.value = value;
        this.type2 = type2;
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s to %s", 
            this.target.llvm(),
            this.opcode,
            this.type1.llvm(),
            this.value.llvm(),
            this.type2.llvm()
        );
    }
}