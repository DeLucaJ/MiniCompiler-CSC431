package llvm;

import java.util.*;

public class Block implements Element
{
    private String label;
    private LinkedList<Instruction> instructions;
    private LinkedList<Edge> edges;   

    public Block(String label)
    {
        this.label = label;
        this.edges = new LinkedList<Edge>();
        this.instructions = new LinkedList<Instruction>();
    }

    public String getLabel()
    {
        return this.label;
    }

    public List<Edge> getEdges()
    {
        return this.edges;
    }

    public LinkedList<Instruction> getInstructions()
    {
        return this.instructions;
    }

    public void addEdge(Block sink)
    {
        Edge newedge = new Edge(this, sink);
        this.edges.add(newedge);
    }

    public void printBlock()
    {
        //print label
        System.out.println( label + ":");

        //print edges
        for (Edge edge : edges)
        {
            System.out.print("\t\t");
            edge.printEdge();
            System.out.println("");
        }
    }

    public String llvm()
    {
        String blockstring = this.label + ":\n";
        for (Instruction instruction : instructions)
        {
            blockstring += String.format("\t%s\n", instruction.llvm());
        }
        return blockstring;
    }
}