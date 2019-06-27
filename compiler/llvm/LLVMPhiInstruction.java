package llvm;

public class LLVMPhiInstruction implements LLVMInstruction
{
    private final LLVMValue target;
    private final String opcode;
    private final LLVMType type;
    private final LLVMValue value0;
    private final LLVMValue label0;
    private final LLVMValue value1;
    private final LLVMValue label1;

    public LLVMPhiInstruction(LLVMValue target, LLVMType type, LLVMValue value0, LLVMValue label0, LLVMValue value1, LLVMValue label1)
    {
        this.target = target;
        this.opcode = "phi";
        this.type = type;
        this.value0 = value0;
        this.label0 = label0;
        this.value1 = value1;
        this.label1 = label1;
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s [%s, %s] [%s, %s]",
            this.target.llvm(),
            this.opcode,
            this.type.llvm(),
            this.value0.llvm(),
            this.label0.llvm(),
            this.value1.llvm(),
            this.label1.llvm()
        );
    }
}