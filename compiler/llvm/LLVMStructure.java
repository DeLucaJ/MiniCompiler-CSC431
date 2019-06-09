package llvm;

import java.util.LinkedList;

public class LLVMStructure implements LLVMType
{
    public final LinkedList<LLVMType> types;

    public LLVMStructure(LinkedList<LLVMType> types)
    {
        this.types = types;
    }

    public String llvm()
    {
        String decl = "{ ";
        if (types.size() == 1)
        {
            decl += types.getFirst().llvm() + " ";
            
        }
        else if (types.size() > 1)
        {
            for (LLVMType type : types)
            {
                decl += type.llvm() + ", ";
            }
        }
        decl += "}";
        return decl;
    }
}