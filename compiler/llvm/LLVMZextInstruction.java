package llvm;

public class LLVMZextInstruction implements LLVMInstruction
{
    private final LLVMValue target;
    private final String opcode;
    private final LLVMType type1;
    private final LLVMValue value;
    private final LLVMType type2;

    public LLVMZextInstruction(LLVMValue target, LLVMType type1, LLVMValue value, LLVMType type2)
    {
        this.target = target;
        this.opcode = "zext";
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