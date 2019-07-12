package llvm;

public class StoreInstruction implements Instruction
{
    public final String opcode;
    public final Type type;
    public Value value;
    public final Type pointerType;
    public Value pointerValue; 

    public StoreInstruction(Type type, Value value, Type pointerType, Value pointerValue)
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

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue)
    {
        if (pointerValue.llvm().equals(oldvalue.toLLVM().llvm()))
        {
            pointerValue = newvalue.toLLVM();
        }
        if (value.llvm().equals(oldvalue.toLLVM().llvm()))
        {
            value = newvalue.toLLVM();
        }
    }
}