package visitor;

import ast.*;

public interface ExpressionVisitor<T>
{
    public T visit (BinaryExpression expression);
    public T visit (DotExpression expression);
    public T visit (FalseExpression expression);
    public T visit (IdentifierExpression expression);
    public T visit (InvocationExpression expression);
    public T visit (IntegerExpression expression);
    public T visit (NewExpression expression);
    public T visit (NullExpression expression);
    public T visit (ReadExpression expression);
    public T visit (TrueExpression expression);
    public T visit (UnaryExpression expression);
}