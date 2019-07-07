package llvm;

public class Label extends NamedValue
{
    public Label(String name)
    {
        super(new LabelType(), name);
    }

    public String llvm()
    {
        return "%" + this.getName();
    }
}