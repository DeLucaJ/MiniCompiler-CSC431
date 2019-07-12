package llvm;

public class TruncInstruction implements Instruction
{
    private final Value target;
    private final String opcode;
    private final Type type1;
    private Value value;
    private final Type type2;

    public TruncInstruction(Value target, Type type1, Value value, Type type2)
    {
        this.target = target;
        this.opcode = "trunct";
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

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue)
    {
        if (value.llvm().equals(oldvalue.toLLVM().llvm()))
        {
            value = newvalue.toLLVM();
        }
    }
}