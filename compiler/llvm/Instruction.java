package llvm;

public interface Instruction extends Element
{
    public String llvm();
    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue);
}