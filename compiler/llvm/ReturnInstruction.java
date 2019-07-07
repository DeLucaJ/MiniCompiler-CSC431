package llvm;

public class ReturnInstruction implements Instruction
{
    private final String opcode;
    private final Type type;
    private final Value value;
    
    public ReturnInstruction(Type type, Value value)
    {
        this.opcode = "ret";
        this.type = type;
        this.value = value;
    }

    public String llvm()
    {
        return String.format(
            "%s %s %s",
            this.opcode, 
            this.type.llvm(),
            this.value.llvm()
        );
    }
}