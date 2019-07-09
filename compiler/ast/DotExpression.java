package ast;

import visitor.*;

public class DotExpression
   extends AbstractExpression
{
   private final Expression left;
   private final String id;
   private boolean isSource = false;

   public DotExpression(int lineNum, Expression left, String id)
   {
      super(lineNum);
      this.left = left;
      this.id = id;
   }

   public DotExpression(int lineNum, Expression left, String id, boolean isSource)
   {
      super(lineNum);
      this.left = left;
      this.id = id;
      this.isSource = isSource;
   }

   public Expression getLeft() { return this.left; }

   public String getId() { return this.id; }

   public void setSource()
   {
      this.isSource = true;
   }

   public boolean isSource()
   {
      return this.isSource;
   }

   //Landing for visitor
   public <T> T accept (ExpressionVisitor<T> visitor)
   {
      return visitor.visit(this);
   }
}
