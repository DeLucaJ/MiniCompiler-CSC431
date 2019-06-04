package cfg;

import ast.*;
import visitor.*;
import java.util.*;

public class CFBlock
{
    private List<Instruction> instructions;
    private List<CFBlock> next;

    public CFBlock()
    {
        this.instructions = new ArrayList<Instruction>();
        this.next = new ArrayList<CFBlock>();
    }

    public void addNext(CFBlock next)
    {
        this.next.add(next);
    }

    public void removeNext(CFBlock block)
    {
        this.next.remove(block);
    }

    public void clearNext()
    {
        this.next.clear();
    }
}