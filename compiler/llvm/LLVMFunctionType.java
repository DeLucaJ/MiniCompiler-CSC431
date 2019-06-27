package llvm;

import java.util.LinkedList;

public class LLVMFunctionType implements LLVMType
{
    private LLVMType rettype;
    private LinkedList<LLVMDeclaration> params;

    public LLVMFunctionType(LLVMType rettype, LinkedList<LLVMDeclaration> params)
    {
        this.rettype = rettype;
        this.params = params;
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
        String decl = rettype.llvm() + " (";
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
        return decl;
    }
}