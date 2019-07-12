package llvm;

public class LoadInstruction implements Instruction
{
    public final String opcode;
    public final Value target;
    public Value pointer;
    public final Type pointerType;

    public LoadInstruction(Value target, Value pointer, Type pointertype)
    {
        this.target = target;
        this.pointer = pointer;
        this.pointerType = pointertype;
        this.opcode = "load";
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s",
            this.target.llvm(),
            this.opcode,
            this.pointerType.llvm(),
            this.pointer.llvm()
        );
    }

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue)
    {
        if (pointer.llvm().equals(oldvalue.toLLVM().llvm()))
        {
            pointer = newvalue.toLLVM();
        }
    }
}