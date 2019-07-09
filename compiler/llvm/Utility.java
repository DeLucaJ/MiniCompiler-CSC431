package llvm;

import java.util.ArrayList;

import ast.*;

public class Utility
{
    public static llvm.Declaration declToLLVM(ast.Declaration decl, llvm.State state)
    {
        return new llvm.Declaration(decl.getName(), astToLLVM(decl.getType(), state));
    }

    public static llvm.TypeDeclaration typeToLLVM(ast.TypeDeclaration type, llvm.State state)
    {
        state.structs.put(type.getName(), new ArrayList<>());
        ArrayList<llvm.Identifier> fields = state.structs.get(type.getName());
        for (ast.Declaration field : type.getFields())
        {
            llvm.Identifier id = new Identifier(astToLLVM(field.getType(), state), field.getName(), false);
            fields.add(id);
        }
        return new llvm.TypeDeclaration(type.getName(), fields);
    }

    public static llvm.Type astToLLVM(ast.Type type, llvm.State state)
    {
        if(type instanceof ast.VoidType)
        {
            return new llvm.Void();
        }
        else if(type instanceof ast.BoolType || type instanceof ast.IntType)
        {
            return new llvm.Integer32();
        }
        else if(type instanceof ast.StructType)
        {   
            StructType stype = (ast.StructType) type;
            return new Pointer(new Structure(stype.getName()));
        }
        return null;
    }

    public static String condToString(llvm.Cond cond)
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