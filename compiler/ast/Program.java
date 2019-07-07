package ast;

import java.util.*;
import semantics.*;
import llvm.*;

public class Program
{
   private final List<TypeDeclaration> types;
   private final List<Declaration> decls;
   private final List<Function> funcs;
   private llvm.Program llvmprog;

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
      semantics.State state = new semantics.State();

      //add types to state.structs
      for (ast.TypeDeclaration type : this.types){ type.define(state); }

      //add decls to global state
      for (ast.Declaration decl : this.decls){ decl.defineSymbol(state); }

      //add functions to global state? optional for filescope
      for (ast.Function func : this.funcs){ func.define(state); }

      //analyze all funcs
      for (ast.Function func : this.funcs){ func.analyze(state); }

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
      this.llvmprog = new llvm.Program();
      llvm.State state = new llvm.State();

      //create the header?

      //create llvm type decls for each type decl
      for (TypeDeclaration type : this.types)
      {
         llvm.TypeDeclaration llvmdecl = llvm.Utility.typeToLLVM(type, state);
         state.structs.put(llvmdecl.getName(), new llvm.Structure(llvmdecl.getName(), llvmdecl.getProps()));
         this.llvmprog.getTypeDecls().add(llvmdecl);
      }
 
      //create llvm decls for each decls
      for (Declaration decl : this.decls)
      {
         llvm.Declaration llvmdecl = llvm.Utility.declToLLVM(decl, state);
         state.global.put(llvmdecl.getName(), new llvm.Identifier(llvmdecl.getType(), llvmdecl.getName(), true));
         this.llvmprog.getDecls().add(llvmdecl);
      }

      for (Function func : this.funcs)
      { 
         //Create an llvm.FunctionType for the CFG
         LinkedList<llvm.Declaration> params = new LinkedList<llvm.Declaration>();
         for (Declaration param : func.getParams())
         {
            params.add(llvm.Utility.declToLLVM(param, state));
         }
         llvm.FunctionType funcType = new llvm.FunctionType(
            func.getName(),
            llvm.Utility.astToLLVM(func.getRetType(), state),
            params
         );
         state.funcs.put(func.getName(), funcType);

         //initialize the cfg
         llvm.Function cfg = new llvm.Function(
            func, 
            func.getName(),
            funcType
         );
         this.llvmprog.getFuncs().add(cfg);
         func.transform(cfg, state);
      }

      //create the footer?

      // display graphs
      for (llvm.Function cfg : this.llvmprog.getFuncs())
      {
         cfg.printGraph();
      }
   }
}
