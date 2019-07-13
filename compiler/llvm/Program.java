package llvm;

import java.util.LinkedList;

import ssa.PhiInstruction;

public class Program implements Element
{
    private LinkedList<TypeDeclaration> types;
    private LinkedList<Declaration> decls;
    private LinkedList<Function> funcs;
    //header
    //footer

    public Program()
    {
        this.types = new LinkedList<TypeDeclaration>();
        this.decls = new LinkedList<Declaration>();
        this.funcs = new LinkedList<Function>();
    }

    public LinkedList<TypeDeclaration> getTypeDecls()
    {
        return this.types;
    }

    public LinkedList<Declaration> getDecls()
    {
        return this.decls;
    }

    public LinkedList<Function> getFuncs()
    {
        return this.funcs;
    }

    public int instructionCount()
    {
        int count = 0; 

        for (Function func : this.funcs)
        {
            for (Block block : func.getBlocks())
            {
                for (PhiInstruction phi : block.getPhis())
                {
                    count++;
                }

                for (Instruction inst : block.getInstructions())
                {
                    count++;
                }
            }
        }

        return count;
    }

    public String llvm()
    {
        String typesString = "";
        for (TypeDeclaration type : types)
        {
            typesString += type.llvm() + "\n";
        }

        String declsString = "";
        for (Declaration decl : decls)
        {
            declsString += decl.llvm() + "\n";
        }

        String funcsString = "";
        for (Function func : funcs)
        {
            funcsString += func.llvm() + "\n";
        }

        String footer = "declare i8* @malloc(i32)\ndeclare void @free(i8*)\ndeclare i32 @printf(i8*, ...)\ndeclare i32 @scanf(i8*, ...)\n@.println = private unnamed_addr constant [5 x i8] c\"%ld\\0A\\00\", align 1\n@.print = private unnamed_addr constant [5 x i8] c\"%ld \\00\", align 1\n@.read = private unnamed_addr constant [4 x i8] c\"%ld\\00\", align 1\n@.read_scratch = common global i32 0, align 4";

        return String.format(
            "target triple=\"i686\"\n%s\n%s\n%s%s",
            typesString,
            declsString,
            funcsString,
            footer
        );
    }
}