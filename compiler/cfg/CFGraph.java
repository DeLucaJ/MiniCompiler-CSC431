package cfg; 

import java.util.*;
import llvm.*;
import ast.*;

public class CFGraph implements LLVMElement
{
    private String label;
    private CFBlock entry;
    private CFBlock exit;
    private LinkedList<CFBlock> blocks;
    private LLVMFunctionType funcType;
    private final Function function;

    public CFGraph(Function function, String label, LLVMFunctionType funcType)
    {
        this.function = function;
        this.label = label;
        this.funcType = funcType;
        this.blocks = new LinkedList<CFBlock>();
        this.entry = new CFBlock(label + "Entry");
        this.exit = new CFBlock(label + "Exit");
        this.blocks.add(this.entry);
    }

    public Function getFunction()
    {
        return this.function;
    }

    public String getLabel()
    {
        return this.label;
    }

    public void setLabel(String newlabel)
    {
        this.label = newlabel;
    }

    public LinkedList<CFBlock> getBlocks()
    {
        return this.blocks;
    }

    public CFBlock getEntry()
    {
        return this.entry;
    }

    public CFBlock getExit()
    {
        return this.exit;
    }

    public LLVMFunctionType getLLVMFunction()
    {
        return this.funcType;
    }

    public void printGraph()
    {
        //print cfg name
        System.out.println(this.label + ":");

        //print the blocks
        for (CFBlock block : blocks)
        {
            System.out.print("\t");
            block.printBlock();
        }
    }

    public String llvm()
    {
        return null;
    }
}