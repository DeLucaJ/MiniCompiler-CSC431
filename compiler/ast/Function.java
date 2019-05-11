package ast;

import java.util.*;
import visitor.*;

public class Function
{
   private final int lineNum;
   private final String name;
   private final Type retType;
   private final List<Declaration> params;
   private final List<Declaration> locals;
   private final Statement body;

   public Function(int lineNum, String name, List<Declaration> params,
      Type retType, List<Declaration> locals, Statement body)
   {
      this.lineNum = lineNum;
      this.name = name;
      this.params = params;
      this.retType = retType;
      this.locals = locals;
      this.body = body;
   }

   public void define(State state)
   {
      List<Type> typeParams = new ArrayList<Type>(this.params.size());
      for (Declaration param : this.params)
      {
         typeParams.add(param.getType());
      }
      state.addFunction(this.name, this.retType, typeParams);
   }

   public void analyze(State state)
   {
      SemanticStatementVisitor visitor = new SemanticStatementVisitor();
      body.accept(visitor, state);
   }
}
