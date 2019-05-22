package ast;

import visitor.*;

public class Declaration
{
   private final int lineNum;
   private final Type type;
   private final String name;

   public Declaration(int lineNum, Type type, String name)
   {
      this.lineNum = lineNum;
      this.type = type;
      this.name = name;
   }

   public void defineField(String structName, State state)
   {
      state.structs.get(structName).put(this.name, this.type);
   }

   public void defineSymbol(State state)
   {
      //check if the symbol already exists
      if(state.symbols.peek().contains(this.name))
      {
         //already defined 
         String message = String.format(
            "Variable Redeclaration Error: Attemt to redefine variable %s of %s as %s", 
            this.name, 
            state.symbols.peek().get(this.name).getClass().getName(),
            this.type.getClass().getName()
         );
         state.addError(this.lineNum, message);
      }
      else
      {
         state.symbols.peek().put(this.name, this.type);
      }
   }

   public Type getType(){ return this.type; }
}
