package ast;

import visitor.*;

public class IdentifierExpression
   extends AbstractExpression
{
   private final String id;

   public IdentifierExpression(int lineNum, String id)
   {
      super(lineNum);
      this.id = id;
   }

   //Laning for visitor
   public <T> T accept (ExpressionVisitor<T> visitor, State state)
   {
      return visitor.visit(this, state);
   }

   public String getId(){ return this.id; }
}
