package ssa;

import llvm.*;

public class PhiOp
{
    private final Value value;
    private final Block parent;

    public PhiOp(Value value, Block parent)
    {
        this.value = value;
        this.parent = parent;
    }

    public Value getValue() { return this.value; }
    
    public Block getParent() { return this.parent; }

    public String toString()
    {
        return String.format("[%s, %s]", value.toLLVM().llvm(), "%" + parent.getLabel());
    }
}