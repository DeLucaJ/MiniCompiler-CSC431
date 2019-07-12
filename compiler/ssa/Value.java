package ssa;

import llvm.*;

public abstract class Value implements ToLLVM
{
    private Block block;
    private Type type;

    public Value(Block block, Type type)
    {
        this.block = block;
        this.type = type;
    }

    public Block getBlock()
    {
        return this.block;
    }

    public Type getType()
    {
        return this.type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
}