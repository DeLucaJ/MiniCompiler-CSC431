package llvm;

import java.util.Hashtable;

public class LLVMState
{
    public Hashtable<String, LLVMIdentifier> global;
    public Hashtable<String, LLVMIdentifier> symbols;
    public Hashtable<String, LLVMStructure> structs;
    public Hashtable<String, LLVMFunctionType> funcs;

    public LLVMState()
    {
        this.global = new Hashtable<String, LLVMIdentifier>();
        this.symbols = new Hashtable<String, LLVMIdentifier>();
        this.structs = new Hashtable<String, LLVMStructure>();
        this.funcs = new Hashtable<String, LLVMFunctionType>();
    }
}