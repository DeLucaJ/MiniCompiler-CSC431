package llvm;

import ast.*;
import visitor.*;
import java.util.*;

public class LLVMExpressionVisitor implements ExpressionVisitor<llvm.Value>
{
    private Function func;
    private llvm.State state;
    public boolean hasTarget = true; //literally just for the invocation 
    public Identifier readId; //always the last id found

    public LLVMExpressionVisitor(Function func, llvm.State state)
    { 
        this.func = func;
        this.state = state;
    }

    public llvm.Value visit (BinaryExpression expression)
    {
        Value op1 = expression.getLeft().accept(this);
        op1 = loadID(op1);
        Value op2 = expression.getRight().accept(this);
        op2 = loadID(op2);

        Instruction inst;
        //Register target = new Register(new Integer32(), "u" + state.registerIndex++);
        Register target = new Register(op1.getType(), "u" + state.registerIndex++);

        switch (expression.getOperator())
        {
            case TIMES:
                inst = new MulInstruction(target, op1, op2);
                break;
            case DIVIDE:
                inst = new SdivInstruction(target, op1, op2);
                break;
            case PLUS:
                inst = new AddInstruction(target, op1, op2);
                break;
            case MINUS:
                inst = new SubInstruction(target, op1, op2);
                break;
            case LT:
                target.setType(new Integer1());
                //System.out.println(target.getType().llvm() + " " + expression.getLineNum());
                inst = new IntCompareInstruction(target, Cond.SLT, op1, op2); 
                break;
            case GT:
                target.setType(new Integer1());
                //System.out.println(target.getType().llvm() + " " + expression.getLineNum());
                inst = new IntCompareInstruction(target, Cond.SGT, op1, op2);
                break;
            case LE:
                target.setType(new Integer1());
                //System.out.println(target.getType().llvm() + " " + expression.getLineNum());
                inst = new IntCompareInstruction(target, Cond.SLE, op1, op2);
                break;
            case GE:
                target.setType(new Integer1());
                //System.out.println(target.getType().llvm() + " " + expression.getLineNum());
                inst = new IntCompareInstruction(target, Cond.SGE, op1, op2);
                break;
            case EQ:
                //Must be a structure or an integer
                //Shouldn't need to type check because of semantics
                target.setType(new Integer1());
                inst = new IntCompareInstruction(target, Cond.EQ, op1, op2);
                break;
            case NE:
                //Must be a structure or an integer
                //Shouldn't need to type check because of semantics
                target.setType(new Integer1());
                //System.out.println(target.getType().llvm() + " " + expression.getLineNum());
                inst = new IntCompareInstruction(target, Cond.NE, op1, op2);
                break;
            case AND:
                inst = new AndInstruction(target, op1, op2);
                break;
            default: //OR
                inst = new OrInstruction(target, op1, op2);
                break;
        }

        func.getBlocks().getLast().getInstructions().add(inst);

        //check for int Compare instruction
        //need to zextend to int32

        return target;
    }

    public llvm.Value visit (DotExpression expression)
    {
        Value leftVal = expression.getLeft().accept(this);
        leftVal = loadID(leftVal);

        //determine which value in the struct we are getting
        //the left expression should always evaluate to an identifier
        Pointer pointer = (Pointer) leftVal.getType();

        Structure struct = (Structure) pointer.getPointerType();
        ArrayList<Identifier> fields = state.structs.get(struct.getName());

        Register target1 = null;
        int index = 0;
        Instruction getElement = null;
        for (int i = 0; i < fields.size(); i++)
        {
            Identifier id = fields.get(i);
            if (id.getName().equals(expression.getId()))
            {
                index = i;
                target1 = new Register(new Pointer(id.getType()), "u" + state.registerIndex++); //just changed
                getElement = new GetElementPtrInstruction(target1, pointer, leftVal, "" + index);
            }
        }

        func.getBlocks().getLast().getInstructions().add(getElement);
        
        if(!expression.isSource())
        {
            Type pointerType = ((Pointer) target1.getType()).getPointerType();
            Register target2 = new Register(pointerType, "u" + state.registerIndex++);
            //Register target2 = new Register(target1.getType(), "u" + state.registerIndex++);

            Instruction load = new LoadInstruction(target2, target1, target1.getType());

            func.getBlocks().getLast().getInstructions().add(load);

            return target2;
        }

        return target1;
    }

    public llvm.Value visit (FalseExpression expression)
    {
        //might need to zextend false constant
        //return new llvm.Immediate(new llvm.Integer32(), "0");
        return new llvm.Immediate(new llvm.Integer1(), "false");
    }

