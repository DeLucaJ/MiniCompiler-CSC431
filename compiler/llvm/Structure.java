package llvm;

public class Structure implements Type
{
    private final String name;

    public Structure(String name)
    {
        this.name = name;
    }

    public String llvm()
    {
        return "%struct." + this.name;
    }

    public String getName()
    {
        return this.name;
    }
}