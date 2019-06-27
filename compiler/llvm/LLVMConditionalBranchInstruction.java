package llvm;

public class LLVMConditionalBranchInstruction implements LLVMInstruction
{
    private final LLVMCond condition;
    private final LLVMValue iftrue;
    private final LLVMValue iffalse;
    private final String opcode;

    public LLVMConditionalBranchInstruction(LLVMCond condition, LLVMValue iftrue, LLVMValue iffalse)
    {
        this.opcode = "br";
        this.condition = condition;
        this.iffalse = iffalse;
        this.iftrue = iftrue;
    }

    public String llvm()
    {
        return String.format(
            "%s i1 %s, label %s, label %s", 
            this.opcode,
            this.condition,
            this.iftrue,
            this.iffalse
        );
    }
}