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
      if (state.containsFunction(this.name))
      {
         String message = String.format(
            "Function Redeclaration Error: Attemt to redefine function %s", 
            this.name
         );
         state.addError(this.lineNum, message);
      }
      else 
      {
         state.addFunction(this.name, this.funcType());
      }
   }

   public FunctionType funcType()
   {
      List<Type> paramTypes = new ArrayList<Type>(this.params.size());
      for (Declaration param : this.params)
      {
         paramTypes.add(param.getType());
      }
      return new FunctionType(this.retType, paramTypes);
   }

   public void analyze(State state)
   {
      SemanticStatementVisitor visitor = new SemanticStatementVisitor();
      state.currentFunc = this.funcType();
      state.pushTable();
      //add params to state
      for (Declaration param : this.params){ param.defineSymbol(state); }
      //add locals to state
      for (Declaration local : this.locals){ local.defineSymbol(state); }
      body.accept(visitor, state);
      state.popTable();
   }
}
