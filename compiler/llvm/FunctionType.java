package llvm;

import java.util.LinkedList;

public class FunctionType implements Type
{
    private final Type rettype;
    private final LinkedList<Declaration> params;

    public FunctionType(Type rettype, LinkedList<Declaration> params)
    {
        this.rettype = rettype;
        this.params = params;
    }

    public Type getRetType()
    {
        return this.rettype;
    }

    public LinkedList<Declaration> getParams()
    {
        return this.params;
    }

    public String llvm()
    {
        return this.rettype.llvm();
    }
}