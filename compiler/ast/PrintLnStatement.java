package ast;

import visitor.*;

public class PrintLnStatement
   extends AbstractStatement
{
   private final Expression expression;

   public PrintLnStatement(int lineNum, Expression expression)
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
