package visitor;

import ast.*;

public class ErrorType implements Type
{
    private final int linenum;

    public ErrorType(int linenum)
    {
        this.linenum = linenum;
    }

    public int getLineNum(){ return this.linenum; }
}