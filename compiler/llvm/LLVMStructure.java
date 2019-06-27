package llvm;

import java.util.ArrayList;

public class LLVMStructure implements LLVMType
{
    private final String name;
    private final ArrayList<LLVMIdentifier> properties;

    public LLVMStructure(String name, ArrayList<LLVMIdentifier> properties)
    {
        this.name = name;
        this.properties = properties;
    }

    public String llvm()
    {
        return "%struct." + this.name;
    }
}