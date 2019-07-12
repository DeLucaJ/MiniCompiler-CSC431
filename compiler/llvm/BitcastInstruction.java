package llvm;

public class BitcastInstruction implements Instruction
{
    private final Value target;
    private final String opcode;
    private Type type1;
    private Value value;
    private final Type type2;

    public BitcastInstruction(Value target, Type type1, Value value, Type type2)
    {
        this.target = target;
        this.opcode = "bitcast";
        this.type1 = type1;
        this.value = value;
        this.type2 = type2;
    }

    public String llvm()
    {
        //System.out.println(this.type1 + " " + );
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
           value = oldvalue.toLLVM();
           type1 = oldvalue.getType();
        }
    }
}