package visitor;

import ast.*;
import java.io.*;

public class SemanticExpressionVisitor implements ExpressionVisitor<Type>
{
    public Type visit (BinaryExpression expression, State state)
    {
        return null;
    }

    public Type visit (DotExpression expression, State state)
    {
        return null;
    }

    public Type visit (FalseExpression expression, State state)
    {
        return null;
    }

    public Type visit (IdentifierExpression expression, State state)
    {
        return null;
    }
    
    public Type visit (InvocationExpression expression, State state)
    {
        return null;
    }
    
    public Type visit (IntegerExpression expression, State state)
    {   
        return null;
    }

    public Type visit (NewExpression expression, State state)
    {
        return null;
    }
    
    public Type visit (NullExpression expression, State state)
    {
        return null;
    }
    
    public Type visit (ReadExpression expression, State state)
    {
        return null;
    }
    
    public Type visit (TrueExpression expression, State state)
    {
        return null;
    }
    
    public Type visit (UnaryExpression expression, State state)
    {
        return null;
    }
}