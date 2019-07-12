package ssa;

import visitor.*;

import java.util.LinkedList;

import ast.*;
import llvm.*;

public class SSAStatementVisitor implements StatementVisitor<Block>
{
    private llvm.Function func;
    private llvm.State llvmstate;
    private ssa.State ssastate;
    private SSAExpressionVisitor expVisitor;

    public SSAStatementVisitor(llvm.Function func, llvm.State llvmstate, ssa.State ssastate)
    {
        this.func = func;
        this.llvmstate = llvmstate;
        this.ssastate = ssastate;
        this.expVisitor = new SSAExpressionVisitor(func, llvmstate, ssastate);
    }

    public SSAExpressionVisitor getExpVisitor()
    {
        return this.expVisitor;
    }

    private Register checkForI1(Value source, Value lvalue, Block current)
    {
        Register newSource = null;
        if (
            source.getType() instanceof Integer1 &&
            !(((Pointer) lvalue.getType()).getPointerType() instanceof Integer1)
        )
        {
            newSource = new Register(current, new Integer32());
            Instruction zext = new ZextInstruction(newSource.toLLVM(), source.getType(), source.toLLVM(), newSource.getType());
            newSource.setDefinition(zext);
            if (source instanceof Register) ((Register) source).addUser(zext);
            current.getInstructions().add(zext);
        }
        return newSource;
    }

    //Marked for proababl error
    public Block visit (AssignmentStatement statement)
    {   
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        Value source = statement.getSource().accept(this.expVisitor);

        if (statement.getTarget() instanceof LvalueId)
        {
            LvalueId targetId = (LvalueId) statement.getTarget();

            //reassign null
            if (
                source.getType() instanceof Pointer && 
                ((Pointer) source.getType()).getPointerType() instanceof llvm.VoidType
            )
            {
                //might need to tbe the pointertype...
                llvm.Type newType = ssastate.varTypes.get(targetId.getId());
                source.setType(newType);   
            }

            //check for global
            if (ssastate.globals.contains(targetId.getId()))
            {
                //store instruction goes here
                Value global = ssastate.globals.get(targetId.getId());
                Instruction store = new StoreInstruction(source.getType(), source.toLLVM(), global.getType(), global.toLLVM());
                current.getInstructions().add(store);

                return current;
            }

            ssastate.writeVariable(targetId.getId(), current, source);
            return current;
        }

        LvalueDot targetDot = (LvalueDot) statement.getTarget();
        DotExpression lvalueExp = new DotExpression(statement.getLineNum(), targetDot.getLeft(), targetDot.getId(), true);
        
        Value lvalue = lvalueExp.accept(this.expVisitor);

        //reassign null
        if (
            source.getType() instanceof Pointer && 
            ((Pointer) source.getType()).getPointerType() instanceof llvm.VoidType
        )
        {
            llvm.Type lpt = ((Pointer) lvalue.getType()).getPointerType();
            source.setType(lpt);
        }

        //check for i1
        Register source2 = checkForI1(source, lvalue, current);

        Instruction store;
        if (source2 != null)
        {
            store = new StoreInstruction(source2.getType(), source2.toLLVM(),lvalue.getType(), lvalue.toLLVM());
            source2.addUser(store);
        }
        else
        {
            store = new StoreInstruction(source.getType(), source.toLLVM(), lvalue.getType(), lvalue.toLLVM());
            if (source instanceof Register) ((Register) source).addUser(store);;

        }
        if (lvalue instanceof Register) ((Register) lvalue).setDefinition(store);
        current.getInstructions().add(store);

        return current;
    }

    public Block visit (BlockStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        Block last = current;
        for (Statement stmt : statement.getStatements())
        {
            last = stmt.accept(this);
        }

        return last;
    }
    
