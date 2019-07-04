package cfg;

import ast.*;
import visitor.*;
import llvm.*;
import java.util.*;

public class CFExpressionVisitor implements ExpressionVisitor<LLVMValue>
{
    private CFGraph cfg;
    private LLVMState state;
    private int registerIndex = 0;

    public CFExpressionVisitor(CFGraph cfg, LLVMState state)
    { 
        this.cfg = cfg;
        this.state = state;
    }

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
        //There is some nonsense happening right now
        LLVMValue leftVal = expression.getLeft().accept(this);

        //leftVal might need to be a pointer to a structure
        if (leftVal.getType() instanceof LLVMStructure)
        {
            LLVMStructure structType = (LLVMStructure) leftVal.getType();
            ArrayList<LLVMIdentifier> ids = state.structs.get(structType.getName()).getProps();
            for (int i = 0; i < ids.size(); i++)
            {
                if(ids.get(i).getName() == expression.getId())
                {
                    //create pointer type
                    LLVMPointer pointerType = new LLVMPointer(leftVal.getType());

                    //create target of pointer type
                    LLVMValue target = new LLVMRegister(pointerType, "" + this.registerIndex++);

                    //create instruction using i as a string
                    LLVMInstruction inst = new LLVMGetElementPtrInstruction(target, pointerType, leftVal, "" + i);
                    this.cfg.getBlocks().getLast().getInstructions().add(inst);
                    return target;
                }
            }
        }
        return null;
    }

    public LLVMValue visit (FalseExpression expression)
    {
        return new LLVMImmediate(new LLVMInteger32(), "0");
    }

    public LLVMValue visit (IdentifierExpression expression)
    {
        // checking for global might be rough
        if(state.symbols.containsKey(expression.getId()))
        {
            return state.symbols.get(expression.getId());
        }

        if (state.global.containsKey(expression.getId()))
        {
            return state.global.get(expression.getId());
        }

        return null;
    }

    public LLVMValue visit (InvocationExpression expression)
    {
        //figure out call stuff

        //find function type
        LLVMFunctionType functionType = state.funcs.get(expression.getName());

        //convert arguments
        LinkedList<LLVMValue> args = new LinkedList<LLVMValue>();
        for (Expression argument : expression.getArguments())
        {
            LLVMValue value = argument.accept(this);
            args.add(value);
        }

        //create target
        LLVMValue target = new LLVMRegister(functionType.getRetType(), "" + this.registerIndex++);

        //create instruction
        LLVMInstruction inst = new LLVMCallInstruction(target, functionType, args);
        this.cfg.getBlocks().getLast().getInstructions().add(inst);

        return target;
    }

    public LLVMValue visit (IntegerExpression expression)
    {
        return new LLVMImmediate(new LLVMInteger32(), expression.getValue());
    }

    public LLVMValue visit (NewExpression expression)
    {
        //create and push the call instruction
        LLVMStructure struct = state.structs.get(expression.getId());

        LinkedList<LLVMDeclaration> params = new LinkedList<LLVMDeclaration>();
        params.add(new LLVMDeclaration("size", new LLVMInteger32()));

        LLVMFunctionType malloc = new LLVMFunctionType(new LLVMPointer(new LLVMInteger8()), params);

        LLVMValue target1 = new LLVMRegister(new LLVMPointer(new LLVMInteger8()), "" + this.registerIndex++);

        LinkedList<LLVMValue> args = new LinkedList<LLVMValue>();
        args.add(new LLVMImmediate(new LLVMInteger32(), "" + (4 * struct.getProps().size())));

        LLVMCallInstruction call = new LLVMCallInstruction(target1, malloc, args);

        this.cfg.getBlocks().getLast().getInstructions().add(call);

        //create and push the bitcast
        LLVMValue target2 = new LLVMRegister(new LLVMPointer(struct), "" + this.registerIndex++);

        LLVMBitcastInstruction bitcast = new LLVMBitcastInstruction(target2, new LLVMPointer(new LLVMInteger8()), target1, new LLVMPointer(struct));

        this.cfg.getBlocks().getLast().getInstructions().add(bitcast);

        return target2;
    }

    public LLVMValue visit (NullExpression expression)
    {
        //the type should match the type being assigned too
        return new LLVMNullValue(null);
    }

    public LLVMValue visit (ReadExpression expression)
    {
        //how do I return a value to get this to work
        return new LLVMReadValue();
    }

    public LLVMValue visit (TrueExpression expression)
    {
        return new LLVMImmediate(new LLVMInteger32(), "4294967295");
    }

    public LLVMValue visit (UnaryExpression expression)
    {
        LLVMValue operand = expression.getOperand().accept(this);
        LLVMValue target = new LLVMRegister(operand.getType(), "" + this.registerIndex++);

        LLVMInstruction inst = null;

        switch (expression.getOperator())
        {
            case NOT:
                //likely has an issue with booleans
                inst = new LLVMXorInstruction(target, operand, new LLVMImmediate(new LLVMInteger32(), "4294967295"));
                break;
            case MINUS:
                //might have some issues with signed and unsigned
                inst = new LLVMSubInstruction(target, new LLVMImmediate(new LLVMInteger32(), "0"), operand);
                break;
        }

        this.cfg.getBlocks().getLast().getInstructions().add(inst);
        return target;
    }
}