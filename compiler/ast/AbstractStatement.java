package ast;
import visitor.*;

public abstract class AbstractStatement
   implements Statement
{
   private final int lineNum;

   public AbstractStatement(int lineNum)
   {
      this.lineNum = lineNum;
   }
}
