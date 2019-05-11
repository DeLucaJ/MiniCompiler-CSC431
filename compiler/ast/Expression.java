package ast;

import visitor.*;

public interface Expression
{
    //Landing for visitor
    public <T> T accept (ExpressionVisitor<T> visitor, State state);
}
