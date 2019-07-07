package llvm;

import java.util.LinkedList;

public class Program implements Element
{
    private LinkedList<TypeDeclaration> types;
    private LinkedList<Declaration> decls;
    private LinkedList<Function> cfgs;
    //header
    //footer

    public Program()
    {
        this.types = new LinkedList<TypeDeclaration>();
        this.decls = new LinkedList<Declaration>();
        this.cfgs = new LinkedList<Function>();
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
        return this.cfgs;
    }

    public String llvm()
    {
        return null;
    }
}