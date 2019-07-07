package llvm;

import java.util.LinkedList;

public class FunctionType implements Type
{
    private final String name;
    private final Type rettype;
    private final LinkedList<Declaration> params;

    public FunctionType(String name, Type rettype, LinkedList<Declaration> params)
    {
        this.name = name;
        this.rettype = rettype;
        this.params = params;
    }

    public String getName()
    {
        return this.name;
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
        /*String decl = rettype.llvm() + " (";
        if(params.size() == 1)
        {
            decl += params.getFirst().llvm();
        }
        else if(params.size() > 1)
        {
            for (Declaration param : params)
            {
                //probable error
                decl += param.getType().llvm() + ", ";
            }
        }
        decl += ")";
        return decl;*/
        return String.format("@%s", this.name);
    }
}