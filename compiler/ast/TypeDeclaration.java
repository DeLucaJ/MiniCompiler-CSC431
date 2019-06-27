package ast;

import java.util.*;
import semantics.*;

public class TypeDeclaration
{
   private final int lineNum;
   private final String name;
   private final List<Declaration> fields;

   public TypeDeclaration(int lineNum, String name, List<Declaration> fields)
   {
      this.lineNum = lineNum;
      this.name = name;
      this.fields = fields;
   }

   public String getName()
   {
      return this.name;
   }

   public List<Declaration> getFields()
   {
      return this.fields;
   }

   public void define(State state)
   {
      if (state.structs.contains(this.name))
      {
         String message = String.format(
            "Struct Redeclaration Error: Attemt to redefine struct %s", 
            this.name
         );
         state.addError(this.lineNum, message);
      }
      else
      {
         state.structs.put(this.name, new Hashtable<String, Type>());
         for (Declaration field : fields)
         {
            field.defineField(this.name, state);
         }
      }
   }
}
