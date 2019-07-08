package llvm;

import java.util.ArrayList;

public class TypeDeclaration implements Element
{
    private final String name;
    private final ArrayList<Identifier> fields;
    private final Pointer type;

    public TypeDeclaration(String name, ArrayList<Identifier> fields)
    {
        this.name = name;
        this.fields = fields;
        this.type = new Pointer(new Structure(this.name, this.fields));
    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<Identifier> getFields()
    {
        return this.fields;
    }

    public Pointer toType()
    {
        return this.type;
    }

    public String llvm()
    {
        String typeString = "%struct." + this.name + " = type {";
        for (int i = 0; i < fields.size(); i++)
        {
            typeString += fields.get(i).getType().llvm();
            if (i != fields.size() - 1) typeString += ", ";
            else typeString += "}";
        }
        return typeString;
    }
}