package visitor;

import ast.*;

public class SemanticStatementVisitor implements StatementVisitor<Void>
{
    public Void visit (AssignmentStatement statement, State state)
    {
        return null;
    }
    
    public Void visit (BlockStatement statement, State state)
    {
        return null;
    }

    public Void visit (ConditionalStatement statement, State state)
    {
        return null;
    }

    public Void visit (DeleteStatement statement, State state)
    {
        return null;
    }  
    
    public Void visit (InvocationStatement statement, State state)
    {
        return null;
    }
    
    public Void visit (PrintStatement statement, State state)
    {
        return null;
    }
    
    public Void visit (PrintLnStatement statement, State state)
    {
        return null;
    }
    
    public Void visit (ReturnEmptyStatement statement, State state)
    {
        return null;
    }
    
    public Void visit (ReturnStatement statement, State state)
    {
        return null;
    }
    
    public Void visit (WhileStatement statement, State state)
    {
        return null;
    }
}