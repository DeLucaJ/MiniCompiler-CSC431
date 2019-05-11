package visitor;

import ast.*;

public interface StatementVisitor<T>
{
    public T visit (AssignmentStatement statement, State state);
    public T visit (BlockStatement statement, State state);
    public T visit (ConditionalStatement statement, State state);
    public T visit (DeleteStatement statement, State state);
    public T visit (InvocationStatement statement, State state);
    public T visit (PrintStatement statement, State state);
    public T visit (PrintLnStatement statement, State state);
    public T visit (ReturnEmptyStatement statement, State state);
    public T visit (ReturnStatement statement, State state);
    public T visit (WhileStatement statement, State state);
}