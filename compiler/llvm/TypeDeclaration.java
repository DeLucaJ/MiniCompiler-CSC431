package llvm;

import java.util.ArrayList;

public class TypeDeclaration implements Element
{
    private final String name;
    private final ArrayList<Identifier> props;

    public TypeDeclaration(String name, ArrayList<Identifier> props)
    {
        this.name = name;
        this.props = props;
    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<Identifier> getProps()
    {
        return this.props;
    }

    public String llvm()
    {
        String typeString = "%struct." + this.name + " = type {";
        for (int i = 0; i < props.size(); i++)
        {
            typeString += props.get(i).llvm();
            if (i != props.size() - 1) typeString += ", ";
            else typeString += "}";
        }
        return typeString;
    }
}