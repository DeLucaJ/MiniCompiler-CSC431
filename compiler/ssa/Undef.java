package ssa;

import llvm.*;

public class Undef extends Value
{
    public Undef(Block block, Type type)
    {
        super(block, type);
    }

    public llvm.Value toLLVM()
    {
        return new NullValue(null);
    }
}