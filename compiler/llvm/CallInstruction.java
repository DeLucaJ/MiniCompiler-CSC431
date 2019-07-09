package llvm;

import java.util.LinkedList;

public class CallInstruction implements Instruction
{
    private final String opcode;
    private final Value target;
    private final FunctionType func;
    private final LinkedList<Value> args;
    private final String name;
    private boolean hasTarget = true;

    public CallInstruction(String name, Value target, FunctionType func, LinkedList<Value> args)
    {
        this.name = name;
        this.opcode = "call";
        this.target = target;
        this.func = func;
        this.args = args;
    }

    public CallInstruction(String name, FunctionType func, LinkedList<Value> args)
    {
        this.name = name;
        this.opcode = "call";
        this.target = null;
        this.func = func;
        this.args = args;
        this.hasTarget = false;
    }

    public String llvm()
    {
        String argString = "(";
        int i = 0;
        for (Value arg : this.args)
        {
            argString += arg.getType().llvm() + " " + arg.llvm();
            i++;
            if (i != this.args.size()) argString += ", ";
        }
        argString += ")";

        if (hasTarget)
        {
            return String.format(
                "%s = %s %s %s%s",
                this.target.llvm(),
                this.opcode,
                this.func.llvm(),
                "@" + this.name,
                argString
            );
        }
        else
        {
            return String.format(
                "%s %s %s%s",
                this.opcode,
                this.func.llvm(),
                "@" + this.name,
                argString
            );
        }
        
    }
}