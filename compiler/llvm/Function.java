package llvm; 

import java.util.*;

public class Function implements Element
{
    private String label;
    private Block entry;
    private Block exit;
    private LinkedList<Block> blocks;
    private llvm.FunctionType funcType;
    private final ast.Function function;
    public int index = 0;

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

    public String blockLabel()
    {
        return this.label + this.index++;
    }

    public void close()
    {
        if (this.blocks.getLast().getEdges().size() == 0)
        {
            this.blocks.getLast().addChild(this.exit);
            Instruction branch = new BranchInstruction(new Label(this.exit.getLabel()));
            this.blocks.getLast().getInstructions().add(branch);
        }
        this.blocks.add(this.exit);
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
        String paramsString = "";
        for (int i = 0; i < this.funcType.getParams().size(); i++)
        {
            Declaration param = this.funcType.getParams().get(i);
            paramsString += String.format("%s %s", param.getType().llvm(), "%" + param.getName());
            paramsString += (i < (this.funcType.getParams().size() - 1)) ? "," : "";
        }

        String blocksString = "";
        for (Block block : this.blocks)
        {
            blocksString += block.llvm();
        }

        return String.format(
            "define %s @%s(%s)\n{\n%s}\n",
            this.funcType.getRetType().llvm(),
            this.label,
            paramsString,
            blocksString
        );
    }
}