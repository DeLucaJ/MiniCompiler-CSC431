package visitor;

import ast.*;

public class SemanticStatementVisitor implements StatementVisitor<Void>
{
    private SemanticExpressionVisitor expVisitor;

    public SemanticStatementVisitor()
    {
        this.expVisitor = new SemanticExpressionVisitor();
    }

    public Void visit (AssignmentStatement statement, State state)
    {
        //evalueate Lvalue of target

        //evaluate Expression of source

        //compare their types

        return null;
    }
    
    public Void visit (BlockStatement statement, State state)
    {
        //loop through all of the statements in statements

        return null;
    }

    public Void visit (ConditionalStatement statement, State state)
    {
        //Evaluate guard

        //Evaluate thenBlock

        //Evaluate elseBlock

        return null;
    }

    public Void visit (DeleteStatement statement, State state)
    {
        // Evaluate expression

        return null;
    }  
    
    public Void visit (InvocationStatement statement, State state)
    {
        // Evaluate expression

        return null;
    }
    
    public Void visit (PrintStatement statement, State state)
    {
        //evaluate expression

        return null;
    }
    
    public Void visit (PrintLnStatement statement, State state)
    {
        //evaluate expression

        return null;
    }
    
    public Void visit (ReturnEmptyStatement statement, State state)
    {
        // make sure current func returns void/null
        
        return null;
    }
    
    public Void visit (ReturnStatement statement, State state)
    {
        //evaluate expression

        //compare to retType of current Func

        return null;
    }
    
    public Void visit (WhileStatement statement, State state)
    {
        //evaluate guard

        //evaluate body

        return null;
    }
}