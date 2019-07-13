package ssa;

import visitor.*;

import java.util.*;

import ast.*;
import llvm.*;

public class SSAExpressionVisitor implements ExpressionVisitor<Value>
{
    private llvm.Function func;
    private llvm.State llvmstate;
    private ssa.State ssastate;
    public boolean hasTarget = true; //literally just for the invocation 
    //public Value readId; //always the last id found */

    public SSAExpressionVisitor(llvm.Function func, llvm.State llvmstate, ssa.State ssastate)
    {
        this.func = func;
        this.llvmstate = llvmstate;
        this.ssastate = ssastate;
    }

    public Value visit (BinaryExpression expression)
    {
        Value op1 = expression.getLeft().accept(this);
        Value op2 = expression.getRight().accept(this);

        Instruction binary;
        ssa.Register target;// = new ssa.Register(func.getBlocks().getLast(), op1.getType());

        switch (expression.getOperator())
        {
            case TIMES:
                target = new Register(func.getBlocks().getLast(), op1.getType());
                binary = new MulInstruction(target.toLLVM(), op1.toLLVM(), op2.toLLVM());
                break;
            case DIVIDE:
                target = new Register(func.getBlocks().getLast(), op1.getType());
                binary = new SdivInstruction(target.toLLVM(), op1.toLLVM(), op2.toLLVM());
                break;
            case PLUS:
                target = new Register(func.getBlocks().getLast(), op1.getType());
                binary = new AddInstruction(target.toLLVM(), op1.toLLVM(), op2.toLLVM());
                break;
            case MINUS:
                target = new Register(func.getBlocks().getLast(), op1.getType());
                binary = new SubInstruction(target.toLLVM(), op1.toLLVM(), op2.toLLVM());
                break;
            case LT:
                target = new Register(func.getBlocks().getLast(), new Integer1());
                binary = new IntCompareInstruction(target.toLLVM(), Cond.SLT, op1.toLLVM(), op2.toLLVM()); 
                break;
            case GT:
                target = new Register(func.getBlocks().getLast(), new Integer1());
                binary = new IntCompareInstruction(target.toLLVM(), Cond.SGT, op1.toLLVM(), op2.toLLVM());
                break;
            case LE:
                target = new Register(func.getBlocks().getLast(), new Integer1());
                binary = new IntCompareInstruction(target.toLLVM(), Cond.SLE, op1.toLLVM(), op2.toLLVM());
                break;
            case GE:
                target = new Register(func.getBlocks().getLast(), new Integer1());;
                binary = new IntCompareInstruction(target.toLLVM(), Cond.SGE, op1.toLLVM(), op2.toLLVM());
                break;
            case EQ:
                target = new Register(func.getBlocks().getLast(), new Integer1());
                binary = new IntCompareInstruction(target.toLLVM(), Cond.EQ, op1.toLLVM(), op2.toLLVM());
                break;
            case NE:
                target = new Register(func.getBlocks().getLast(), new Integer1());
                binary = new IntCompareInstruction(target.toLLVM(), Cond.NE, op1.toLLVM(), op2.toLLVM());
                break;
            case AND:
                target = new Register(func.getBlocks().getLast(), op1.getType());
                binary = new AndInstruction(target.toLLVM(), op1.toLLVM(), op2.toLLVM());
                break;
            default: //OR
                target = new Register(func.getBlocks().getLast(), op1.getType());
                binary = new OrInstruction(target.toLLVM(), op1.toLLVM(), op2.toLLVM());
                break;
        }
        target.setDefinition(binary);
        func.getBlocks().getLast().getInstructions().add(binary);

        if (op1 instanceof Register) ((Register) op1).addUser(binary);
        if (op2 instanceof Register) ((Register) op2).addUser(binary);

        return target;
    }

    public Value visit (DotExpression expression)
    {
        //System.out.println(expression.getLineNum());
        Block current = func.getBlocks().getLast();

        Value leftVal = expression.getLeft().accept(this); //the way identifiers are being written too is not working

        //This should still work in ssa
        Pointer pointer = (Pointer) leftVal.getType();

        //again, hoping this still works in ssa
        Structure struct = (Structure) pointer.getPointerType();
        ArrayList<Identifier> fields = llvmstate.structs.get(struct.getName());

        Register target1 = null;
        int index = 0;
        Instruction getElement = null;
        for (int i = 0; i < fields.size(); i++)
        {
            Identifier id = fields.get(i);
            if (id.getName().equals(expression.getId()))
            {
                index = i;
                target1 = new Register(current, new Pointer(id.getType()));
                getElement = new GetElementPtrInstruction(target1.toLLVM(), pointer, leftVal.toLLVM(), "" + index);
                target1.setDefinition(getElement);
                if (leftVal instanceof Register) ((Register) leftVal).addUser(getElement);
            }
        }

        current.getInstructions().add(getElement);

        if(!expression.isSource())
        {
            llvm.Type pointerType = ((Pointer) target1.getType()).getPointerType();
            Register target2 = new Register(current, pointerType);

            Instruction load = new LoadInstruction(target2.toLLVM(), target1.toLLVM(), target1.getType());
            target2.setDefinition(load);
            target1.addUser(load);

            current.getInstructions().add(load);

            return target2;
        }

        return target1;
    }

    public Value visit (FalseExpression expressione)
    {
        return new Constant(func.getBlocks().getLast(), new Integer1(), "false");
    }

