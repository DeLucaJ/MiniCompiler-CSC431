package llvm; 

import java.util.*;
import ast.*;

public class Function implements Element
{
    private String label;
    private Block entry;
    private Block exit;
    private LinkedList<Block> blocks;
    private llvm.FunctionType funcType;
    private final ast.Function function;

    public Function(ast.Function function, String label, llvm.FunctionType funcType)
    {
        this.function = function;
        this.label = label;
        this.funcType = funcType;
        this.blocks = new LinkedList<Block>();
        this.entry = new Block(label + "Entry");
        this.exit = new Block(label + "Exit");
        this.blocks.add(this.entry);
    }

    public ast.Function getFunction()
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

    public LinkedList<Block> getBlocks()
    {
        return this.blocks;
    }

    public Block getEntry()
    {
        return this.entry;
    }

    public Block getExit()
    {
        return this.exit;
    }

    public llvm.FunctionType getFuncType()
    {
        return this.funcType;
    }

    public void printGraph()
    {
        //print cfg name
        System.out.println(this.label + ":");

        //print the blocks
        for (Block block : blocks)
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