package llvm;

public class LLVMConditionalBranchInstruction implements LLVMInstruction
{
    private final LLVMValue condition;
    private final LLVMLabel iftrue;
    private final LLVMLabel iffalse;
    private final String opcode;

    public LLVMConditionalBranchInstruction(LLVMValue condition, LLVMLabel iftrue, LLVMLabel iffalse)
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