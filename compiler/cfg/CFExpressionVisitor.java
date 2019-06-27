package cfg;

import ast.*;
import visitor.*;
import llvm.*;
import java.util.*;

public class CFExpressionVisitor implements ExpressionVisitor<LLVMValue>
{
    private CFGraph cfg;
    private int registerIndex = 0;

    public CFExpressionVisitor(CFGraph cfg){ this.cfg = cfg; }


    public LLVMValue visit (BinaryExpression expression)
    {
        LLVMValue op1 = expression.getLeft().accept(this);
        LLVMValue op2 = expression.getLeft().accept(this);
        LLVMValue target = new LLVMRegister(op1.getType(), "" + this.registerIndex++);

        LLVMInstruction inst = null;

        switch (expression.getOperator())
        {
            case TIMES:
                inst = new LLVMMulInstruction(target, op1, op2);
                break;
            case DIVIDE:
                inst = new LLVMSdivInstruction(target, op1, op2);
                break;
            case PLUS:
                inst = new LLVMAddInstruction(target, op1, op2);
                break;
            case MINUS:
                inst = new LLVMSubInstruction(target, op1, op2);
                break;
            case LT:
                inst = new LLVMIntCompareInstruction(target, LLVMCond.ULT, op1, op2);
                break;
            case GT:
                inst = new LLVMIntCompareInstruction(target, LLVMCond.UGT, op1, op2);
                break;
            case LE:
                inst = new LLVMIntCompareInstruction(target, LLVMCond.ULE, op1, op2);
                break;
            case GE:
                inst = new LLVMIntCompareInstruction(target, LLVMCond.UGE, op1, op2);
                break;
            case EQ:
                inst = new LLVMIntCompareInstruction(target, LLVMCond.EQ, op1, op2);
                break;
            case NE:
                inst = new LLVMIntCompareInstruction(target, LLVMCond.NE, op1, op2);
                break;
            case AND:
                inst = new LLVMAndInstruction(target, op1, op2);
                break;
            case OR:
                inst = new LLVMOrInstruction(target, op1, op2);
                break;
        }
        this.cfg.getBlocks().getLast().getInstructions().add(inst);
        return target;
    }

    public LLVMValue visit (DotExpression expression)
    {
        return null;
    }

    public LLVMValue visit (FalseExpression expression)
    {
        return null;
    }

    public LLVMValue visit (IdentifierExpression expression)
    {
        return null;
    }

    public LLVMValue visit (InvocationExpression expression)
    {
        return null;
    }

    public LLVMValue visit (IntegerExpression expression)
    {
        return null;
    }

    public LLVMValue visit (NewExpression expression)
    {
        return null;
    }

    public LLVMValue visit (NullExpression expression)
    {
        return null;
    }

    public LLVMValue visit (ReadExpression expression)
    {
        return null;
    }

    public LLVMValue visit (TrueExpression expression)
    {
        return null;
    }

    public LLVMValue visit (UnaryExpression expression)
    {
        return null;
    }
}