package ssa;

import llvm.*;
import java.util.LinkedList;

public class PhiInstruction implements Instruction
{
    private final ssa.Register target;
    private final String opcode;
    private final String variable;
    private final LinkedList<ssa.Value> operands;

    public PhiInstruction(ssa.Register target, String variable, LinkedList<ssa.Value> operands)
    {
        this.target = target;
        this.opcode = "phi";
        this.variable = variable;
        this.operands = operands;
    }

    public PhiInstruction(ssa.Register target, String variable)
    {
        this.target = target;
        this.opcode = "phi";
        this.variable = variable;
        this.operands = new LinkedList<>();
    }

    public LinkedList<Value> getOperands()
    {
        return this.operands;
    }

    public String getVariable()
    {
        return this.variable;
    }

    public String llvm()
    {
        String phistring = String.format(
            "%s = %s %s ",
            this.target.toLLVM().llvm(),
            this.opcode,
            this.target.getType().llvm()
        );

        for (ssa.Value operand : this.operands)
        {
            //System.out.println(operand.toLLVM());
            phistring += String.format("[%s, %s]", operand.toLLVM().llvm(), "%" + operand.getBlock().getLabel());
            if(!(operand.equals(this.operands.getLast())))
            {
                phistring += ", ";
            }
        }
        return phistring;
    }

    public Value addPhiOperands(String variable, State state)
    {
        for (Edge edge : this.target.getBlock().getParents())
        {
            Block parent = edge.getSouce();
            Value operand = state.readVariable(variable, parent);
            if (operand instanceof Register) ((Register) operand).addUser(this);
            this.operands.add(operand);
        }
        return tryRemoveTrivialPhi();
    }

    public  Value tryRemoveTrivialPhi()
    {
        Value same = null;
        for (Value op : this.operands)
        {
            if (op.equals(same) || op.equals(this))
            {
                continue;
            }
            if (same != null)
            {
                return this.target;
            }
            same = op;
        }
        if (same == null)
        {
            same = new Undef(this.target.getBlock(), this.target.getType());
        }
        target.getUsers().remove(this);
        replaceBy(same); //Not finished

        for (Instruction user : target.getUsers())
        {
            if (user instanceof PhiInstruction) ((PhiInstruction) user).tryRemoveTrivialPhi();
        }

        this.target.getBlock().getPhis().remove(this);
        this.target.getBlock().getIncomplete().remove(this);

        return same;
    }

    //Not finished
    public void replaceBy(Value value)
    {
       for (Instruction user : target.getUsers())
       {
           user.replaceValue(target, value);
       }
    }

    public void replaceValue(ssa.Value oldvalue, ssa.Value newvalue)
    {
        for (Value operand : this.operands)
        {
            if (operand.toLLVM().llvm().equals(oldvalue.toLLVM().llvm()))
            {
                this.operands.set(this.operands.indexOf(operand), newvalue);
            }
        }
    }
}