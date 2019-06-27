package cfg;

import ast.*;
import llvm.LLVMState;
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
        //return null;
        CFBlock block = cfg.getBlocks().getLast();
        return block;
    }

    public CFBlock visit (BlockStatement statement)
    {
        //Graph Construction
        
        //old way, create a block everytime this was entered, should have just chained
        //get the predecessor
        //this should always be the predecessor (i hope)
        //every statement should add a block to the list and then return it
        /*CFBlock pred = cfg.getBlocks().getLast();

        //create the block for this entry
        CFBlock block = new CFBlock(blockLabel());
        
        //create an edge pred -> block
        pred.addEdge(block);

        //add block to the blocks list
        cfg.getBlocks().add(block);*/   

        //new way
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

        //create then block, guard -> then, add to list, accept
        CFBlock thenBlock = new CFBlock(blockLabel());
        guard.addEdge(thenBlock);
        cfg.getBlocks().add(thenBlock);
        CFBlock thenLast = statement.getThen().accept(this);

        //check if else block is empty
        CFBlock elseBlock, elseLast = null;
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
        if (elseLast != null)
        {
            //if is not, elseLast -> join
            elseLast.addEdge(join);
        }
        else 
        {
            //if is, guard -> join
            guard.addEdge(join);
        }

        //add join to list
        cfg.getBlocks().add(join);

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
        return pred;
    }
    
    public CFBlock visit (WhileStatement statement)
    {
        //Graph Construction
        //get the predecessor 
        CFBlock pred = cfg.getBlocks().getLast();

        //create the guard block
        CFBlock guard = new CFBlock(blockLabel());

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

        //create endloop, guard -> endloop, endloop to list
        CFBlock endloop = new CFBlock(blockLabel());
        guard.addEdge(endloop);
        cfg.getBlocks().add(endloop);

        //return endloop
        return endloop;
    }
}