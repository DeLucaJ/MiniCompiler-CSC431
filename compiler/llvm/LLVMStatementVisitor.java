package llvm;

import ast.*;
import visitor.*;
import java.util.*;

public class LLVMStatementVisitor implements StatementVisitor<llvm.Block>
{
    private llvm.Function func;
    private llvm.State state;
    private LLVMExpressionVisitor expVisitor;

    public LLVMStatementVisitor(llvm.Function cfg, llvm.State state)
    { 
        this.func = cfg;
        this.state = state;
        this.expVisitor = new LLVMExpressionVisitor(this.func, this.state); 
    }

    public LLVMExpressionVisitor getExpVisitor()
    {
        return this.expVisitor;
    }

    public llvm.Block visit (AssignmentStatement statement)
    {
        // turn Lvalue into the proper expression
        Expression lvalueExp;
        if (statement.getTarget() instanceof LvalueId)
        {
            LvalueId targetId = (LvalueId) statement.getTarget();
            lvalueExp = new IdentifierExpression(statement.getLineNum(), targetId.getId());
        }
        else
        {
            LvalueDot targetDot = (LvalueDot) statement.getTarget();
            lvalueExp = new DotExpression(statement.getLineNum(), targetDot.getLeft(), targetDot.getId(), true);
        }

        // visit the expression generated
        // should always be a pointer due to semantics
        Value lvalue = lvalueExp.accept(this.expVisitor);

        // visit the source expression
        Value source = statement.getSource().accept(this.expVisitor);
        source = expVisitor.loadID(source);

        Block block = func.getBlocks().getLast();

        if (source instanceof ReadValue)
        {
            Instruction read;
            if (lvalueExp instanceof IdentifierExpression)
            {
                read = new ReadInstruction(this.expVisitor.readId);
            }
            else
            {
                //System.out.println(lvalue.llvm());
                read = new ReadInstruction(lvalue);
            }
            block.getInstructions().add(read);
            return block;
        }

        //check for void type (null)
        source = expVisitor.reasignNull(source, ((Pointer) lvalue.getType()));

        //check for i1
        Instruction store;
        if (
            source.getType() instanceof Integer1 &&
            !(((Pointer) lvalue.getType()).getPointerType() instanceof Integer1)
        )
        {
            Register newSource = new Register(new Integer32(), "u" + state.registerIndex++);
            Instruction zext = new ZextInstruction(newSource, source.getType(), source, newSource.getType());
            block.getInstructions().add(zext);
            store = new StoreInstruction(newSource.getType(), newSource, lvalue.getType(), lvalue);
        }
        else
        {
            //create the store instruction
            store = new StoreInstruction(source.getType(), source, lvalue.getType(), lvalue);
        }
        block.getInstructions().add(store);

        return block;
    }

    public llvm.Block visit (BlockStatement statement)
    {
        //Graph Construction  

        //get predecessor
        llvm.Block block = func.getBlocks().getLast();
        
        //loop through statements list (only change the statement list when necessary)
        llvm.Block last = block;
        for (Statement stmt : statement.getStatements())
        {
            last = stmt.accept(this);
        }

        //return the last block create by a statement in the list
        return last;
    }
    
    public llvm.Block visit (ConditionalStatement statement)
    {
        // get the block this begins with
        Block current = func.getBlocks().getLast();

        Value guard = statement.getGuard().accept(this.expVisitor);
        guard = expVisitor.loadID(guard);

        //create the then block and accept then statement
        Block thenBlock = new Block(func.blockLabel());
        current.addChild(thenBlock);
        func.getBlocks().add(thenBlock);
        Block thenLast = statement.getThen().accept(this);

        //create the else block and accept else statement
        Block elseBlock = new Block(func.blockLabel());
        current.addChild(elseBlock);
        func.getBlocks().add(elseBlock);
        Block elseLast = statement.getElse().accept(this);

        //create the branch instruction
        Label thenLabel = new Label(thenBlock.getLabel());
        Label elseLabel = new Label(elseBlock.getLabel());
        Instruction branch = new ConditionalBranchInstruction(guard, thenLabel, elseLabel);
        current.getInstructions().add(branch);

        // create join block
        Block joinBlock = new Block(func.blockLabel());
        Label joinLabel = new Label(joinBlock.getLabel());
        //func.getBlocks().add(joinBlock);

        // check the last instructions of thenLast and elseLast
        // if they return do not branch to join
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

        return joinBlock;
    }
    
