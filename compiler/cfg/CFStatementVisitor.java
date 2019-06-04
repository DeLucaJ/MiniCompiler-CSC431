package cfg;

import ast.*;
import visitor.*;
import java.util.*;

public class CFStatementVisitor
{
    public void visit (AssignmentStatement statement, CFGraph cfg)
    {
        
    }

    public void visit (BlockStatement statement, CFGraph cfg)
    {
        //Graph Construction
    }
    
    public void visit (ConditionalStatement statement, CFGraph cfg)
    {
        //Graph Construction
    }
    
    public void visit (DeleteStatement statement, CFGraph cfg)
    {

    }
    
    public void visit (InvocationStatement statement, CFGraph cfg)
    {

    }
    
    public void visit (PrintStatement statement, CFGraph cfg)
    {

    }
    
    public void visit (PrintLnStatement statement, CFGraph cfg)
    {

    }
    
    public void visit (ReturnEmptyStatement statement, CFGraph cfg)
    {
        //Graph Construction
        //Goto ExitNode
    }
    
    public void visit (ReturnStatement statement, CFGraph cfg)
    {
        //Graph Construction
        //Goto ExitNode
    }
    
    public void visit (WhileStatement statement, CFGraph cfg)
    {
        //Graph Construction
    }
}