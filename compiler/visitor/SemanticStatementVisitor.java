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
        //handle target
        /*Type targetType;
        LValue target = statement.getTarget();
        if (target instanceof LValueDot)
        {
            Expression leftExp = target.getLeft().accept(expVisitor, state);
            Type fieldType = 
        }
        else if (target instanceof LValueId)
        {

        }
        else
        {
            //this should be impossible
        }*/

        //handle source

        //check if they match

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