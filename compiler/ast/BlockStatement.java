package ast;

import visitor.*;
import cfg.*;
import java.util.List;
import java.util.ArrayList;

public class BlockStatement
   extends AbstractStatement
{
   public boolean returns; 
   private final List<Statement> statements;

   public BlockStatement(int lineNum, List<Statement> statements)
   {
      super(lineNum);
      this.statements = statements;
      this.returns = false;
   }

   public static BlockStatement emptyBlock()
   {
      return new BlockStatement(-1, new ArrayList<>());
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

   public List<Statement> getStatements()
   {
      return this.statements;
   }
}
