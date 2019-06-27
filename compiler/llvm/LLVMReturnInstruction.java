package llvm;

public class LLVMReturnInstruction implements LLVMInstruction
{
    private final String opcode;
    private final LLVMType type;
    private final LLVMValue value;
    
    public LLVMReturnInstruction(LLVMType type, LLVMValue value)
    {
        this.opcode = "ret";
        this.type = type;
        this.value = value;
    }

    public String llvm()
    {
        return String.format(
            "%s %s %s",
            this.opcode, 
            this.type.llvm(),
            this.value.llvm()
        );
    }
}