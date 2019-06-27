package cfg;

import llvm.*;
import java.util.*;

public class CFBlock implements LLVMElement
{
    private String label;
    private LinkedList<LLVMInstruction> instructions;
    private LinkedList<CFEdge> edges;   

    public CFBlock(String label)
    {
        this.label = label;
        this.edges = new LinkedList<CFEdge>();
        this.instructions = new LinkedList<LLVMInstruction>();
    }

    public String getLabel()
    {
        return this.label;
    }

    public List<CFEdge> getEdges()
    {
        return this.edges;
    }

    public LinkedList<LLVMInstruction> getInstructions()
    {
        return this.instructions;
    }

    public void addEdge(CFBlock sink)
    {
        CFEdge newedge = new CFEdge(this, sink);
        this.edges.add(newedge);
    }

    public void printBlock()
    {
        //print label
        System.out.println( label + ":");

        //print edges
        for (CFEdge edge : edges)
        {
            System.out.print("\t\t");
            edge.printEdge();
            System.out.println("");
        }
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
}