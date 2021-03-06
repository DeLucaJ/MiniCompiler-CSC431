package ast;

import visitor.*;

public class ReturnEmptyStatement
   extends AbstractStatement
{
   //public boolean valid; 

   public ReturnEmptyStatement(int lineNum)
   {
      super(lineNum);
   }

   //Landing for visitor
   public <T> T accept(StatementVisitor<T> visitor)
   {
      return visitor.visit(this);
   }
}
