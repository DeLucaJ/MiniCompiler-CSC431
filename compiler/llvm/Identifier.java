package llvm;

public class Identifier extends NamedValue
{
    private final boolean global;

    public Identifier(Type type, String name, boolean global)
    {
        super(type, name);
        this.global = global;
    }

    public boolean isGlobal()
    {
        return this.global;
    }

    public String llvm()
    {
        String tag = this.global ? "@" : "%";
        return tag + this.getName();
    }
}