package ast;

import java.util.*;
import visitor.*;
import semantics.*;
import cfg.*;

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

   public String getName()
   {
      return this.name;
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
      SemanticStatementVisitor visitor = new SemanticStatementVisitor(state);
      state.currentFunc = this.funcType();
      state.pushTable();
      //add params to state
      for (Declaration param : this.params){ param.defineSymbol(state); }
      //add locals to state
      for (Declaration local : this.locals){ local.defineSymbol(state); }
      body.accept(visitor);
      state.popTable();
      boolean returns = true;
      if(!(this.retType instanceof VoidType))
      {
         if(this.body instanceof BlockStatement)
         {
            BlockStatement bbody = (BlockStatement) this.body;
            returns = bbody.returns;
         }
         else if (this.body instanceof ConditionalStatement)
         {
            ConditionalStatement cbody = (ConditionalStatement) this.body;
            returns = cbody.returns;
         }
      }
      
      if(!returns)
      {
         String message = String.format(
            "Path Return Error: Function %s does not return on all paths",
            this.name
         );
         state.addError(this.lineNum, message);
      }
   }

   public void cfTransform(CFGraph cfg)
   {
      CFStatementVisitor visitor = new CFStatementVisitor(cfg);
      // declarations should be included at the beginning of the body block 
      // this may only be necessary for the stack command so hold off until translation
      
      // create block for body
      CFBlock block = new CFBlock(visitor.blockLabel());
      
      // create an edge entry -> block
      cfg.getEntry().addEdge(block);
      
      // add block to the linked list
      cfg.getBlocks().add(block);
      
      // run the control flow visitor on the body and get the last block that it creates
      CFBlock last = this.body.accept(visitor);
      
      // this is wrong because last should already have been added to the list
      //cfg.getBlocks().add(last);
      
      //return statement visit already does this
      //connect edge to the exit of the list
      //last.addEdge(cfg.getExit());

      //add exit to the end of the linked list
      cfg.getBlocks().add(cfg.getExit());
   }
}
