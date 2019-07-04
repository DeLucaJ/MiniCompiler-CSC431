package cfg;

import ast.*;
import llvm.*;
import visitor.*;
import java.util.*;

public class CFStatementVisitor implements StatementVisitor<CFBlock>
{
    private CFGraph cfg;
    private LLVMState state;
    private CFExpressionVisitor expVisitor;
    private int index = 0;

    public CFStatementVisitor(CFGraph cfg, LLVMState state)
    { 
        this.cfg = cfg;
        this.state = state;
        this.expVisitor = new CFExpressionVisitor(this.cfg, this.state); 
    }

    public String blockLabel()
    {
        return cfg.getLabel() + this.index++;
    }

    public CFBlock visit (AssignmentStatement statement)
    {
        // probably need to check for a read expression\

        // check for null value

        //return null;
        CFBlock block = cfg.getBlocks().getLast();
        return block;
    }

    public CFBlock visit (BlockStatement statement)
    {
        //Graph Construction  

        //get predecessor
        CFBlock block = cfg.getBlocks().getLast();
        
        //loop through statements list (only change the statement list when necessary)
        CFBlock last = block;
        for (Statement stmt : statement.getStatements())
        {
            last = stmt.accept(this);
        }

        //return the last block create by a statement in the list
        return last;
    }
    
    public CFBlock visit (ConditionalStatement statement)
    {
        //Graph Construction
        //get the predecessor
        CFBlock pred = cfg.getBlocks().getLast();
        
        //create the entry node for the conditional
        CFBlock guard = new CFBlock(blockLabel());

        //create and edge pred -> guard
        pred.addEdge(guard);

        //add guard to blocks list
        cfg.getBlocks().add(guard);

        //GUARD ACCEPT AND PUSH GOES HERE
        //run accept on the guard
        LLVMValue guardVal = statement.getGuard().accept(expVisitor);
    
        //create then block, guard -> then, add to list, accept
        CFBlock thenBlock = new CFBlock(blockLabel());
        guard.addEdge(thenBlock);
        cfg.getBlocks().add(thenBlock);
        CFBlock thenLast = statement.getThen().accept(this);

        //check if else block is empty
        CFBlock elseBlock = null;
        CFBlock elseLast = null;
        BlockStatement ebs = (BlockStatement) statement.getElse();
        if (ebs.getStatements().size() != 0)
        {
            //create else block, guard -> else, add to list, accept
            elseBlock = new CFBlock(blockLabel());
            guard.addEdge(elseBlock);
            cfg.getBlocks().add(elseBlock);
            elseLast = statement.getElse().accept(this);
        }

        //create join node
        CFBlock join = new CFBlock(blockLabel());
        thenLast.addEdge(join);
        
        //check if elseLast is null
        LLVMLabel thenLabel = new LLVMLabel(thenBlock.getLabel());
        LLVMLabel elseLabel = null;
        if (elseLast != null)
        {
            //if is not, elseLast -> join
            elseLast.addEdge(join);
            elseLabel = new LLVMLabel(elseBlock.getLabel());
        }
        else 
        {
            //if is, guard -> join
            guard.addEdge(join);
            elseLabel = new LLVMLabel(join.getLabel());
        }

        //add join to list
        cfg.getBlocks().add(join);

        LLVMInstruction branch = new LLVMConditionalBranchInstruction(guardVal, thenLabel, elseLabel);
        guard.getInstructions().add(branch);

        return join;
    }
    
    public CFBlock visit (DeleteStatement statement)
    {
        //return null;
        CFBlock block = cfg.getBlocks().getLast();
        return block;
    }
    
    public CFBlock visit (InvocationStatement statement)
    {
        //return null;
        CFBlock block = cfg.getBlocks().getLast();
        return block;
    }
    
    public CFBlock visit (PrintStatement statement)
    {
        //return null;
        CFBlock block = cfg.getBlocks().getLast();
        return block;
    }
    
    public CFBlock visit (PrintLnStatement statement)
    {
        //return null;
        CFBlock block = cfg.getBlocks().getLast();
        return block;
    }
    
    public CFBlock visit (ReturnEmptyStatement statement)
    {
        //Graph Construction
        //get predecessor
        CFBlock pred = cfg.getBlocks().getLast();

        //create pred -> exit
        pred.addEdge(cfg.getExit());

        //this may create problems
        //return cfg.getExit();

        //using this unless the other is necessary
        LLVMInstruction inst = new LLVMReturnVoidInstruction();
        cfg.getBlocks().getLast().getInstructions().add(inst);

        return pred;
    }
    
    public CFBlock visit (ReturnStatement statement)
    {
        //Graph Construction
        //get predecessor
        CFBlock pred = cfg.getBlocks().getLast();

        //create pred -> exit
        pred.addEdge(cfg.getExit());

        //this may create problems
        //return cfg.getExit();

        //using this unless the other is necessary

        //visit expression add return instruction with the value
        LLVMValue value = statement.getExpression().accept(this.expVisitor);
        LLVMInstruction inst = new LLVMReturnInstruction(value.getType(), value);
        this.cfg.getBlocks().getLast().getInstructions().add(inst);

        return pred;
    }
    
    public CFBlock visit (WhileStatement statement)
    {
        //Graph Construction
        //get the predecessor 
        CFBlock pred = cfg.getBlocks().getLast();

        //create the guard block
        CFBlock guard = new CFBlock(blockLabel());

        //guard instructions
        LLVMValue guardVal = statement.getGuard().accept(expVisitor);

        //add edge pred -> guard
        pred.addEdge(guard);

        //add guard to list
        cfg.getBlocks().add(guard);

        //create the body block, guard -> body, body to list, bodyLast <- accept
        CFBlock body = new CFBlock(blockLabel());
        guard.addEdge(body);
        cfg.getBlocks().add(body);
        CFBlock bodyLast = statement.getBody().accept(this);

        //add bodyLast -> guard
        bodyLast.addEdge(guard);
        
        //add basic branch instruction to guard
        LLVMLabel gaurdLabel = new LLVMLabel(guard.getLabel());
        LLVMInstruction guardBranch = new LLVMBranchInstruction(gaurdLabel);
        bodyLast.getInstructions().add(guardBranch);

        //create endloop, guard -> endloop, endloop to list
        CFBlock endloop = new CFBlock(blockLabel());
        guard.addEdge(endloop);
        cfg.getBlocks().add(endloop);

        //branch instruction and so on
        LLVMLabel endLabel = new LLVMLabel(endloop.getLabel());
        LLVMLabel bodyLabel = new LLVMLabel(body.getLabel());
        LLVMInstruction loopBranch = new LLVMConditionalBranchInstruction(guardVal, bodyLabel, endLabel);
        guard.getInstructions().add(loopBranch);

        //return endloop
        return endloop;
    }
}