package llvm;

import ast.*;
import visitor.*;
import java.util.*;

public class LLVMStatementVisitor implements StatementVisitor<llvm.Block>
{
    private llvm.Function cfg;
    private llvm.State state;
    private LLVMExpressionVisitor expVisitor;
    private int index = 0;

    public LLVMStatementVisitor(llvm.Function cfg, llvm.State state)
    { 
        this.cfg = cfg;
        this.state = state;
        this.expVisitor = new LLVMExpressionVisitor(this.cfg, this.state); 
    }

    public String blockLabel()
    {
        return cfg.getLabel() + this.index++;
    }

    public llvm.Block visit (AssignmentStatement statement)
    {
        // probably need to check for a read expression
        // check for null value !!!!

        // Parse the LValues to do the proper load instructions
            // only the load for an LValueID
            // load struct get element until we get to the end of LValue Dots
        llvm.Register target = null;
        
        if (statement.getTarget() instanceof LvalueId)
        {
            LvalueId lvalue = (LvalueId) statement.getTarget();
            llvm.Identifier id = null; 
            
            if (state.symbols.contains(lvalue.getId()))
            {
                id = state.symbols.get(lvalue.getId());
            }
            else if(state.global.contains(lvalue.getId()))
            {
                id = state.global.get(lvalue.getId());
            }
            
            target = new llvm.Register(id.getType(), "" + this.expVisitor.registerIndex++);
            
            llvm.LoadInstruction inst = new llvm.LoadInstruction(target, id, new llvm.Pointer(id.getType()));
            
            this.cfg.getBlocks().getLast().getInstructions().add(inst);
        }
        else if (statement.getTarget() instanceof LvalueDot)
        {
            LvalueDot lvalue = (LvalueDot) statement.getTarget();
            
            llvm.Value leftVal = lvalue.getLeft().accept(this.expVisitor);
            
            int index = -1;

            if(!(leftVal.getType() instanceof llvm.Structure)) return null;

            llvm.Structure leftS = (llvm.Structure) leftVal.getType();
            for (int i = 0; i < leftS.getProps().size(); i++)
            {
                if (leftS.getProps().get(i).getName() == lvalue.getId())
                {
                    index = i; break;
                }
            }

            target = new llvm.Register(leftS.getProps().get(index).getType(), "" + this.expVisitor.registerIndex++);

            llvm.GetElementPtrInstruction getelement = new llvm.GetElementPtrInstruction(target, new llvm.Pointer(leftS.getProps().get(index).getType()), leftVal, "" + index);

            this.cfg.getBlocks().getLast().getInstructions().add(getelement);
        }

        // store the expression value into the pointer resolved by the LValue
            //check for read and null here
        llvm.Value expVal = statement.getSource().accept(this.expVisitor);

        llvm.StoreInstruction store = new llvm.StoreInstruction(target.getType(), target, new llvm.Pointer(expVal.getType()), expVal);

        llvm.Block block = cfg.getBlocks().getLast();
        block.getInstructions().add(store);

        return block;
    }

    public llvm.Block visit (BlockStatement statement)
    {
        //Graph Construction  

        //get predecessor
        llvm.Block block = cfg.getBlocks().getLast();
        
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
        //Graph Construction
        //get the predecessor
        llvm.Block pred = cfg.getBlocks().getLast();
        
        //create the entry node for the conditional
        llvm.Block guard = new llvm.Block(blockLabel());

        //create and edge pred -> guard
        pred.addEdge(guard);

        //add guard to blocks list
        cfg.getBlocks().add(guard);

        //GUARD ACCEPT AND PUSH GOES HERE
        //run accept on the guard
        llvm.Value guardVal = statement.getGuard().accept(expVisitor);
    
        //create then block, guard -> then, add to list, accept
        llvm.Block thenBlock = new llvm.Block(blockLabel());
        guard.addEdge(thenBlock);
        cfg.getBlocks().add(thenBlock);
        llvm.Block thenLast = statement.getThen().accept(this);

        //check if else block is empty
        llvm.Block elseBlock = null;
        llvm.Block elseLast = null;
        BlockStatement ebs = (BlockStatement) statement.getElse();
        if (ebs.getStatements().size() != 0)
        {
            //create else block, guard -> else, add to list, accept
            elseBlock = new llvm.Block(blockLabel());
            guard.addEdge(elseBlock);
            cfg.getBlocks().add(elseBlock);
            elseLast = statement.getElse().accept(this);
        }

        //create join node
        llvm.Block join = new llvm.Block(blockLabel());
        thenLast.addEdge(join);
        
        //check if elseLast is null
        llvm.Label thenLabel = new llvm.Label(thenBlock.getLabel());
        llvm.Label elseLabel = null;
        if (elseLast != null)
        {
            //if is not, elseLast -> join
            elseLast.addEdge(join);
            elseLabel = new llvm.Label(elseBlock.getLabel());
        }
        else 
        {
            //if is, guard -> join
            guard.addEdge(join);
            elseLabel = new llvm.Label(join.getLabel());
        }

        //add join to list
        cfg.getBlocks().add(join);

        llvm.Instruction branch = new llvm.ConditionalBranchInstruction(guardVal, thenLabel, elseLabel);
        guard.getInstructions().add(branch);

        return join;
    }
    
