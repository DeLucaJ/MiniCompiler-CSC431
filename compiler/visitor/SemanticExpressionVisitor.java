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
            else
            {
                String message = String.format(
                    "Binary Arithmetic Expression Type Error: Left is %s, Right is type %s",
                    left.getClass().getName(),
                    right.getClass().getName() 
                );
                return state.addError(expression.getLineNum(), message);
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
            else
            {
                String message = String.format(
                    "Binary Relational Expression Type Error: Left is %s, Right is type %s",
                    left.getClass().getName(),
                    right.getClass().getName() 
                );
                return state.addError(expression.getLineNum(), message);
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
            else if
            (
                (left instanceof StructType && right instanceof VoidType) ||
                (left instanceof VoidType && right instanceof StructType)
            )
            {
                return new BoolType();
            }
            else
            {
                String message = String.format(
                    "Binary Equality Expression Type Error: Left is %s, Right is type %s",
                    left.getClass().getName(),
                    right.getClass().getName() 
                );
                return state.addError(expression.getLineNum(), message);
            }
        }
        else
        {
            //AND & OR
            if (left instanceof BoolType && right instanceof BoolType)
            {
                return new BoolType();
            }
            else
            {
                String message = String.format(
                    "Binary Boolean Expression Type Error: Left is %s, Right is type %s",
                    left.getClass().getName(),
                    right.getClass().getName() 
                );
                return state.addError(expression.getLineNum(), message);
            }
        }
    }

    // Likely needs to check for a value in table
    public Type visit (DotExpression expression, State state)
    {
        String id = expression.getId();
        Type left = expression.getLeft().accept(this, state);

        //left needs to be a struct type
        if (left instanceof StructType) 
        {
            StructType leftS = (StructType) left;
            if (state.structs.containsKey(leftS.getName()))
            {
                if (state.structs.get(leftS.getName()).containsKey(id))
                {
                    return state.structs.get(leftS.getName()).get(id);
                }
                else
                {
                    String message = String.format(
                        "DotExpression Left Field Undefined Error: Field %s is undefined for Struct %s",
                        id,
                        leftS.getName()
                    );
                    return state.addError(expression.getLineNum(), message);
                }
            }
            else
            {
                String message = String.format(
                    "DotExpression Left Undefined Error: Struct %s is undefined",
                    leftS.getName()
                );
                return state.addError(expression.getLineNum(), message);
            }
        }
        else
        {
            String message = String.format(
                "DotExpression Left Error: Left is %s, not StructType",
                left.getClass().getName()
            );
            return state.addError(expression.getLineNum(), message);
        }
    }

    public Type visit (FalseExpression expression, State state)
    {
        return new BoolType();
    }

    public Type visit (IdentifierExpression expression, State state)
    {
        String id = expression.getId();
        return state.getType(expression.getLineNum(), id);
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

        if (params.size() != arguments.size()) 
        { 
            String message = String.format(
                "Too Many Parameters Error: Arguments enterered %d, Paramters needed %d",
                arguments.size(),
                params.size()
            );
            return state.addError(expression.getLineNum(), message);
        }

        for (int i = 0; i < params.size(); i++)
        {
            if 
            (
                params.get(i).getClass().equals(arguments.get(i).getClass()) ||
                (params.get(i) instanceof StructType && arguments.get(i) instanceof VoidType)
            )
            {
                continue;
            }
            else
            {
                String message = String.format(
                    "Invocation Argument Does not Match: Arguments do not match types of parameters"
                );
                return state.addError(expression.getLineNum(), message);
            }
        }

        return func.getRetType();
    }
    
    public Type visit (IntegerExpression expression, State state)
    {   
        return new IntType();
    }

    public Type visit (NewExpression expression, State state)
    {
        //throw error if structure does not exist in state
        if (state.structs.containsKey(expression.getId()))
        {
            return new StructType(expression.getLineNum(), expression.getId());
        }
        else 
        {
            String message = String.format(
               "New Expression Type Error: Attempt to Allocate undefined type %s",
               expression.getId() 
            );
            return state.addError(expression.getLineNum(), message);
        }
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

        if (operator == UnaryExpression.Operator.NOT)
        {
            if (operand instanceof BoolType)
            {
                return new BoolType();
            }
            else
            {
                String message = String.format(
                    "Unary Boolean Expression Type Error: Operand is %s not BoolType",
                    operand.getClass().getName()
                );
                return state.addError(expression.getLineNum(), message);
            }
        }
        else
        {
            if (operand instanceof IntType)
            {
                return new IntType();
            }
            else
            {
                String message = String.format(
                    "Unary Integer Expression Type Error: Operand is %s not IntType",
                    operand.getClass().getName()
                );
                return state.addError(expression.getLineNum(), message);
            }
        }
    }
}