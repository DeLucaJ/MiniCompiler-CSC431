package cfg;

import ast.*;
import visitor.*;
import java.util.*;

public class CFStatementVisitor implements StatementVisitor<Void>
{
    public CFGraph cfg;

    public CFStatementVisitor(CFGraph cfg){ this.cfg = cfg; }

    public Void visit (AssignmentStatement statement)
    {
        return null;
    }

    public Void visit (BlockStatement statement)
    {
        //Graph Construction
        return null;
    }
    
    public Void visit (ConditionalStatement statement)
    {
        //Graph Construction
        return null;
    }
    
    public Void visit (DeleteStatement statement)
    {
        return null;
    }
    
    public Void visit (InvocationStatement statement)
    {
        return null;
    }
    
    public Void visit (PrintStatement statement)
    {
        return null;
    }
    
    public Void visit (PrintLnStatement statement)
    {
        return null;
    }
    
    public Void visit (ReturnEmptyStatement statement)
    {
        //Graph Construction
        //Goto ExitNode
        return null;
    }
    
    public Void visit (ReturnStatement statement)
    {
        //Graph Construction
        //Goto ExitNode
        return null;
    }
    
    public Void visit (WhileStatement statement)
    {
        //Graph Construction
        return null;
    }
}