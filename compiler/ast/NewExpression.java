package ast;

import visitor.*;

public class NewExpression
   extends AbstractExpression
{
   private final String id;

   public NewExpression(int lineNum, String id)
   {
      super(lineNum);
      this.id = id;
   }

   // Landing for visitor
   public <T> T accept (ExpressionVisitor<T> visitor)
   {
      return visitor.visit(this);
   }
}
