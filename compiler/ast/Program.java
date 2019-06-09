package ast;

import java.util.List;
import semantics.*;
import cfg.*;

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

   public void cfTransform(List<CFGraph> cfgs)
   {
      for (int i = 0; i < this.funcs.size(); i++)
      { 
         CFGraph cfg = new CFGraph(this.funcs.get(i), this.funcs.get(i).getName());
         cfgs.add(cfg);
         this.funcs.get(i).cfTransform(cfg);
      }

      // display graphs
      for (CFGraph cfg : cfgs)
      {
         cfg.printGraph();
      }
   }
}
