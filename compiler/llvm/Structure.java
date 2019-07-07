package llvm;

import java.util.ArrayList;

public class Structure implements Type
{
    private final String name;
    private final ArrayList<Identifier> properties;

    public Structure(String name, ArrayList<Identifier> properties)
    {
        this.name = name;
        this.properties = properties;
    }

    public String llvm()
    {
        return "%struct." + this.name;
    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<Identifier> getProps()
    {
        return this.properties;
    }
}