package ssa;

import llvm.*;

public class Ident extends Value
{
    private final String id; 
    private final boolean global;

    public Ident(Block block, Type type, String id, boolean global)
    {
        super(block, type);
        this.id = id;
        this.global = global;
    }

    public String getID()
    {
        return this.id;
    }

    public llvm.Value toLLVM()
    {
        return new Identifier(this.getType(), this.id, this.global);
    }

    public boolean isGlobal()
    {
        return this.global;
    }
}