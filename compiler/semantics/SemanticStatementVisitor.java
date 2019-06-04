package semantics;

import ast.*;
import visitor.*;
import java.lang.*;
import java.util.*;

public class SemanticStatementVisitor implements StatementVisitor<Void>
{
    public State state;
    private SemanticExpressionVisitor expVisitor;

    public SemanticStatementVisitor(State state)
    {
        this.state = state;
        this.expVisitor = new SemanticExpressionVisitor(this.state);
    }

    public Void visit (AssignmentStatement statement)
    {
        LvalueDot dotTarget;
        LvalueId idTarget;
        Type sourceType = statement.getSource().accept(expVisitor);

        //evalueate Lvalue of target
        if (statement.getTarget() instanceof LvalueDot)
        {
            dotTarget = (LvalueDot) statement.getTarget();
            Type leftType = dotTarget.getLeft().accept(this.expVisitor);
            if (!(leftType instanceof StructType))
            {
                String message = String.format(
                    "LvalueDot Left Type Error: Left is of %s not of StructType",
                    leftType.getClass().getName()
                );
                state.addError(statement.getLineNum(), message);
                return null;
            }
            StructType leftS = (StructType) leftType;
            //make sure dotTarget is a field in struct leftS
            if (state.structs.containsKey(leftS.getName()))
            {
                if(state.structs.get(leftS.getName()).containsKey(dotTarget.getId()))
                {
                    if (
                        state.structs.get(leftS.getName()).get(dotTarget.getId()).getClass().equals(sourceType.getClass()) ||
                        (state.structs.get(leftS.getName()).get(dotTarget.getId()) instanceof StructType && sourceType instanceof VoidType)
                    )
                    {
                        return null;
                    }
                    else
                    {
                        String message = String.format(
                            "LvalueDot Field Type Error: Source type is of %s not %s for field %s.%s",
                            sourceType.getClass().getName(),
                            state.structs.get(leftS.getName()).get(dotTarget.getId()),
                            leftS.getName(),
                            dotTarget.getId()
                        );
                        state.addError(statement.getLineNum(), message);
                        return null;
                    }
                }
                else
                {
                    String message = String.format(
                        "LvalueDot Undefined Field Error: StructType %s does not contain field %s",
                        leftS.getName(),
                        dotTarget.getId()
                    );
                    state.addError(statement.getLineNum(), message);
                    return null;
                }
            }
            else
            {
                String message = String.format(
                    "LvalueDot Undefined Struct Error: StructType %s is undefined",
                    leftS.getName()
                );
                state.addError(statement.getLineNum(), message);
                return null;
            }
        }
        else //instance of LvalueId
        {
            idTarget = (LvalueId) statement.getTarget();
            Type targetType = state.getType(statement.getLineNum(), idTarget.getId());
            if (targetType instanceof StructType && sourceType instanceof VoidType)
            {
                return null;
            }
            else if (!targetType.getClass().equals(sourceType.getClass()))
            {
                String message = String.format(
                    "LvalueId Assignment Type Error: Cannot assign %s to %s of %s",
                    sourceType.getClass().getName(),
                    idTarget.getId(),
                    targetType.getClass().getName()
                );
                state.addError(statement.getLineNum(), message);
                return null;
            }
        }
        return null;
    }
    
    public Void visit (BlockStatement statement)
    {
        List<Statement> body = statement.getStatements();
        boolean returns = false;
        for (int i = 0; i < body.size(); i++)
        {
            Statement stmt = body.get(i);
            stmt.accept(this); 
            if (stmt instanceof ReturnEmptyStatement || stmt instanceof ReturnStatement)
            {
                returns = true; 
            }
            else if (stmt instanceof BlockStatement)
            {
                BlockStatement bstmt = (BlockStatement) stmt;
                returns = bstmt.returns;
            }
            else if (stmt instanceof ConditionalStatement)
            {
                ConditionalStatement cstmt = (ConditionalStatement) stmt;
                returns = cstmt.returns; 
            }
        }
        statement.returns = returns; 
        return null;
    }

