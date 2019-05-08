package ast;

import visitor.*;

public class ReadExpression
   extends AbstractExpression
{
   public ReadExpression(int lineNum)
   {
      super(lineNum);
   }

   // Landing for visitor
   public <T> T accept (ExpressionVisitor<T> visitor)
   {
      return visitor.visit(this);
   }
}
