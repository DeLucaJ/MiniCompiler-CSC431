package visitor;

import ast.*;

public interface StatementVisitor<T>
{
    public T visit (AssignmentStatement statement);
    public T visit (BlockStatement statement);
    public T visit (ConditionalStatement statement);
    public T visit (DeleteStatement statement);
    public T visit (InvocationStatement statement);
    public T visit (PrintStatement statement);
    public T visit (PrintLnStatement statement);
    public T visit (ReturnEmptyStatement statement);
    public T visit (ReturnStatement statement);
    public T visit (WhileStatement statement);
}