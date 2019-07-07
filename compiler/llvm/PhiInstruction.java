package llvm;

public class PhiInstruction implements Instruction
{
    private final Value target;
    private final String opcode;
    private final Type type;
    private final Value value0;
    private final Value label0;
    private final Value value1;
    private final Value label1;

    public PhiInstruction(Value target, Type type, Value value0, Value label0, Value value1, Value label1)
    {
        this.target = target;
        this.opcode = "phi";
        this.type = type;
        this.value0 = value0;
        this.label0 = label0;
        this.value1 = value1;
        this.label1 = label1;
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s [%s, %s] [%s, %s]",
            this.target.llvm(),
            this.opcode,
            this.type.llvm(),
            this.value0.llvm(),
            this.label0.llvm(),
            this.value1.llvm(),
            this.label1.llvm()
        );
    }
}