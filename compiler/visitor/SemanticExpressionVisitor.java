package visitor;

import ast.*;
import java.io.*;

public class SemanticExpressionVisitor implements ExpressionVisitor<Type>
{
    public Type visit (BinaryExpression expression)
    {
        return null;
    }

    public Type visit (DotExpression expression)
    {
        return null;
    }

    public Type visit (FalseExpression expression)
    {
        return null;
    }

    public Type visit (IdentifierExpression expression)
    {
        return null;
    }
    
    public Type visit (InvocationExpression expression)
    {
        return null;
    }
    
    public Type visit (IntegerExpression expression)
    {   
        return null;
    }

    public Type visit (NewExpression expression)
    {
        return null;
    }
    
    public Type visit (NullExpression expression)
    {
        return null;
    }
    
    public Type visit (ReadExpression expression)
    {
        return null;
    }
    
    public Type visit (TrueExpression expression)
    {
        return null;
    }
    
    public Type visit (UnaryExpression expression)
    {
        return null;
    }
}