package ast;

import visitor.*;

public class ConditionalStatement
   extends AbstractStatement
{
   public boolean returns; 
   private final Expression guard;
   private final Statement thenBlock;
   private final Statement elseBlock;

   public ConditionalStatement(int lineNum, Expression guard,
      Statement thenBlock, Statement elseBlock)
   {
      super(lineNum);
      this.guard = guard;
      this.thenBlock = thenBlock;
      this.elseBlock = elseBlock;
      this.returns = false;
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

   public Statement getThen()
   {
      return this.thenBlock;
   }

   public Statement getElse()
   {
      return this.elseBlock;
   }
}
