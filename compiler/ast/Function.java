package ast;

import java.util.*;

import semantics.*;
import llvm.*;

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

   public Type getRetType()
   {
      return this.retType;
   }

   public List<Declaration> getParams()
   {
      return this.params;
   }

   public void define(semantics.State state)
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

   public void analyze(semantics.State state)
   {
      SemanticStatementVisitor visitor = new SemanticStatementVisitor(state);
      state.currentFunc = this.funcType();
      state.pushTable();
      //add params to state
      for (ast.Declaration param : this.params){ param.defineSymbol(state); }
      //add locals to state
      for (ast.Declaration local : this.locals){ local.defineSymbol(state); }
      body.accept(visitor);
      state.popTable();
      boolean returns = true;
      if(!(this.retType instanceof VoidType))
      {
         if(this.body instanceof BlockStatement)
         {
            ast.BlockStatement bbody = (BlockStatement) this.body;
            returns = bbody.returns;
         }
         else if (this.body instanceof ConditionalStatement)
         {
            ast.ConditionalStatement cbody = (ConditionalStatement) this.body;
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

   public void transform(llvm.Function func, llvm.State state)
   {
      LLVMStatementVisitor visitor = new LLVMStatementVisitor(func, state);
      
      //alloca retVal
      llvm.Identifier retVal = null;
      llvm.Pointer retValType = null;
      if (!(this.retType instanceof VoidType))
      {
         retValType = new llvm.Pointer(func.getFuncType().getRetType());
         retVal = new llvm.Identifier(retValType, "_retval_", false);
         AllocationInstruction retAlloca = new AllocationInstruction(retVal, func.getFuncType().getRetType());
         func.getEntry().getInstructions().add(retAlloca);
         state.symbols.put("_retval_", retValType);
      }

      //alloca params
      for (llvm.Declaration param : func.getFuncType().getParams())
      {
         //might need to add a parameter prefix
         llvm.Pointer paramType = new llvm.Pointer(param.getType());
         llvm.Identifier paramVal = new llvm.Identifier(paramType, "_P_" + param.getName(), false);
         AllocationInstruction paramAlloca = new AllocationInstruction(paramVal, paramType.getPointerType());
         func.getEntry().getInstructions().add(paramAlloca);
         state.params.put(param.getName(), paramType);
      }
      
      //alloca locals
      for (Declaration local : this.locals)
      {
         llvm.Declaration localllvm = Utility.declToLLVM(local, state);

         llvm.Pointer localType = new llvm.Pointer(localllvm.getType());
         llvm.Identifier localVal = new llvm.Identifier(localType, localllvm.getName(), false);
         AllocationInstruction localAlloca = new AllocationInstruction(localVal, localType.getPointerType());
         func.getEntry().getInstructions().add(localAlloca);
         state.symbols.put(local.getName(), localType);
      }

      //store params into identifiers
      for (llvm.Declaration param : func.getFuncType().getParams())
      {
         Pointer ppointer = state.params.get(param.getName());
         Identifier pid = new Identifier(ppointer, "_P_" + param.getName(), false);
         Identifier original = new Identifier(ppointer.getPointerType(), param.getName(), false);
         Instruction store = new StoreInstruction(original.getType(), original, pid.getType(), pid);
         func.getEntry().getInstructions().add(store);
      }

      // run the control flow visitor on the body and get the last block that it creates
      llvm.Block last = this.body.accept(visitor);

      //add a return instruction at the end
      Instruction retinst;
      if (func.getFuncType().getRetType() instanceof llvm.Void)
      {
         retinst = new ReturnVoidInstruction();
      }
      else
      {
         llvm.Register retreg = new llvm.Register(func.getFuncType().getRetType(), "u" + state.registerIndex++);
         llvm.LoadInstruction loadinst = new llvm.LoadInstruction(retreg, retVal, retValType);
         func.getExit().getInstructions().add(loadinst);
         retinst = new ReturnInstruction(func.getFuncType().getRetType(), retVal);
      }
      func.getExit().getInstructions().add(retinst);

      //add exit to the end of the linked list
      func.close();
      
      //this is here to make sure locals from other functions do not interfere
      state.symbols.clear();
   }
}
