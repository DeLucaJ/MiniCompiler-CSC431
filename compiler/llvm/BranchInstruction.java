package llvm;

public class BranchInstruction implements Instruction
{
    private final Label dest;
    private final String opcode;

    public BranchInstruction(Label dest)
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

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue) {}
}