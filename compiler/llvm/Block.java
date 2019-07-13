package llvm;

import java.util.*;
import ssa.*;

public class Block implements Element
{
    private String label;
    private LinkedList<Instruction> instructions;
    private LinkedList<Edge> parents;
    private LinkedList<Edge> children;
    private LinkedList<PhiInstruction> phis;
    private LinkedList<PhiInstruction> incomplete;
    private boolean sealed = false;
    private boolean returns = false;  

    public Block(String label)
    {
        this.label = label;
        this.children = new LinkedList<Edge>();
        this.parents = new LinkedList<Edge>();
        this.instructions = new LinkedList<Instruction>();
        this.phis = new LinkedList<PhiInstruction>();
        this.incomplete = new LinkedList<PhiInstruction>();
    }

    public String getLabel()
    {
        return this.label;
    }

    public LinkedList<Edge> getEdges()
    {
        return this.children;
    }

    public LinkedList<Edge> getParents()
    {
        return this.parents;
    }

    public LinkedList<Instruction> getInstructions()
    {
        return this.instructions;
    }

    public LinkedList<PhiInstruction> getPhis()
    {
        return this.phis;
    }

    public LinkedList<PhiInstruction> getIncomplete()
    {
        return this.incomplete;
    }

    public void addChild(Block sink)
    {
        Edge newedge = new Edge(this, sink);

        //preds and edges always added simultaneously
        sink.getParents().add(newedge);
        this.children.add(newedge);
    }

    public void setSealed()
    {
        this.sealed = true;
    }

    public boolean isSealed()
    {
        return this.sealed;
    }

    public void printBlock()
    {
        //print edges
        for (Edge edge : children)
        {
            System.out.print("\t\t");
            edge.printEdge();
            System.out.println("");
        }
    }

    public boolean doesReturn() { return this.returns; }

    public void returns() { this.returns = true; }

    public String llvm()
    {
        String blockstring = this.label + ":\n";
        //String blockstring = this.printLabel ? this.label + ":\n" : "";
        // System.out.println(String.format("%s: %d phis", this.label, phis.size()));

        for (PhiInstruction phi : phis)
        {
            // System.out.println(phis.indexOf(phi));
            blockstring += String.format("\t%s\n", phi.llvm());
        }

        for (Instruction instruction : instructions)
        {
            /* System.out.println(
                String.format(
                    "%20s: %s",
                    this.label + "[" + instructions.indexOf(instruction) + "]",
                    instruction
                )
            ); */
            blockstring += String.format("\t%s\n", instruction.llvm());
        }
        return blockstring;
    }
}