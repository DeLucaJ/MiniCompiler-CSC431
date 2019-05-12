package ast;

import java.util.*;

public class FunctionType implements Type
{
    private final Type retType;
    private final List<Type> paramTypes;

    public FunctionType(Type retType, List<Type> paramTypes)
    {
        this.retType = retType;
        this.paramTypes = paramTypes;
    } 

    public Type getRetType(){ return this.retType; }

    public Type getParamTypes(){ return this.paramTypes; }
}