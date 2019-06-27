package llvm;

import java.util.ArrayList;

import ast.*;

public class LLVMUtility
{
    public static LLVMDeclaration declToLLVM(Declaration decl)
    {
        return new LLVMDeclaration(decl.getName(), astToLLVM(decl.getType()));
    }

    public static LLVMTypeDeclaration typeToLLVM(TypeDeclaration type)
    {
        ArrayList<LLVMIdentifier> props = new ArrayList<LLVMIdentifier>();
        for (Declaration field : type.getFields())
        {
            props.add(declToIdentifier(field));
        }
        return new LLVMTypeDeclaration(type.getName(), props);
    }

    public static LLVMIdentifier declToIdentifier(Declaration decl)
    {
        return new LLVMIdentifier(astToLLVM(decl.getType()), decl.getName(), false);
    }

    public static LLVMType astToLLVM(Type type)
    {
        if(type instanceof VoidType)
        {
            return new LLVMVoid();
        }
        else if(type instanceof BoolType || type instanceof IntType)
        {
            return new LLVMInteger32();
        }
        else if(type instanceof StructType)
        {   
            StructType stype = (StructType) type;
            return new LLVMStructure(stype.getName());
        }
        return null;
    }

    public static String condToString(LLVMCond cond)
    {
        switch (cond)
        {
            case EQ:
                return "eq";
            case NE:
                return "ne";
            case UGT:
                return "ugt";
            case UGE:
                return "uge";
            case ULT:
                return "ult";
            case ULE:
                return "ule";
            case SGT:
                return "sgt";
            case SGE:
                return "sge";
            case SLT:
                return "slt";
            case SLE:
                return "sle";
            default:
                return "ERROR";
        }
    }
}