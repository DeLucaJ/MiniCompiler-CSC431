package llvm;

public class SubInstruction implements Instruction
{
    private final Value target;
    private final String opcode;
    private final Type type;
    private Value op1;
    private Value op2;

    public SubInstruction(Value target, Value op1, Value op2)
    {
        this.target = target;
        this.op1 = op1;
        this.op2 = op2;
        this.type = op1.getType();
        this.opcode = "sub";
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s, %s", 
            this.target.llvm(), 
            this.opcode, 
            this.type.llvm(), 
            this.op1.llvm(), 
            this.op2.llvm()
        );
    }

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue)
    {
        if (op1.llvm().equals(oldvalue.toLLVM().llvm()))
        {
            op1 = newvalue.toLLVM();
        }
        if (op2.llvm().equals(oldvalue.toLLVM().llvm()))
        {
            op2 = newvalue.toLLVM();
        }
    }
}