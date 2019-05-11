package visitor;

import ast.*;
import java.util.*;

public class State
{
    public LinkedList<Hashtable<String, Type>> symbols;
    public Hashtable<String, Hashtable<String, Type>> structs; 

    public State()
    {
        this.symbols = new LinkedList<Hashtable<String, Type>>();
        this.structs = new Hashtable<String, Hashtable<String, Type>>();
        //create global table
        this.symbols.push(new Hashtable<String, Type>());
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

    public Type getType(String id)
    {
        ListIterator<Hashtable<String, Type>> iterator = symbols.listIterator(0);
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
        return null;
    }

    public void addStruct(String id)
    {
        this.structs.put(id, new Hashtable<String, Type>());
    }

    public void addPropertyToStruct(String structID, String propertyID, Type type)
    {
        this.structs.get(structID).put(propertyID, type);
    }
}