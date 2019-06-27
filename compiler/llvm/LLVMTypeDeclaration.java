package llvm;

import java.util.ArrayList;

public class LLVMTypeDeclaration implements LLVMElement
{
    private final String name;
    private final ArrayList<LLVMType> props;

    public LLVMTypeDeclaration(String name, ArrayList<LLVMType> props)
    {
        this.name = name;
        this.props = props;
    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<LLVMType> getProps()
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