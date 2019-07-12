package llvm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class State
{
    public Hashtable<String, Pointer> global;
    public Hashtable<String, Pointer> symbols;
    public Hashtable<String, Pointer> params;
    public Hashtable<String, ArrayList<Identifier>> structs;
    public Hashtable<String, FunctionType> funcs;
    public int registerIndex = 0;

    public State()
    {
        this.global = new Hashtable<String, Pointer>();
        this.symbols = new Hashtable<String, Pointer>();
        this.params = new Hashtable<String, Pointer>();
        this.structs = new Hashtable<String, ArrayList<Identifier>>();
        this.funcs = new Hashtable<String, FunctionType>();

        //declare global functions here
        //malloc
        LinkedList<Declaration> m_params = new LinkedList<>();
        m_params.add(new Declaration("size", new Integer32()));
        FunctionType malloc = new FunctionType(new Pointer(new Integer8()), m_params);

        //free
        LinkedList<Declaration> f_params = new LinkedList<>();
        f_params.add(new Declaration("pointer", new Pointer(new Integer8())));
        FunctionType free = new FunctionType(new VoidType(), f_params);

        //printf
        LinkedList<Declaration> p_params = new LinkedList<>();
        p_params.add(new Declaration("printOption", new Pointer(new Integer8())));
        FunctionType print = new FunctionType(new Integer32(), p_params);

        //scanf
        LinkedList<Declaration> s_params = new LinkedList<>();
        s_params.add(new Declaration("scanOption", new Pointer(new Integer8())));
        FunctionType scanf = new FunctionType(new Integer8(), s_params);

        //add them all
        this.funcs.put("malloc", malloc);
        this.funcs.put("free", free);
        this.funcs.put("printf", print);
        this.funcs.put("scanf", scanf);
    }
}