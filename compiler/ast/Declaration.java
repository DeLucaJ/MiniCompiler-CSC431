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
      state.symbols.peek().put(this.name, this.type);
   }

   public Type getType(){ return this.type; }
}
