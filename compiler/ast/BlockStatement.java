package ast;

import visitor.*;
import java.util.List;
import java.util.ArrayList;

public class BlockStatement
   extends AbstractStatement
{
   private final List<Statement> statements;

   public BlockStatement(int lineNum, List<Statement> statements)
   {
      super(lineNum);
      this.statements = statements;
   }

   public static BlockStatement emptyBlock()
   {
      return new BlockStatement(-1, new ArrayList<>());
   }

   //Landing for visitor
   public <T> T accept(StatementVisitor<T> visitor)
   {
      return visitor.visit(this);
   }
}
