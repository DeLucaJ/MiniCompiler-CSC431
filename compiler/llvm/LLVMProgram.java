package llvm;

import cfg.*;
import java.util.LinkedList;

public class LLVMProgram implements LLVMElement
{
    private LinkedList<LLVMTypeDeclaration> types;
    private LinkedList<LLVMDeclaration> decls;
    private LinkedList<CFGraph> cfgs;
    //header
    //footer

    public LLVMProgram()
    {
        this.types = new LinkedList<LLVMTypeDeclaration>();
        this.decls = new LinkedList<LLVMDeclaration>();
        this.cfgs = new LinkedList<CFGraph>();
    }

    public LinkedList<LLVMTypeDeclaration> getTypeDecls()
    {
        return this.types;
    }

    public LinkedList<LLVMDeclaration> getDecls()
    {
        return this.decls;
    }

    public LinkedList<CFGraph> getFuncs()
    {
        return this.cfgs;
    }

    public String llvm()
    {
        return null;
    }
}