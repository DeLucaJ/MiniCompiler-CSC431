package llvm;

import java.util.LinkedList;

public class LLVMFunctionType implements LLVMType
{
    private LLVMType rettype;
    private LinkedList<LLVMType> params;

    public LLVMFunctionType(LLVMType rettype, LinkedList<LLVMType> params)
    {
        this.rettype = rettype;
        this.params = params;
    }

    public LLVMType getRetType()
    {
        return this.rettype;
    }

    public LinkedList<LLVMType> getParams()
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
            for (LLVMType param : params)
            {
                decl += param.llvm() + ", ";
            }
        }
        decl += ")";
        return decl;
    }
}