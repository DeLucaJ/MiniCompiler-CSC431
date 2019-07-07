package llvm;

public class AllocationInstruction implements Instruction
{
    private final Value target;
    private final String opcode;
    private final Type type;

    public AllocationInstruction(Value target, Type type)
    {
        this.target = target;
        this.type = type;
        this.opcode = "alloca";
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s",
            this.target.llvm(),
            this.opcode,
            this.type.llvm()
        );
    }
}