package cfg; 

import java.util.*;
import java.io.*;

public class CFGraph
{
    private String label;
    private CFBlock entry;
    private CFBlock exit;
    private LinkedList<CFBlock> blocks;

    public CFGraph(String label)
    {
        this.label = label;
        this.blocks = new LinkedList<CFBlock>();
        this.entry = new CFBlock(label + "Entry");
        this.exit = new CFBlock(label + "Exit");
        this.blocks.add(this.entry);
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
}