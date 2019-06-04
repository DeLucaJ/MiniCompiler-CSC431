package ast;
import cfg.*;
import visitor.*;

public abstract class AbstractStatement
   implements Statement
{
   private final int lineNum;

   public AbstractStatement(int lineNum)
   {
      this.lineNum = lineNum;
   }

   public int getLineNum()
   {
      return this.lineNum;
   }
}
