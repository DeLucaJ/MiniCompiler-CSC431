package llvm;

public class ReturnVoidInstruction implements Instruction
{
    public String llvm()
    {
        return "ret void";
    }

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue){}
}