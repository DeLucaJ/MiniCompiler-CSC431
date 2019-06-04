package semantics;

import ast.*;

public class ErrorType implements Type
{
    private final int linenum;
    private final String message;

    public ErrorType(int linenum, String message)
    {
        this.linenum = linenum;
        this.message = message;
    }

    public int getLineNum(){ return this.linenum; }
    public String getMessage(){ return this.message; }
}