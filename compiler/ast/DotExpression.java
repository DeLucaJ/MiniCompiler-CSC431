package ast;

import visitor.*;

public class DotExpression
   extends AbstractExpression
{
   private final Expression left;
   private final String id;

   public DotExpression(int lineNum, Expression left, String id)
   {
      super(lineNum);
      this.left = left;
      this.id = id;
   }

   public Expression getLeft() { return this.left; }

   public String getId() { return this.id; }

   //Landing for visitor
   public <T> T accept (ExpressionVisitor<T> visitor, State state)
   {
      return visitor.visit(this, state);
   }
}