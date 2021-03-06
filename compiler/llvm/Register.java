package llvm;

public class Register extends NamedValue
{
    public Register(Type type, String name)
    {
        super(type, name);
    }

    public String llvm()
    {
        return "%" + this.getName();
    }
}