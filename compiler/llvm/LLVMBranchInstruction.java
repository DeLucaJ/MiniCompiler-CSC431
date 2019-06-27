package llvm;

public class LLVMBranchInstruction implements LLVMInstruction
{
    private final LLVMValue dest;
    private final String opcode;

    public LLVMBranchInstruction(LLVMValue dest)
    {
        this.opcode = "br";
        this.dest = dest;
    }

    public String llvm()
    {
        return String.format(
            "%s label %s", 
            this.opcode,
            this.dest.llvm()
        );
    }
}