package ast;

import java.util.*;
import llvm.*;

public class Program {
   private final List<TypeDeclaration> types;
   private final List<Declaration> decls;
   private final List<Function> funcs;

   public Program(List<TypeDeclaration> types, List<Declaration> decls, List<Function> funcs) {
      this.types = types;
      this.decls = decls;
      this.funcs = funcs;
   }

   public boolean analyze() {
      // initialize state
      semantics.State state = new semantics.State();

      // add types to state.structs
      for (ast.TypeDeclaration type : this.types) {
         type.define(state);
      }

      // add decls to global state
      for (ast.Declaration decl : this.decls) {
         decl.defineSymbol(state);
      }

      // add functions to global state? optional for filescope
      for (ast.Function func : this.funcs) {
         func.define(state);
      }

      // analyze all funcs
      for (ast.Function func : this.funcs) {
         func.analyze(state);
      }

      if (state.errors.size() == 0) {
         //System.out.println("Semantics Passed");
         return true;
      } else {
         return false;
      }
   }

   public llvm.Program transform(boolean ssa)
   {
      //initialize llvm program
      llvm.Program llvmprog = new llvm.Program();
      llvm.State llvmstate = new llvm.State();
      ssa.State ssastate = new ssa.State();

      //create the header?

      //create llvm type decls for each type decl
      for (TypeDeclaration type : this.types)
      {
         //filler pointer so structs can declare themselves
         llvm.TypeDeclaration newStruct = Utility.typeToLLVM(type, llvmstate);
         llvmprog.getTypeDecls().add(newStruct);
      }
 
      //create llvm decls for each decls
      for (ast.Declaration decl : this.decls)
      {
         llvm.Declaration newGlobal = Utility.declToLLVM(decl, llvmstate);
         llvmstate.global.put(newGlobal.getName(), new Pointer(newGlobal.getType()));
         llvmprog.getDecls().add(newGlobal);

         ssa.Ident gIdent = new ssa.Ident(null, new Pointer(newGlobal.getType()), newGlobal.getName(), true);
         //System.out.println(newGlobal.getName());
         ssastate.globals.put(newGlobal.getName(), gIdent);
         //System.out.println(ssastate.globals.containsKey(newGlobal.getName()));
      }

      for (Function func : this.funcs)
      { 
         //Create an llvm.FunctionType for the CFG
         LinkedList<llvm.Declaration> params = new LinkedList<llvm.Declaration>();
         for (Declaration param : func.getParams())
         {
            params.add(llvm.Utility.declToLLVM(param, llvmstate));
         }
         llvm.FunctionType funcType = new llvm.FunctionType(
            llvm.Utility.astToLLVM(func.getRetType(), llvmstate),
            params
         );
         llvmstate.funcs.put(func.getName(), funcType);

         //initialize the cfg
         llvm.Function llvmFunc = new llvm.Function(
            func, 
            func.getName(),
            funcType
         );
         llvmprog.getFuncs().add(llvmFunc);
         
         if(ssa)
         {
            func.ssaTransform(llvmFunc, llvmstate, ssastate);
         }
         else
         {
            func.transform(llvmFunc, llvmstate);
         }
      }
      
      return llvmprog;
   }
}
