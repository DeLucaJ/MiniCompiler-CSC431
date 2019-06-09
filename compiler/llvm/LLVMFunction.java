package llvm;

import java.util.LinkedList;

public class LLVMFunction implements LLVMElement
{
    private String name;
    private LLVMFunctionType functype;
    private LinkedList<LLVMBlock> blocks;

    public LLVMFunction(String name, LLVMFunctionType functype)
    {
        this.name = name;
        this.functype = functype;
        this.blocks = new LinkedList<LLVMBlock>();
    }

    public String getName()
    {
        return this.name;
    }

    public LLVMFunctionType getType()
    {
        return this.functype;
    }

    public LinkedList<LLVMBlock> getBlocks()
    {
        return this.blocks;
    }

    public String llvm()
    {
        return null;
    }
}