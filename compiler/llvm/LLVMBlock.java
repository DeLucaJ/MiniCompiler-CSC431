package llvm;

import java.util.LinkedList;

public class LLVMBlock implements LLVMElement
{
    private String label;
    private LinkedList<LLVMInstruction> instructions;

    public LLVMBlock(String label, LinkedList<LLVMInstruction> instructions)
    {
        this.label = label;
        this.instructions = instructions;
    }

    public String llvm()
    {
        String blockstring = this.label + ":\n";
        for (LLVMInstruction instruction : instructions)
        {
            blockstring += String.format("\t%s\n", instruction.llvm());
        }
        return blockstring;
    }

    public LinkedList<LLVMInstruction> getInstructions()
    {
        return this.instructions;
    }

    public String getLabel()
    {
        return this.label;
    }
}