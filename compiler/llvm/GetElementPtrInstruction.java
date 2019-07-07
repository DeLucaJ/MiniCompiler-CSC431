package llvm;

public class GetElementPtrInstruction implements Instruction
{
    private final String opcode;
    private final Value target;
    private final Pointer pointerType;
    private final Value pointerValue;
    private final String index;

    public GetElementPtrInstruction(Value target, Pointer pointerType, Value pointerValue, String index)
    {
        this.opcode = "getelementptr";
        this.target = target;
        this.pointerType = pointerType;
        this.pointerValue = pointerValue;
        this.index = index;
    }

    public String llvm()
    {
        return String.format(
            "%s = %s %s %s, i1 0, i32 %s",
            this.target.llvm(),
            this.opcode,
            this.pointerType.llvm(),
            this.pointerValue.llvm(),
            this.index
        );
    }
}