package llvm;

import ast.*;
import visitor.*;
import java.util.*;

public class LLVMExpressionVisitor implements ExpressionVisitor<llvm.Value>
{
    private Function cfg;
    private llvm.State state;
    public int registerIndex = 0;

    public LLVMExpressionVisitor(Function cfg, llvm.State state)
    { 
        this.cfg = cfg;
        this.state = state;
    }

    public llvm.Value visit (BinaryExpression expression)
    {
        /*llvm.Value op1 = expression.getLeft().accept(this);
        llvm.Value op2 = expression.getLeft().accept(this);
        llvm.Value target = new llvm.Register(op1.getType(), "" + this.registerIndex++);

        llvm.Instruction inst = null; // might cause a problem

        switch (expression.getOperator())
        {
            case TIMES:
                inst = new llvm.MulInstruction(target, op1, op2);
                break;
            case DIVIDE:
                inst = new llvm.SdivInstruction(target, op1, op2);
                break;
            case PLUS:
                inst = new llvm.AddInstruction(target, op1, op2);
                break;
            case MINUS:
                inst = new llvm.SubInstruction(target, op1, op2);
                break;
            case LT:
                inst = new llvm.IntCompareInstruction(target, llvm.Cond.ULT, op1, op2);
                break;
            case GT:
                inst = new llvm.IntCompareInstruction(target, llvm.Cond.UGT, op1, op2);
                break;
            case LE:
                inst = new llvm.IntCompareInstruction(target, llvm.Cond.ULE, op1, op2);
                break;
            case GE:
                inst = new llvm.IntCompareInstruction(target, llvm.Cond.UGE, op1, op2);
                break;
            case EQ:
                inst = new llvm.IntCompareInstruction(target, llvm.Cond.EQ, op1, op2);
                break;
            case NE:
                inst = new llvm.IntCompareInstruction(target, llvm.Cond.NE, op1, op2);
                break;
            case AND:
                inst = new llvm.AndInstruction(target, op1, op2);
                break;
            case OR:
                inst = new llvm.OrInstruction(target, op1, op2);
                break;
        }
        this.cfg.getBlocks().getLast().getInstructions().add(inst);
        return target;*/
        return null;
    }

    public llvm.Value visit (DotExpression expression)
    {
        //There is some nonsense happening right now
        /*llvm.Value leftVal = expression.getLeft().accept(this);

        //leftVal might need to be a pointer to a structure
        llvm.Pointer pointer = (llvm.Pointer) leftVal.getType();

        llvm.Structure struct = (llvm.Structure) pointer.getPointerType();
        
        return null; //not implemented right now*/
        return null;
    }

    public llvm.Value visit (FalseExpression expression)
    {
        return new llvm.Immediate(new llvm.Integer32(), "0");
    }

    public llvm.Value visit (IdentifierExpression expression)
    {
        // checking for global might be rough
        /*if(state.symbols.containsKey(expression.getId()))
        {
            return state.symbols.get(expression.getId());
        }

        if (state.global.containsKey(expression.getId()))
        {
            return state.global.get(expression.getId());
        }

        return null; //possible problem*/
        return null;
    }

    public llvm.Value visit (InvocationExpression expression)
    {
        //figure out call stuff

        //find function type
        /*llvm.FunctionType functionType = state.funcs.get(expression.getName());

        //convert arguments
        LinkedList<llvm.Value> args = new LinkedList<llvm.Value>();
        for (Expression argument : expression.getArguments())
        {
            llvm.Value value = argument.accept(this);
            args.add(value);
        }

        //create target
        llvm.Value target = new llvm.Register(functionType.getRetType(), "" + this.registerIndex++);

        //create instruction
        llvm.Instruction inst = new llvm.CallInstruction(target, functionType, args);
        this.cfg.getBlocks().getLast().getInstructions().add(inst);

        return target;*/
        return null;
    }

    public llvm.Value visit (IntegerExpression expression)
    {
        return new llvm.Immediate(new llvm.Integer32(), expression.getValue());
    }

    public llvm.Value visit (NewExpression expression)
    {
        //create and push the call instruction
        /*llvm.Structure struct = state.structs.get(expression.getId());

        llvm.Value target1 = new llvm.Register(new llvm.Pointer(new llvm.Integer8()), "" + this.registerIndex++);

        LinkedList<llvm.Value> args = new LinkedList<llvm.Value>();
        args.add(new llvm.Immediate(new llvm.Integer32(), "" + (4 * struct.getProps().size())));

        llvm.CallInstruction call = new llvm.CallInstruction(target1, this.state.funcs.get("malloc"), args);

        this.cfg.getBlocks().getLast().getInstructions().add(call);

        //create and push the bitcast
        llvm.Value target2 = new llvm.Register(new llvm.Pointer(struct), "" + this.registerIndex++);

        llvm.BitcastInstruction bitcast = new llvm.BitcastInstruction(target2, new llvm.Pointer(new llvm.Integer8()), target1, new llvm.Pointer(struct));

        this.cfg.getBlocks().getLast().getInstructions().add(bitcast);

        return target2;*/
        return null;
    }

    public llvm.Value visit (NullExpression expression)
    {
        //the type should match the type being assigned too
        return new llvm.Immediate(new llvm.Pointer(new llvm.Void()), "null");
    }

    public llvm.Value visit (ReadExpression expression)
    {
        //how do I return a value to get this to work
        return new llvm.ReadValue();
    }

    public llvm.Value visit (TrueExpression expression)
    {
        return new llvm.Immediate(new llvm.Integer32(), "-1");
    }

    public llvm.Value visit (UnaryExpression expression)
    {
        /*llvm.Value operand = expression.getOperand().accept(this);
        llvm.Value target = new llvm.Register(operand.getType(), "" + this.registerIndex++);

        llvm.Instruction inst = null; //might cause a problem

        switch (expression.getOperator())
        {
            case NOT:
                //likely has an issue with booleans
                inst = new llvm.XorInstruction(target, operand, new llvm.Immediate(new llvm.Integer32(), "4294967295"));
                break;
            case MINUS:
                //might have some issues with signed and unsigned
                inst = new llvm.SubInstruction(target, new llvm.Immediate(new llvm.Integer32(), "0"), operand);
                break;
        }

        this.cfg.getBlocks().getLast().getInstructions().add(inst);
        return target;*/
        return null;
    }
}