    public Void visit (ConditionalStatement statement)
    {
        //Evaluate guard
        Type guardType = statement.getGuard().accept(expVisitor);
        if (!(guardType instanceof BoolType))
        {
            String message = String.format(
               "Conditional Statement Guard Type Error: Guard expression of type %s not of BoolType",
               guardType.getClass().getName() 
            );
            state.addError(statement.getLineNum(), message);
        }
        statement.getThen().accept(this);
        statement.getElse().accept(this);

        boolean thenRet = false;
        boolean elseRet = false;
        //check then and else
        if (statement.getThen() instanceof BlockStatement)
        {
            BlockStatement thenBlock = (BlockStatement) statement.getThen();
            thenRet = thenBlock.returns;
        }
        if (statement.getElse() instanceof BlockStatement)
        {
            BlockStatement elseBlock = (BlockStatement) statement.getElse();
            elseRet = elseBlock.returns;
        }
        statement.returns = thenRet && elseRet;
        return null;
    }

    public Void visit (DeleteStatement statement)
    {
        Type exptype = statement.getExpression().accept(expVisitor);
        if (!(exptype instanceof StructType))
        {
            String message = String.format(
               "Delete Expression Type Error: Expression is of type %s not of StructType",
               exptype.getClass().getName() 
            );
            state.addError(statement.getLineNum(), message);
        }
        return null;
    }  
    
    public Void visit (InvocationStatement statement)
    {
        statement.getExpression().accept(expVisitor);
        return null;
    }
    
    public Void visit (PrintStatement statement)
    {
        Type expType = statement.getExpression().accept(expVisitor);
        if (!(expType instanceof IntType))
        {
            String message = String.format(
               "Print Expression Type Error: Expression is of type %s not of IntType",
               expType.getClass().getName() 
            );
            state.addError(statement.getLineNum(), message);
        }
        return null;
    }
    
    public Void visit (PrintLnStatement statement)
    {
        Type expType = statement.getExpression().accept(expVisitor);
        if (!(expType instanceof IntType))
        {
            String message = String.format(
               "PrintLn Expression Type Error: Expression is of type %s not of IntType",
               expType.getClass().getName() 
            );
            state.addError(statement.getLineNum(), message);
        }
        return null;
    }
    
    public Void visit (ReturnEmptyStatement statement)
    {
        // make sure current func returns void/null
        if (!state.currentFunc.getRetType().getClass().equals((new VoidType()).getClass()))
        {
            String message = String.format(
               "Return Empty Type Error: Return type is of type %s not of VoidType",
               state.currentFunc.getRetType().getClass().getName()
            );
            state.addError(statement.getLineNum(), message);
        }
        return null;
    }
    
    public Void visit (ReturnStatement statement)
    {
        //evaluate expression
        Type rType = statement.getExpression().accept(expVisitor);
        //compare to retType of current Func
        if ((rType instanceof VoidType) && (state.currentFunc.getRetType() instanceof StructType))
        {
            return null;
        }
        else if (!state.currentFunc.getRetType().getClass().equals(rType.getClass()))
        {
            String message = String.format(
               "Return Type Error: Return expression type is of type %s not of %s",
               rType.getClass().getName(),
               state.currentFunc.getRetType().getClass().getName()
            );
            state.addError(statement.getLineNum(), message);
        }
        return null;
    }
    
    public Void visit (WhileStatement statement)
    {
        //evaluate guard
        Type guardType = statement.getGuard().accept(expVisitor);
        if (!(guardType instanceof BoolType))
        {
            String message = String.format(
               "While Statement Guard Type Error: Guard expression of type %s not of BoolType",
               guardType.getClass().getName() 
            );
            state.addError(statement.getLineNum(), message);
        }
        //evaluate body
        statement.getBody().accept(this);
        return null;
    }
}