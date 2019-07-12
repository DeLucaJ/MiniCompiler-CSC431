package ssa;

import llvm.*;

public class Constant extends Value
{
    private String value;

    public Constant(Block block, Type type, String value)
    {
        super(block, type);
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

    public llvm.Value toLLVM()
    {
        return new Immediate(this.getType(), this.value);
    }
}