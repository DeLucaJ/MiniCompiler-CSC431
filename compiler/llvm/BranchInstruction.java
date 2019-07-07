package llvm;

public class BranchInstruction implements Instruction
{
    private final Value dest;
    private final String opcode;

    public BranchInstruction(Value dest)
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