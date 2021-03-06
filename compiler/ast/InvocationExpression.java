package ast;

import visitor.*;
import java.util.List;

public class InvocationExpression
   extends AbstractExpression
{
   private final String name;
   private final List<Expression> arguments;

   public InvocationExpression(int lineNum, String name,
      List<Expression> arguments)
   {
      super(lineNum);
      this.name = name;
      this.arguments = arguments;
   }

   // Landing for visitor
   public <T> T accept (ExpressionVisitor<T> visitor)
   {
      return visitor.visit(this);
   }

   public String getName(){ return this.name; }

   public List<Expression> getArguments(){ return this.arguments; }
}
