package ast;

import visitor.*;

public class WhileStatement
   extends AbstractStatement
{
   private final Expression guard;
   private final Statement body;

   public WhileStatement(int lineNum, Expression guard, Statement body)
   {
      super(lineNum);
      this.guard = guard;
      this.body = body;
   }

   //Landing for visitor
   public <T> T accept(StatementVisitor<T> visitor)
   {
      return visitor.visit(this);
   }

   public Expression getGuard()
   {
      return this.guard;
   }

   public Statement getBody()
   {
      return this.body;
   }
}
