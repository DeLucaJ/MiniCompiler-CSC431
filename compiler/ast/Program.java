package ast;

import java.util.*;
import semantics.*;
import cfg.*;
import llvm.*;

public class Program
{
   private final List<TypeDeclaration> types;
   private final List<Declaration> decls;
   private final List<Function> funcs;
   private LLVMProgram llvmprog;

   public Program(List<TypeDeclaration> types, List<Declaration> decls,
      List<Function> funcs)
   {
      this.types = types;
      this.decls = decls;
      this.funcs = funcs;
      this.llvmprog = null;
   }

   public boolean analyze()
   {
      //initialize state
      State state = new State();

      //add types to state.structs
      for (TypeDeclaration type : this.types){ type.define(state); }

      //add decls to global state
      for (Declaration decl : this.decls){ decl.defineSymbol(state); }

      //add functions to global state? optional for filescope
      for (Function func : this.funcs){ func.define(state); }

      //analyze all funcs
      for (Function func : this.funcs){ func.analyze(state); }

      if (state.errors.size() == 0)
      {
         System.out.println("Semantics Passed");
         return true;
      }
      else
      {
         return false;
      }
   }

   public void transform()
   {
      //initialize llvm program
      this.llvmprog = new LLVMProgram();
      LLVMState state = new LLVMState();

      //create the header?

      //create llvm type decls for each type decl
      for (TypeDeclaration type : this.types)
      {
         LLVMTypeDeclaration llvmdecl = LLVMUtility.typeToLLVM(type, state);
         state.structs.put(llvmdecl.getName(), new LLVMStructure(llvmdecl.getName(), llvmdecl.getProps()));
         this.llvmprog.getTypeDecls().add(llvmdecl);
      }
 
      //create llvm decls for each decls
      for (Declaration decl : this.decls)
      {
         LLVMDeclaration llvmdecl = LLVMUtility.declToLLVM(decl, state);
         state.global.put(llvmdecl.getName(), new LLVMIdentifier(llvmdecl.getType(), llvmdecl.getName(), true));
         this.llvmprog.getDecls().add(llvmdecl);
      }

      for (Function func : this.funcs)
      { 
         //Create an LLVMFunctionType for the CFG
         LinkedList<LLVMDeclaration> params = new LinkedList<LLVMDeclaration>();
         for (Declaration param : func.getParams())
         {
            params.add(LLVMUtility.declToLLVM(param, state));
         }
         LLVMFunctionType funcType = new LLVMFunctionType(
            func.getName(),
            LLVMUtility.astToLLVM(func.getRetType(), state),
            params
         );
         state.funcs.put(func.getName(), funcType);

         //initialize the cfg
         CFGraph cfg = new CFGraph(
            func, 
            func.getName(),
            funcType
         );
         this.llvmprog.getFuncs().add(cfg);
         func.transform(cfg, state);
      }

      //create the footer?

      // display graphs
      for (CFGraph cfg : this.llvmprog.getFuncs())
      {
         cfg.printGraph();
      }
   }
}