    public llvm.Block visit (DeleteStatement statement)
    {
        //accept the expression
        llvm.Value value = statement.getExpression().accept(this.expVisitor);

        //load the id being deleted
        if (!(value instanceof llvm.Identifier)) return null;

        llvm.Identifier idValue = (llvm.Identifier) value;

        llvm.Register target1 = new llvm.Register(idValue.getType(), "" + this.expVisitor.registerIndex++);

        llvm.LoadInstruction load = new llvm.LoadInstruction(target1, idValue, new llvm.Pointer(idValue.getType()));

        this.cfg.getBlocks().getLast().getInstructions().add(load);

        //bitcast the value to an int8
        llvm.Register target2 = new llvm.Register(new llvm.Integer8(), "" + this.expVisitor.registerIndex++);

        llvm.BitcastInstruction bitcast = new llvm.BitcastInstruction(target2, target2.getType(), target1, target1.getType());

        this.cfg.getBlocks().getLast().getInstructions().add(bitcast);

        //call void @free(i8* arg)
        LinkedList<llvm.Value> args = new LinkedList<>();
        args.add(idValue);

        llvm.CallInstruction free = new llvm.CallInstruction(this.state.funcs.get("free"), args);

        this.cfg.getBlocks().getLast().getInstructions().add(free);

        llvm.Block block = cfg.getBlocks().getLast();
        return block;
    }
    
    public llvm.Block visit (InvocationStatement statement)
    {
        // This is when a line just calls a function and does not assign a value

        //construct this in a similar manner to the llvm.CallInstruction but with no assignment at the end

        //leave this alone for now but will almost certainly be a problem
        llvm.Value expVal = statement.getExpression().accept(this.expVisitor);

        llvm.Block block = cfg.getBlocks().getLast();
        return block;
    }
    
    public llvm.Block visit (PrintStatement statement)
    {
        //call the printf function here just with print
        llvm.Value expVal = statement.getExpression().accept(this.expVisitor);

        llvm.PrintInstruction print = new llvm.PrintInstruction(false, expVal);
        this.cfg.getBlocks().getLast().getInstructions().add(print); 

        llvm.Block block = cfg.getBlocks().getLast();
        return block;
    }
    
    public llvm.Block visit (PrintLnStatement statement)
    {
        //call the printf function here with println
        llvm.Value expVal = statement.getExpression().accept(this.expVisitor);

        llvm.PrintInstruction println = new llvm.PrintInstruction(true, expVal);
        this.cfg.getBlocks().getLast().getInstructions().add(println);

        llvm.Block block = cfg.getBlocks().getLast();
        return block;
    }
    
    public llvm.Block visit (ReturnEmptyStatement statement)
    {
        //Graph Construction
        //get predecessor
        llvm.Block pred = cfg.getBlocks().getLast();

        //create pred -> exit
        pred.addEdge(cfg.getExit());

        //this may create problems
        //return cfg.getExit();

        //using this unless the other is necessary
        llvm.Instruction inst = new llvm.ReturnVoidInstruction();
        cfg.getBlocks().getLast().getInstructions().add(inst);

        return pred;
    }
    
    public llvm.Block visit (ReturnStatement statement)
    {
        //Graph Construction
        //get predecessor
        llvm.Block pred = cfg.getBlocks().getLast();

        //create pred -> exit
        pred.addEdge(cfg.getExit());

        //this may create problems
        //return cfg.getExit();

        //using this unless the other is necessary

        //visit expression add return instruction with the value
        llvm.Value value = statement.getExpression().accept(this.expVisitor);
        llvm.Instruction inst = new llvm.ReturnInstruction(value.getType(), value);
        this.cfg.getBlocks().getLast().getInstructions().add(inst);

        return pred;
    }
    
    public llvm.Block visit (WhileStatement statement)
    {
        //Graph Construction
        //get the predecessor 
        llvm.Block pred = cfg.getBlocks().getLast();

        //create the guard block
        llvm.Block guard = new llvm.Block(blockLabel());

        //guard instructions
        llvm.Value guardVal = statement.getGuard().accept(expVisitor);

        //add edge pred -> guard
        pred.addEdge(guard);

        //add guard to list
        cfg.getBlocks().add(guard);

        //create the body block, guard -> body, body to list, bodyLast <- accept
        llvm.Block body = new llvm.Block(blockLabel());
        guard.addEdge(body);
        cfg.getBlocks().add(body);
        llvm.Block bodyLast = statement.getBody().accept(this);

        //add bodyLast -> guard
        bodyLast.addEdge(guard);
        
        //add basic branch instruction to guard
        llvm.Label gaurdLabel = new llvm.Label(guard.getLabel());
        llvm.Instruction guardBranch = new llvm.BranchInstruction(gaurdLabel);
        bodyLast.getInstructions().add(guardBranch);

        //create endloop, guard -> endloop, endloop to list
        llvm.Block endloop = new llvm.Block(blockLabel());
        guard.addEdge(endloop);
        cfg.getBlocks().add(endloop);

        //branch instruction and so on
        llvm.Label endLabel = new llvm.Label(endloop.getLabel());
        llvm.Label bodyLabel = new llvm.Label(body.getLabel());
        llvm.Instruction loopBranch = new llvm.ConditionalBranchInstruction(guardVal, bodyLabel, endLabel);
        guard.getInstructions().add(loopBranch);

        //return endloop
        return endloop;
    }
}