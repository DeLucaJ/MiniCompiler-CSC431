package visitor;

import ast.*;

public class SemanticStatementVisitor implements StatementVisitor<Void>
{
    public Void visit (AssignmentStatement statement)
    {
        return null;
    }
    
    public Void visit (BlockStatement statement)
    {
        return null;
    }

    public Void visit (ConditionalStatement statement)
    {
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
        return null;
    }
    
    public Void visit (ReturnStatement statement)
    {
        return null;
    }
    
    public Void visit (WhileStatement statement)
    {
        return null;
    }
}