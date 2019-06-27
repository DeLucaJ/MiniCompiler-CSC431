package ast;

import visitor.*;

public class ReturnStatement
   extends AbstractStatement
{
   //public boolean valid; 
   private final Expression expression;

   public ReturnStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   //Landing for visitor
   public <T> T accept(StatementVisitor<T> visitor)
   {
      return visitor.visit(this);
   }

   public Expression getExpression()
   {
      return this.expression;
   }
}
