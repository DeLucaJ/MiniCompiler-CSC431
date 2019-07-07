package llvm;

public class StoreInstruction implements Instruction
{
    public final String opcode;
    public final Type type;
    public final Value value;
    public final Pointer pointerType;
    public final Value pointerValue; 

    public StoreInstruction(Type type, Value value, Pointer pointerType, Value pointerValue)
    {
        this.type = type;
        this.value = value;
        this.pointerType = pointerType;
        this.pointerValue = pointerValue;
        this.opcode = "store";
    }

    public String llvm()
    {
        return String.format(
            "%s %s %s, %s %s",
            this.opcode,
            this.type.llvm(),
            this.value.llvm(),
            this.pointerType.llvm(),
            this.pointerValue.llvm()
        );
    }
}