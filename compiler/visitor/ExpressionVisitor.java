package visitor;

import ast.*;

public interface ExpressionVisitor<T>
{
    public T visit (BinaryExpression expression, State state);
    public T visit (DotExpression expression, State state);
    public T visit (FalseExpression expression, State state);
    public T visit (IdentifierExpression expression, State state);
    public T visit (InvocationExpression expression, State state);
    public T visit (IntegerExpression expression, State state);
    public T visit (NewExpression expression, State state);
    public T visit (NullExpression expression, State state);
    public T visit (ReadExpression expression, State state);
    public T visit (TrueExpression expression, State state);
    public T visit (UnaryExpression expression, State state);
}