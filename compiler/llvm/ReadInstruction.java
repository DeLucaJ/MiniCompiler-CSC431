package llvm;

public class ReadInstruction implements Instruction
{
    private Identifier identifier;

    public ReadInstruction(Identifier identifier)
    {
        this.identifier = identifier;
    }

    public Identifier getID()
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