    public Block visit (ConditionalStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        //handle guard stuff here
        Value guard = statement.getGuard().accept(this.expVisitor);

        Block thenBlock = new Block(func.blockLabel());
        current.addChild(thenBlock);
        ssastate.sealBlock(thenBlock);
        func.getBlocks().add(thenBlock);
        Block thenLast = statement.getThen().accept(this);

        Block elseBlock = new Block(func.blockLabel());
        current.addChild(elseBlock);
        ssastate.sealBlock(elseBlock);
        func.getBlocks().add(elseBlock);
        Block elseLast = statement.getElse().accept(this);

        Block joinBlock = new Block(func.blockLabel());

        Label thenLabel = new Label(thenBlock.getLabel());
        Label elseLabel = new Label(elseBlock.getLabel());
        Label joinLabel = new Label(joinBlock.getLabel());
        
        //create branch instruction here
        Instruction branch = new ConditionalBranchInstruction(guard.toLLVM(), thenLabel, elseLabel);
        if (guard instanceof Register) ((Register) guard).addUser(branch);
        current.getInstructions().add(branch);

        //create branch instructions for thenLast and elseLast here
        if (!thenLast.doesReturn())
        {
            thenLast.addChild(joinBlock);
            Instruction binst = new BranchInstruction(joinLabel);
            thenLast.getInstructions().add(binst);
        }

        if (!elseLast.doesReturn())
        {
            elseLast.addChild(joinBlock);
            Instruction binst = new BranchInstruction(joinLabel);
            elseLast.getInstructions().add(binst);
        }

        if (!elseLast.doesReturn() || !thenLast.doesReturn())
        {
            func.getBlocks().add(joinBlock);
        }

        ssastate.sealBlock(joinBlock);
        return joinBlock;
    }
    
    public Block visit (DeleteStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        Value expval = statement.getExpression().accept(this.expVisitor);

        Register target = new Register(current, new Pointer(new Integer8()));
        Instruction bitcast = new BitcastInstruction(target.toLLVM(), expval.getType(), expval.toLLVM(), target.getType());
        target.setDefinition(bitcast);
        if (expval instanceof Register) ((Register) expval).addUser(bitcast);
        current.getInstructions().add(bitcast);

        LinkedList<llvm.Value> args = new LinkedList<>();
        args.add(target.toLLVM());
        Instruction free = new CallInstruction("free", llvmstate.funcs.get("free"), args);
        target.addUser(free);
        current.getInstructions().add(free);

        return current;
    }
    
    public Block visit (InvocationStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        this.expVisitor.hasTarget = false;
        statement.getExpression().accept(this.expVisitor);

        return current;
    }
    
    public Block visit (PrintStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        Value expval = statement.getExpression().accept(this.expVisitor);
        Instruction print = new PrintInstruction(false, expval.toLLVM());
        current.getInstructions().add(print);
        if (expval instanceof Register) ((Register) expval).addUser(print);

        return current;
    }
    
    public Block visit (PrintLnStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        Value expval = statement.getExpression().accept(this.expVisitor);
        Instruction print = new PrintInstruction(true, expval.toLLVM());
        current.getInstructions().add(print);
        if (expval instanceof Register) ((Register) expval).addUser(print);

        return current;
    }
    
    public Block visit (ReturnEmptyStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();
        current.addChild(func.getExit());

        Label retLabel = new Label(func.getExit().getLabel());
        Instruction branch = new BranchInstruction(retLabel);
        current.getInstructions().add(branch);
        current.returns();

        return current;
    }
    
    public Block visit (ReturnStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();
        current.addChild(func.getExit());

        Value expval = statement.getExpression().accept(this.expVisitor);

        ssastate.writeVariable("_retval_", current, expval);

        //create branch to return block
        Label retLabel = new Label(func.getExit().getLabel());
        Instruction branch = new BranchInstruction(retLabel);
        current.getInstructions().add(branch);
        current.returns();

        return current;
    }
    
    public Block visit (WhileStatement statement)
    {
        // System.out.println(statement.getLineNum());
        Block current = func.getBlocks().getLast();

        //evaluate guard stuff here
        Value guard1 = statement.getGuard().accept(this.expVisitor);

        Block body = new Block(func.blockLabel());
        current.addChild(body);
        func.getBlocks().add(body);
        Block bodyLast = statement.getBody().accept(this);
        bodyLast.addChild(body);
        ssastate.sealBlock(body);

        //evaluate guard again
        Value guard2 = statement.getGuard().accept(this.expVisitor);

        Block endloop = new Block(func.blockLabel());
        current.addChild(endloop);
        bodyLast.addChild(endloop);
        ssastate.sealBlock(endloop);
        func.getBlocks().add(endloop);

        Label bodyLabel = new Label(body.getLabel());
        Label endLabel = new Label(endloop.getLabel());

        //create conditional branch that goes in current
        Instruction branch1 = new ConditionalBranchInstruction(guard1.toLLVM(), bodyLabel, endLabel);
        if (guard1 instanceof Register) ((Register) guard1).addUser(branch1);
        current.getInstructions().add(branch1);

        //create conditional branch that goes in bodyLast
        Instruction branch2 = new ConditionalBranchInstruction(guard2.toLLVM(), bodyLabel, endLabel);
        if (guard2 instanceof Register) ((Register) guard2).addUser(branch2);
        bodyLast.getInstructions().add(branch2);

        return endloop;
    }
}