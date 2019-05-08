package ast;

import visitor.*;

public interface Statement
{
    //Landing for Visitor
    public <T> T accept (StatementVisitor<T> visitor);
}
