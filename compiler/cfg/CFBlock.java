package cfg;

import java.util.*;

public class CFBlock
{
    private String label;
    private LinkedList<Instruction> instructions;
    private LinkedList<CFEdge> edges;   

    public CFBlock(String label)
    {
        this.label = label;
        this.instructions = new LinkedList<Instruction>();
        this.edges = new LinkedList<CFEdge>();
    }

    public String getLabel()
    {
        return this.label;
    }

    public List<CFEdge> getEdges()
    {
        return this.edges;
    }

    public List<Instruction> getInstructions()
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
        System.out.println( label + " (" + instructions.size() + " instructions):");

        //print edges
        for (CFEdge edge : edges)
        {
            System.out.print("\t\t");
            edge.printEdge();
            System.out.println("");
        }
    }
}