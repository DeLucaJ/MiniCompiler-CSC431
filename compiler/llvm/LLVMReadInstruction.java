package llvm;

public class LLVMReadInstruction implements LLVMInstruction
{
    private LLVMIdentifier identifier;

    public LLVMReadInstruction(LLVMIdentifier identifier)
    {
        this.identifier = identifier;
    }

    public LLVMIdentifier getID()
    {
        return this.identifier;
    }

    public void setID(LLVMIdentifier identifier)
    {
        this.identifier = identifier;
    }

    public String llvm()
    {
        return String.format(
            "call i32 (i8*, ...)* @scanf(i8* getelementptr inbounds ([4 x i8]* @.read, i32 0, i32 0), i32* %s)",
            identifier.llvm()
        );
    }
}