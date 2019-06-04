package cfg;

import ast.*;
import visitor.*;
import java.util.*;

public class CFGraph
{
    //private String label;
    private CFBlock entry;
    private CFBlock current;
    private CFBlock exit;

    public CFGraph()
    {
        this.entry = new CFBlock();
        this.exit = new CFBlock();
        this.entry.addNext(this.exit);
        this.current = this.entry;
    }

    public CFBlock getEntry()
    {
        return this.entry;
    }

    public CFBlock getExit()
    {
        return this.exit;
    }

    public CFBlock getCurrent()
    {
        return this.current;
    }

    public void setCurrent(CFBlock block)
    {
        this.current = block;
    }

    public void insertGraph(CFGraph graph)
    {
        //place graph between current node and exit
        //attach new graph at current node
        this.current.removeNext(this.exit);
        this.current.addNext(graph.getEntry());
        //attack new graph at exit
        graph.getExit().addNext(this.exit);
    }
}