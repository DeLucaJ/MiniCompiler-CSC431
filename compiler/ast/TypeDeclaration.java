package ast;

import java.util.*;
import visitor.*;

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

   public void define(State state)
   {
      state.structs.put(this.name, new Hashtable<String, Type>());
      for (Declaration field : fields)
      {
         field.defineField(this.name, state);
      }
   }
}
