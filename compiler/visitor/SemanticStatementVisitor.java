package visitor;

import ast.*;
import java.lang.*;
import java.util.*;

public class SemanticStatementVisitor implements StatementVisitor<Void>
{
    private SemanticExpressionVisitor expVisitor;

    public SemanticStatementVisitor()
    {
        this.expVisitor = new SemanticExpressionVisitor();
    }

    public Void visit (AssignmentStatement statement, State state)
    {
        /*LvalueDot dotTarget;
        LvalueId idTarget;
        Type sourceType = statement.getSource().accept(expVisitor, state);

        //evalueate Lvalue of target
        if (statement.getTarget() instanceof LvalueDot)
        {
            dotTarget = target;
            //Need to fix getType() for finding structs
        }
        else //instance of LvalueId
        {
            idTarget = target;
            Type targetType = state.getType(idTarget.getId());
            if (!targetType.getClass().equals(sourceType.getClass()))
            {
                System.out.println("Type Error (l:%d): Assignment Statement", statement.getLineNum());
            }
        }*/

        //compare their types

        return null;
    }
    
    public Void visit (BlockStatement statement, State state)
    {
        //loop through all of the statements in statements
        List<Statement> body = statement.getStatements();
        body.forEach(
            (stmt) -> { stmt.accept(this, state); }
        );
        return null;
    }

    public Void visit (ConditionalStatement statement, State state)
    {
        //Evaluate guard
        Type guardType = statement.getGuard().accept(expVisitor, state);
        if (!(guardType instanceof BoolType))
        {
            System.out.printf("Type Error (l:%d): Conditional Statement\n", statement.getLineNum());
        }

        statement.getThen().accept(this, state);
        statement.getElse().accept(this, state);

        return null;
    }

    public Void visit (DeleteStatement statement, State state)
    {
        statement.getExpression().accept(expVisitor, state);
        return null;
    }  
    
    public Void visit (InvocationStatement statement, State state)
    {
        statement.getExpression().accept(expVisitor, state);
        return null;
    }
    
    public Void visit (PrintStatement statement, State state)
    {
        statement.getExpression().accept(expVisitor, state);
        return null;
    }
    
    public Void visit (PrintLnStatement statement, State state)
    {
        statement.getExpression().accept(expVisitor, state);
        return null;
    }
    
    public Void visit (ReturnEmptyStatement statement, State state)
    {
        // make sure current func returns void/null
        return null;
    }
    
    public Void visit (ReturnStatement statement, State state)
    {
        //evaluate expression
        statement.getExpression().accept(expVisitor, state);

        //compare to retType of current Func

        return null;
    }
    
    public Void visit (WhileStatement statement, State state)
    {
        //evaluate guard
        Type guardType = statement.getGuard().accept(expVisitor, state);
        if (!(guardType instanceof BoolType))
        {
            System.out.printf("Type Error (l:%d): While Statement\n", statement.getLineNum());
        }
        //evaluate body
        statement.getBody().accept(this, state);
        return null;
    }
}