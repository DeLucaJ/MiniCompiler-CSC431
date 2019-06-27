package llvm;

import java.util.ArrayList;

public class LLVMStructure implements LLVMType
{
    private String name;
    private ArrayList<LLVMIdentifier> properties;

    public LLVMStructure(String name)
    {
        this.name = name;
    }

    public String llvm()
    {
        return "%struct." + this.name;
    }
}