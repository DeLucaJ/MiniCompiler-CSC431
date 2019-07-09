package llvm;

public class SdivInstruction implements Instruction
{
    private final Value target;
    private final String opcode;
    private final Type type;
    private final Value op1;
    private final Value op2;

    public SdivInstruction(Value target, Value op1, Value op2)
    {
        this.target = target;
        this.op1 = op1;
        this.op2 = op2;
        this.type = new Integer32();
        this.opcode = "sdiv";
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
}