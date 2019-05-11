package ast;

import java.util.List;
import visitor.*;

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

   public void analyze()
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
   }
}
