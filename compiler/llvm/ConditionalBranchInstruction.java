package llvm;

public class ConditionalBranchInstruction implements Instruction
{
    private final Value condition;
    private final Label iftrue;
    private final Label iffalse;
    private final String opcode;

    public ConditionalBranchInstruction(Value condition, Label iftrue, Label iffalse)
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
            this.condition.llvm(),
            this.iftrue.llvm(),
            this.iffalse.llvm()
        );
    }
}