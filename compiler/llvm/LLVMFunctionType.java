package llvm;

import java.util.LinkedList;

public class LLVMFunctionType implements LLVMType
{
    private final String name;
    private final LLVMType rettype;
    private final LinkedList<LLVMDeclaration> params;

    public LLVMFunctionType(String name, LLVMType rettype, LinkedList<LLVMDeclaration> params)
    {
        this.name = name;
        this.rettype = rettype;
        this.params = params;
    }

    public String getName()
    {
        return this.name;
    }

    public LLVMType getRetType()
    {
        return this.rettype;
    }

    public LinkedList<LLVMDeclaration> getParams()
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
            for (LLVMDeclaration param : params)
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