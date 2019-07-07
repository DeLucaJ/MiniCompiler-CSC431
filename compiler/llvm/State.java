package llvm;

import java.util.Hashtable;
import java.util.LinkedList;

public class State
{
    public Hashtable<String, Identifier> global;
    public Hashtable<String, Identifier> symbols;
    public Hashtable<String, Structure> structs;
    public Hashtable<String, FunctionType> funcs;

    public State()
    {
        this.global = new Hashtable<String, Identifier>();
        this.symbols = new Hashtable<String, Identifier>();
        this.structs = new Hashtable<String, Structure>();
        this.funcs = new Hashtable<String, FunctionType>();

        //declare global functions here
        //malloc
        LinkedList<Declaration> m_params = new LinkedList<>();
        m_params.add(new Declaration("size", new Integer32()));
        FunctionType malloc = new FunctionType("malloc", new Integer8(), m_params);

        //free
        LinkedList<Declaration> f_params = new LinkedList<>();
        f_params.add(new Declaration("pointer", new Pointer(new Integer8())));
        FunctionType free = new FunctionType("free", new Void(), f_params);

        //printf
        LinkedList<Declaration> p_params = new LinkedList<>();
        p_params.add(new Declaration("printOption", new Pointer(new Integer8())));
        FunctionType print = new FunctionType("print", new Integer32(), p_params);

        //scanf
        LinkedList<Declaration> s_params = new LinkedList<>();
        s_params.add(new Declaration("scanOption", new Pointer(new Integer8())));
        FunctionType scanf = new FunctionType("scanf", new Integer8(), s_params);

        //add them all
        this.funcs.put(malloc.getName(), malloc);
        this.funcs.put(free.getName(), free);
        this.funcs.put(free.getName(), print);
        this.funcs.put(scanf.getName(), scanf);
    }
}