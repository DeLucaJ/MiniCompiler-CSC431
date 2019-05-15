package visitor;

import ast.*;

import java.io.*;
import java.util.*;

public class SemanticExpressionVisitor implements ExpressionVisitor<Type>
{
    public SemanticExpressionVisitor(){}

    public Type visit (BinaryExpression expression, State state)
    {
        Type left = expression.getLeft().accept(this, state);
        Type right = expression.getRight().accept(this, state);
        BinaryExpression.Operator op = expression.getOperator();

        if (
            op == BinaryExpression.Operator.TIMES ||
            op == BinaryExpression.Operator.DIVIDE ||
            op == BinaryExpression.Operator.PLUS ||
            op == BinaryExpression.Operator.MINUS
        )
        {
            if (left instanceof IntType && right instanceof IntType)
            {
                return new IntType();
            }
        }
        else if (
            op == BinaryExpression.Operator.LT ||
            op == BinaryExpression.Operator.GT ||
            op == BinaryExpression.Operator.LE ||
            op == BinaryExpression.Operator.GE 
        )
        {
            if (left instanceof IntType && right instanceof IntType)
            {
                return new BoolType();
            }
        }
        else if (
            op == BinaryExpression.Operator.EQ ||
            op == BinaryExpression.Operator.NE
        )
        {
            //make sure types are equal
            if (left.getClass().equals(right.getClass()))
            {
                return new BoolType();
            }
        }
        else
        {
            //AND & OR
            if (left instanceof BoolType && right instanceof BoolType)
            {
                return new BoolType();
            }
        }
        return null;
    }

    // Likely needs to check for a value in table
    public Type visit (DotExpression expression, State state)
    {
        String id = expression.getId();
        Type left = expression.getLeft().accept(this, state);

        //left needs to be a struct type
        if (!(left instanceof StructType)) {
            return null;
        }

        StructType leftS = (StructType) left;

        return state.structs.get(leftS.getName()).get(id);
    }

    public Type visit (FalseExpression expression, State state)
    {
        return new BoolType();
    }

    public Type visit (IdentifierExpression expression, State state)
    {
        String id = expression.getId();
        return state.getType(id);
    }
    
    public Type visit (InvocationExpression expression, State state)
    {
        String name = expression.getName();
        List<Type> arguments = new ArrayList<Type>();
        expression.getArguments().forEach(
            (exp) -> { arguments.add(exp.accept(this, state)); }
        );
        FunctionType func = state.funcs.get(name);
        List<Type> params = func.getParamTypes();

        if (params.size() != arguments.size()) return null;

        for (int i = 0; i < params.size(); i++)
        {
            if (!params.get(i).getClass().equals(arguments.get(i).getClass())) return null;
        }

        return func.getRetType();
    }
    
    public Type visit (IntegerExpression expression, State state)
    {   
        return new IntType();
    }

    public Type visit (NewExpression expression, State state)
    {
        String id = expression.getId();

        if (state.structs.contains(id))
        {
            return new StructType(expression.getLineNum(), id);
        }

        return null;
    }
    
    public Type visit (NullExpression expression, State state)
    {
        return new VoidType();
    }
    
    public Type visit (ReadExpression expression, State state)
    {
        return new IntType(); //Read expresion reads a number
    }
    
    public Type visit (TrueExpression expression, State state)
    {
        return new BoolType();
    }
    
    public Type visit (UnaryExpression expression, State state)
    {
        UnaryExpression.Operator operator = expression.getOperator();
        Type operand = expression.getOperand().accept(this, state);

        if (
            operator == UnaryExpression.Operator.NOT && 
            operand instanceof BoolType
            )
        {
            return new BoolType();
        }
        else if (
            operator == UnaryExpression.Operator.MINUS &&
            operand instanceof IntType
        )
        {
            return new IntType();
        }
        else 
        {
            return null;
        }
    }
}