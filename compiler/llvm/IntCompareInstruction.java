package llvm;

public class IntCompareInstruction implements Instruction
{
    private final Value target;
    private final String opcode;
    private final Cond condition; 
    private final Type type;
    private final Value op1;
    private final Value op2;

    public IntCompareInstruction(Value target, Cond condition, Value op1, Value op2)
    {
        this.target = target;
        this.op1 = op1;
        this.op2 = op2;
        this.condition = condition;
        this.type = new Integer32();
        this.opcode = "sub";
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s %s, %s", 
            this.target, 
            this.opcode,
            Utility.condToString(this.condition), 
            this.type.llvm(), 
            this.op1.llvm(), 
            this.op2.llvm()
        );
    }
}
