package semantics;

import ast.*;
import java.util.*;

public class State
{
    //fields act as namespaces in the state
    public LinkedList<Hashtable<String, Type>> symbols;
    public Hashtable<String, Hashtable<String, Type>> structs;
    public Hashtable<String, FunctionType> funcs;
    public ArrayList<ErrorType> errors;
    public FunctionType currentFunc;

    public State()
    {
        this.symbols = new LinkedList<Hashtable<String, Type>>();
        this.structs = new Hashtable<String, Hashtable<String, Type>>();
        this.funcs = new Hashtable<String, FunctionType>();
        this.errors = new ArrayList<ErrorType>();
        //create global table
        this.symbols.push(new Hashtable<String, Type>());
    }

    public ErrorType addError(int linenum, String message)
    {
        ErrorType error = new ErrorType(linenum, message);
        System.out.printf("%5d| %s\n", linenum, message);
        this.errors.add(error);
        return error;
    }

    public Hashtable<String, Type> globalTable()
    {
        return symbols.getLast();
    }

    public void pushTable()
    {
        this.symbols.push(new Hashtable<String, Type>());      
    }

    public void popTable()
    {
        this.symbols.pop();
    }

    public Type getType(int linenum, String id)
    {
        ListIterator<Hashtable<String, Type>> iterator = this.symbols.listIterator(0);
        Hashtable<String, Type> current;
        Type varType;

        while (iterator.hasNext())
        {
            //needs to print error message or pass errorType when not found
            current = iterator.next(); 
            varType = current.get(id);
            if(varType != null){ return varType; }
        }
        //variable is not defined
        String message = String.format(
            "Undefined ID Error: ID %s is undefined", id
        );
        return this.addError(linenum, message);
    }

    public void addStruct(String id)
    {
        this.structs.put(id, new Hashtable<String, Type>());
    }

    public void addPropertyToStruct(String structID, String propertyID, Type type)
    {
        this.structs.get(structID).put(propertyID, type);
    }

    public void addFunction(String funcID, FunctionType func)
    {
        this.funcs.put(funcID, func);    
    }

    public boolean containsFunction(String funcID)
    {
        //this throws a null pointer exception
        return this.funcs.containsKey(funcID);
    }
}