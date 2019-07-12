package llvm;

public class PrintInstruction implements Instruction
{
    private final String opcode;
    private final boolean endl;
    private Value arg;

    public PrintInstruction(boolean endl, Value arg)
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
                "%s i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.println, i32 0, i32 0), %s %s)",
                this.opcode,
                this.arg.getType().llvm(),
                this.arg.llvm()
            );
        }
        else
        {
            return String.format(
                "%s i32 (i8*, ...)* @printf(i8* getelementptr inbounds ([5 x i8]* @.print, i32 0, i32 0), %s %s)",
                this.opcode,
                this.arg.getType().llvm(),
                this.arg.llvm()
            );
        }
    }

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue)
    {
        if (arg.llvm().equals(oldvalue.toLLVM().llvm()))
        {
            arg = newvalue.toLLVM();
        }
    }
}