package ast;

import visitor.*;
import cfg.*;

public class PrintStatement
   extends AbstractStatement
{
   private final Expression expression;

   public PrintStatement(int lineNum, Expression expression)
   {
      super(lineNum);
      this.expression = expression;
   }

   //Landing for visitor
   public <T> T accept(StatementVisitor<T> visitor, State state)
   {
      return visitor.visit(this, state);
   }

   public void constructCFG(CFStatementVisitor visitor, CFGraph cfg)
   {
      visitor.visit(this, cfg);
   }

   public Expression getExpression()
   {
      return this.expression;
   }
}
