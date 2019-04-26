package visitor;

import ast.*;

public class PrintExpressionVisitor implements ExpressionVisitor<Void>
{
    public Void visit (BinaryExpression expression)
    {
        return null;
    }

    public Void visit (DotExpression expression)
    {
        return null;
    }

    public Void visit (FalseExpression expression)
    {
        return null;
    }

    public Void visit (IdentifierExpression expression)
    {
        return null;
    }
    
    public Void visit (InvocationExpression expression)
    {
        return null;
    }
    
    public Void visit (IntegerExpression expression)
    {   
        return null;
    }

    public Void visit (NewExpression expression)
    {
        return null;
    }
    
    public Void visit (NullExpression expression)
    {
        return null;
    }
    
    public Void visit (ReadExpression expression)
    {
        return null;
    }
    
    public Void visit (TrueExpression expression)
    {
        return null;
    }
    
    public Void visit (UnaryExpression expression)
    {
        return null;
    }
}