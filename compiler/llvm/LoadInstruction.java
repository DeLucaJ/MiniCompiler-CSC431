package llvm;

public class LoadInstruction implements Instruction
{
    public final String opcode;
    public final Value target;
    public final Value pointer;
    public final Pointer pointerType;

    public LoadInstruction(Value target, Value pointer, Pointer pointertype)
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
}