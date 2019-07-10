package llvm;

public class ReadInstruction implements Instruction
{
    private Value identifier;

    public ReadInstruction(Value identifier)
    {
        this.identifier = identifier;
    }

    public Value getID()
    {
        return this.identifier;
    }

    public void setID(Identifier identifier)
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