package llvm;

public class LLVMStoreInstruction implements LLVMInstruction
{
    public final String opcode;
    public final LLVMType type;
    public final LLVMValue value;
    public final LLVMPointer pointerType;
    public final LLVMValue pointerValue; 

    public LLVMStoreInstruction(LLVMType type, LLVMValue value, LLVMPointer pointerType, LLVMValue pointerValue)
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