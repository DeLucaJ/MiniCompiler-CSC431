package ast;

import java.util.*;
import semantics.*;
import llvm.*;

public class Program
{
   private final List<TypeDeclaration> types;
   private final List<Declaration> decls;
   private final List<Function> funcs;

   public Program(List<TypeDeclaration> types, List<Declaration> decls,
      List<Function> funcs)
   {
      this.types = types;
      this.decls = decls;
      this.funcs = funcs;
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
      llvm.Program llvmprog = new llvm.Program();
      llvm.State state = new llvm.State();

      //create the header?

      //create llvm type decls for each type decl
      for (TypeDeclaration type : this.types)
      {
         llvm.TypeDeclaration newStruct = Utility.typeToLLVM(type, state);
         state.structs.put(newStruct.getName(), newStruct.toType());
         llvmprog.getTypeDecls().add(newStruct);
      }
 
      //create llvm decls for each decls
      for (ast.Declaration decl : this.decls)
      {
         llvm.Declaration newGlobal = Utility.declToLLVM(decl, state);
         state.global.put(newGlobal.getName(), new Pointer(newGlobal.getType()));
         llvmprog.getDecls().add(newGlobal);
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
            llvm.Utility.astToLLVM(func.getRetType(), state),
            params
         );
         state.funcs.put(func.getName(), funcType);

         //initialize the cfg
         llvm.Function newFunc = new llvm.Function(
            func, 
            func.getName(),
            funcType
         );
         llvmprog.getFuncs().add(newFunc);
         func.transform(newFunc, state);
      }

      System.out.println("Control Flow Graph:--------------------");

      // display graphs
      for (llvm.Function func : llvmprog.getFuncs())
      {
         func.printGraph();
      }

      System.out.println("LLVM Output:---------------------------");

      System.out.println(llvmprog.llvm());
   }
}