    public llvm.Block visit (DeleteStatement statement)
    {
        Block current = func.getBlocks().getLast();

        Value expVal = statement.getExpression().accept(this.expVisitor);
        expVal = expVisitor.loadID(expVal);
        Register castReg = new Register(new Pointer(new Integer8()), "u" + state.registerIndex++);

        Instruction bitcast = new BitcastInstruction(castReg, expVal.getType(), expVal, castReg.getType());
        current.getInstructions().add(bitcast);

        LinkedList<Value> args = new LinkedList<>();
        args.add(castReg);
        Instruction free = new CallInstruction("free", state.funcs.get("free"), args);
        current.getInstructions().add(free);

        return current;
    }
    
    public llvm.Block visit (InvocationStatement statement)
    {
        Block current = func.getBlocks().getLast();

        this.expVisitor.hasTarget = false;
        statement.getExpression().accept(this.expVisitor);

        return current;
    }
    
    public llvm.Block visit (PrintStatement statement)
    {
        Block current = func.getBlocks().getLast();

        Value expVal = statement.getExpression().accept(this.expVisitor);
        expVal = expVisitor.loadID(expVal);
        Instruction print = new PrintInstruction(false, expVal);
        current.getInstructions().add(print);

        return current;
    }
    
    public llvm.Block visit (PrintLnStatement statement)
    {
        Block current = func.getBlocks().getLast();

        Value expVal = statement.getExpression().accept(this.expVisitor);
        expVal = expVisitor.loadID(expVal);
        Instruction print = new PrintInstruction(true, expVal);
        current.getInstructions().add(print);

        return current;
    }
    
    public llvm.Block visit (ReturnEmptyStatement statement)
    {
        Block current = func.getBlocks().getLast();
        current.addChild(func.getExit());

        Label retLabel = new Label(func.getExit().getLabel());
        Instruction branch = new BranchInstruction(retLabel);
        current.getInstructions().add(branch);
        current.returns();

        return current;
    }
    
    public llvm.Block visit (ReturnStatement statement)
    {
        Block current = func.getBlocks().getLast();
        current.addChild(func.getExit());

        Value expVal = statement.getExpression().accept(this.expVisitor);
        expVal = expVisitor.loadID(expVal);

        //store expVal into retval
        Pointer retValType = state.symbols.get("_retval_");
        Identifier retVal = new Identifier(retValType, "_retval_", false);

        //check for null
        expVal = expVisitor.reasignNull(expVal, retValType);
        /* if (expVal.getType() instanceof Pointer)
        {
            Pointer p = (Pointer) expVal.getType();
            if (p.getPointerType() instanceof Void)
            {
                Type lpt = retValType.getPointerType();
                expVal.setType(lpt);
            }
        } */

        //check for i1
        Instruction store;
        if (expVal.getType() instanceof Integer1 && !(retValType.getPointerType() instanceof Integer1))
        {
            Register newSource = new Register(new Integer32(), "u" + state.registerIndex++);
            Instruction zext = new ZextInstruction(newSource, expVal.getType(), expVal, newSource.getType());
            current.getInstructions().add(zext);
            store = new StoreInstruction(newSource.getType(), newSource, retValType, retVal);
        }
        else
        {
            store = new StoreInstruction(expVal.getType(), expVal, retValType, retVal);
        }
        current.getInstructions().add(store);

        Label retLabel = new Label(func.getExit().getLabel());
        Instruction branch = new BranchInstruction(retLabel);
        current.getInstructions().add(branch);
        current.returns();

        return current;
    }
    
    public llvm.Block visit (WhileStatement statement)
    {   
        Block current = func.getBlocks().getLast();

        Value guard1 = statement.getGuard().accept(this.expVisitor);
        guard1 = expVisitor.loadID(guard1);

        Block body = new Block(func.blockLabel());
        current.addChild(body);
        func.getBlocks().add(body);
        Block bodyLast = statement.getBody().accept(this);

        //evaluate guard in the bodylast loop
        Value guard2 = statement.getGuard().accept(this.expVisitor);
        guard2 = expVisitor.loadID(guard2);

        Block endloop = new Block(func.blockLabel());
        current.addChild(endloop);
        bodyLast.addChild(endloop);
        func.getBlocks().add(endloop);

        Label bodyLabel = new Label(body.getLabel());
        Label endLabel = new Label(endloop.getLabel());

        //create the conditional branch for current
        Instruction branch1 = new ConditionalBranchInstruction(guard1, bodyLabel, endLabel);
        current.getInstructions().add(branch1);

        //create the conditional branch for bodyLast
        Instruction branch2 = new ConditionalBranchInstruction(guard2, bodyLabel, endLabel);
        bodyLast.getInstructions().add(branch2);

        return endloop;
    }
}