    public Value loadID(Value value)
    {
        if (value instanceof Identifier)
        {
            Register target = new Register(((Pointer) value.getType()).getPointerType(), "u" + state.registerIndex++);

            Instruction load = new LoadInstruction(target, value, value.getType());
            func.getBlocks().getLast().getInstructions().add(load);

            return target;
        }
        /* else if (value.getType() instanceof Pointer)
        {
            Pointer type = (Pointer) value.getType();
            Register target = new Register(type.getPointerType(), "u" + state.registerIndex++);

            Instruction load = new LoadInstruction(target, value, type);
            func.getBlocks().getLast().getInstructions().add(load);

            return target;
        } */

        return value;
    }

    public llvm.Value visit (IdentifierExpression expression)
    {
        Pointer pointer;
        Identifier id;
        if (state.symbols.containsKey(expression.getId()))
        {
            pointer = state.symbols.get(expression.getId());
            id = new Identifier(pointer, expression.getId(), false);
        }
        else if (state.params.containsKey(expression.getId()))
        {
            pointer = state.params.get(expression.getId());
            id = new Identifier(pointer, "_P_" + expression.getId(), false);
        }
        else
        {
            pointer = state.global.get(expression.getId());
            id = new Identifier(pointer, expression.getId(), true);
        }

        this.readId = id;

        return id;
    }

    public Value reasignNull(Value v, Pointer p)
    {
        if (v.getType() instanceof Pointer)
        {
            Pointer vp = (Pointer) v.getType();
            if (vp.getPointerType() instanceof Void)
            {
                Type lpt = p.getPointerType();
                v.setType(lpt);
            }
        }
        
        return v;
    } 

    public llvm.Value visit (InvocationExpression expression)
    {   
        LinkedList<Value> args = new LinkedList<>();
        boolean isStmt = !this.hasTarget;
        this.hasTarget = true;

        FunctionType function = state.funcs.get(expression.getName());

        for (Expression argument : expression.getArguments())
        {
            Value arg = argument.accept(this);
            arg = loadID(arg);
            int index = args.size();
            if (function.getParams().get(index).getType() instanceof Pointer)
            {
                if (arg.getType() instanceof Pointer)
                {
                    Pointer argp = (Pointer) arg.getType();
                    if (argp.getPointerType() instanceof Void)
                    {
                        Type lpt = (Pointer) function.getParams().get(index).getType();
                        arg.setType(lpt);
                    }
                }
            }
            args.add(arg);
        }
        
        Register target = new Register(function.getRetType(), "u" + state.registerIndex++);

        Instruction call;
        if(isStmt)
        {
            call = new CallInstruction(expression.getName(), function, args);
        }
        else
        {
            call = new CallInstruction(expression.getName(), target, function, args);
        }
        func.getBlocks().getLast().getInstructions().add(call);

        return target;
    }

    public llvm.Value visit (IntegerExpression expression)
    {
        return new llvm.Immediate(new llvm.Integer32(), expression.getValue());
    }

    public llvm.Value visit (NewExpression expression)
    {
        int numargs = state.structs.get(expression.getId()).size();

        Pointer p = new Pointer(new Structure(expression.getId()));
        Register target1 = new Register(p, "u" + state.registerIndex++);
        FunctionType malloc = state.funcs.get("malloc");
        LinkedList<Value> args = new LinkedList<>();
        Immediate size = new Immediate(new Integer32(), "" + (4 * numargs));
        args.add(size);
        Instruction call = new CallInstruction("malloc", target1, malloc, args);

        Register target2 = new Register(p, "u" + state.registerIndex++);
        Instruction bitcast = new BitcastInstruction(target2, new Pointer(new Integer8()), target1, p);

        func.getBlocks().getLast().getInstructions().add(call);
        func.getBlocks().getLast().getInstructions().add(bitcast);

        return target2;
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
        //return new llvm.Immediate(new llvm.Integer32(), "-1");
        return new llvm.Immediate(new llvm.Integer1(), "true");
    }

    public llvm.Value visit (UnaryExpression expression)
    {
        Value op1 = expression.getOperand().accept(this);
        op1 = loadID(op1);

        Instruction inst;
        Register target = new Register(op1.getType(), "u" + state.registerIndex++);
        switch (expression.getOperator())
        {
            case NOT:
                inst = new XorInstruction(target, op1, new Immediate(new Integer32(), "-1"));
                break;
            default: //MINUS
                inst = new SubInstruction(target, new Immediate(new Integer32(), "0"), op1);
                break;
        }

        func.getBlocks().getLast().getInstructions().add(inst);
        return target;
    }
}