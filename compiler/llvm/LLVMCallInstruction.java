package llvm;

import java.util.LinkedList;

public class LLVMCallInstruction implements LLVMInstruction
{
    private final String opcode;
    private final LLVMValue target;
    private final LLVMFunctionType func;
    private final LinkedList<LLVMValue> args;
    private boolean hasTarget = true;

    public LLVMCallInstruction(LLVMValue target, LLVMFunctionType func, LinkedList<LLVMValue> args)
    {
        this.opcode = "call";
        this.target = target;
        this.func = func;
        this.args = args;
    }

    public LLVMCallInstruction(LLVMFunctionType func, LinkedList<LLVMValue> args)
    {
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
        for (LLVMValue arg : this.args)
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
                this.func.getRetType(),
                this.func.llvm(),
                argString
            );
        }
        else
        {
            return String.format(
                "%s %s %s%s",
                this.opcode,
                this.func.getRetType(),
                this.func.llvm(),
                argString
            );
        }
        
    }
}