    public Value visit (IdentifierExpression expression)
    {
        Block current = func.getBlocks().getLast();
        /* if (ssastate.globals.contains(expression.getId()))
        {
            return ssastate.globals.get(expression.getId());
        } */
        // System.out.println("IDEX: " + expression.getId() + " in globals? " + ssastate.globals.containsKey(expression.getId()));

        if (
                !ssastate.varTypes.containsKey(expression.getId()) &&
                ssastate.globals.containsKey(expression.getId())
        )
        {
            // System.out.println("Global Variable " + expression.getId());
            //load the variable into a register and write the variable in the current block as a this
            //globals should always be pointers
            Value global = ssastate.globals.get(expression.getId());
            Register loadreg = new Register(current, ((Pointer) global.getType()).getPointerType());
            Instruction load = new LoadInstruction(loadreg.toLLVM(), global.toLLVM(), global.getType());
            current.getInstructions().add(load);
            loadreg.setDefinition(load);
            
            return loadreg;
        }
        Value value = ssastate.readVariable(expression.getId(), current);

        SSAStatementVisitor.reassignNull(value, ssastate.varTypes.get(expression.getId()));

        //if variable is not defined within the function
        return value;
    }

    public Value visit (InvocationExpression expression)
    {
        Block current = func.getBlocks().getLast();

        LinkedList<llvm.Value> args = new LinkedList<>();
        LinkedList<ssa.Value> argssa = new LinkedList<>();
        boolean isStmt = !this.hasTarget;
        this.hasTarget = true;

        llvm.FunctionType function = llvmstate.funcs.get(expression.getName());

        for (Expression argument : expression.getArguments())
        {
            Value arg = argument.accept(this);
            
            int index = args.size();
            if (function.getParams().get(index).getType() instanceof Pointer)
            {
                SSAStatementVisitor.reassignNull(arg, (Pointer) function.getParams().get(index).getType());
            }
            args.add(arg.toLLVM());
            argssa.add(arg);
        }

        Register target = null; 

        Instruction call;
        if (isStmt)
        {
            call = new CallInstruction(expression.getName(), function, args);
        }
        else
        {
            target = new Register(current, function.getRetType());
            call = new CallInstruction(expression.getName(), target.toLLVM(), function, args);
        }

        //if target is used, set as definition
        if (target != null) target.setDefinition(call);
        
        //for each arg, set call as a user
        for (ssa.Value arg : argssa)
        {
            if (arg instanceof Register) ((Register) arg).addUser(call);
        }
        current.getInstructions().add(call);

        return target;
    }

    public Value visit (IntegerExpression expression)
    {
        return new Constant(func.getBlocks().getLast(), new Integer32(), expression.getValue());
    }

    public Value visit (NewExpression expression)
    {
        Block current = func.getBlocks().getLast();
        int numargs = llvmstate.structs.get(expression.getId()).size();

        Pointer structPointer = new Pointer(new Structure(expression.getId()));

        Register target1 = new Register(current, new Pointer(new Integer8()));
        llvm.FunctionType malloc = llvmstate.funcs.get("malloc");
        LinkedList<llvm.Value> args = new LinkedList<>();
        Immediate size = new Immediate(new Integer32(), "" + (4 * numargs));
        args.add(size);
        Instruction call = new CallInstruction("malloc", target1.toLLVM(), malloc, args);
        target1.setDefinition(call);

        //if (target1.getType() == null) System.out.println(expression.getLineNum());

        Register target2 = new Register(current, structPointer);
        Instruction bitcast = new BitcastInstruction(target2.toLLVM(), target1.getType(), target1.toLLVM(), structPointer);
        target1.addUser(bitcast);
        target2.setDefinition(bitcast);

        current.getInstructions().add(call);
        current.getInstructions().add(bitcast);

        return target2;
    }

    public Value visit (NullExpression expression)
    {
        return new Constant(func.getBlocks().getLast(), new Pointer(new llvm.VoidType()), "null");
    }

    public Value visit (ReadExpression expression)
    {
        //do the read instruciton here since it always needs the same id
        Identifier read_scratch = new Identifier(new Pointer(new Integer32()), ".read_scratch", true);
        Instruction read = new ReadInstruction(read_scratch);
        func.getBlocks().getLast().getInstructions().add(read);

        //nead to load id into a register
        Register readreg = new ssa.Register(func.getBlocks().getLast(), ((Pointer)read_scratch.getType()).getPointerType());
        Instruction load = new LoadInstruction(readreg.toLLVM(), read_scratch, read_scratch.getType());
        func.getBlocks().getLast().getInstructions().add(load);
        readreg.setDefinition(load);

        return readreg;
    }

    public Value visit (TrueExpression expression)
    {
        return new Constant(func.getBlocks().getLast(), new Integer1(), "true");
    }

    public Value visit (UnaryExpression expression)
    {
        Value op1 = expression.getOperand().accept(this);

        Instruction unary;
        Register target = new Register(func.getBlocks().getLast(), op1.getType());
        switch (expression.getOperator())
        {
            case NOT:
                unary = new XorInstruction(target.toLLVM(), op1.toLLVM(), new Immediate(new Integer32(), "-1"));
                break;
            default: //MINUS
                unary = new SubInstruction(target.toLLVM(), new Immediate(new Integer32(), "0"), op1.toLLVM());
                break;
        }
        target.setDefinition(unary);
        if (op1 instanceof Register) ((Register) op1).addUser(unary);
        func.getBlocks().getLast().getInstructions().add(unary);

        return target;
    }
}