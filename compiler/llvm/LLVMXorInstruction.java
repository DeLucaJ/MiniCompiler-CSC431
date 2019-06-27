package llvm;

public class LLVMXorInstruction implements LLVMInstruction
{
    private final LLVMValue target;
    private final String opcode;
    private final LLVMType type;
    private final LLVMValue op1;
    private final LLVMValue op2;

    public LLVMXorInstruction(LLVMValue target, LLVMValue op1, LLVMValue op2)
    {
        this.target = target;
        this.op1 = op1;
        this.op2 = op2;
        this.type = new LLVMInteger32();
        this.opcode = "xor";
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s, %s", 
            this.target, 
            this.opcode, 
            this.type.llvm(), 
            this.op1.llvm(), 
            this.op2.llvm()
        );
    }
}