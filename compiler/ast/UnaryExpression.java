package ast;

import visitor.*;

public class UnaryExpression
   extends AbstractExpression
{
   private final Operator operator;
   private final Expression operand;

   private UnaryExpression(int lineNum, Operator operator, Expression operand)
   {
      super(lineNum);
      this.operator = operator;
      this.operand = operand;
   }

   public Expression getOperand(){ return this.operand; }

   public Operator getOperator(){ return this.operator; }

   // Landing for visitor
   public <T> T accept (ExpressionVisitor<T> visitor, State state)
   {
      return visitor.visit(this, state);
   }

   public static UnaryExpression create(int lineNum, String opStr,
      Expression operand)
   {
      if (opStr.equals(NOT_OPERATOR))
      {
         return new UnaryExpression(lineNum, Operator.NOT, operand);
      }
      else if (opStr.equals(MINUS_OPERATOR))
      {
         return new UnaryExpression(lineNum, Operator.MINUS, operand);
      }
      else
      {
         throw new IllegalArgumentException();
      }
   }

   private static final String NOT_OPERATOR = "!";
   private static final String MINUS_OPERATOR = "-";

   public static enum Operator
   {
      NOT, MINUS
   }
}
