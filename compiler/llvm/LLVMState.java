package llvm;

import java.util.Hashtable;
import java.util.LinkedList;

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

        //declare global functions here
        //malloc
        LinkedList<LLVMDeclaration> m_params = new LinkedList<>();
        m_params.add(new LLVMDeclaration("size", new LLVMInteger32()));
        LLVMFunctionType malloc = new LLVMFunctionType("malloc", new LLVMInteger8(), m_params);

        //free
        LinkedList<LLVMDeclaration> f_params = new LinkedList<>();
        f_params.add(new LLVMDeclaration("pointer", new LLVMPointer(new LLVMInteger8())));
        LLVMFunctionType free = new LLVMFunctionType("free", new LLVMVoid(), f_params);

        //printf
        LinkedList<LLVMDeclaration> p_params = new LinkedList<>();
        p_params.add(new LLVMDeclaration("printOption", new LLVMPointer(new LLVMInteger8())));
        LLVMFunctionType print = new LLVMFunctionType("print", new LLVMInteger32(), p_params);

        //scanf
        LinkedList<LLVMDeclaration> s_params = new LinkedList<>();
        s_params.add(new LLVMDeclaration("scanOption", new LLVMPointer(new LLVMInteger8())));
        LLVMFunctionType scanf = new LLVMFunctionType("scanf", new LLVMInteger8(), s_params);

        //add them all
        this.funcs.put(malloc.getName(), malloc);
        this.funcs.put(free.getName(), free);
        this.funcs.put(free.getName(), print);
        this.funcs.put(scanf.getName(), scanf);
    }
}