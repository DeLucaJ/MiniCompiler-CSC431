package llvm;

public class LLVMPrintInstruction implements LLVMInstruction
{
    private final String opcode;
    private final boolean endl;
    private final LLVMValue arg;

    public LLVMPrintInstruction(boolean endl, LLVMValue arg)
    {
        this.opcode = "call";
        this.endl = endl;
        this.arg = arg;
    }

    public String llvm()
    {
        if(endl)
        {
            return String.format(
                "%s i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), %s %s",
                this.opcode,
                this.arg.getType().llvm(),
                this.arg.llvm()
            );
        }
        else
        {
            return String.format(
                "%s i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.print, i32 0, i32 0), %s %s",
                this.opcode,
                this.arg.getType().llvm(),
                this.arg.llvm()
            );
        }
    }
}