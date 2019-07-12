package ssa;

import llvm.*;
import java.util.LinkedList;

public class Register extends Value
{
    // static field for tracking virtual register number
    private static int current = 0;

    private final int number;
    private Instruction definition;
    private LinkedList<Instruction> users;
    private String name;

    public Register(Block block, Type type, Instruction definition)
    {
        super(block, type);
        this.number = newNumber();
        this.definition = definition;
        this.users = new LinkedList<>();
        this.name = "_u";
    }

    public Register(Block block, Type type)
    {
        super(block, type);
        this.number = newNumber();
        this.definition = null;
        this.users = new LinkedList<>();
        this.name = "_u";
    }

    public Register(Block block, Type type, String name, int number)
    {
        super(block, type);
        this.name = name;
        this.number = number;
        this.definition = null;
        this.users = new LinkedList<>();
    }

    //static method for creating new register number
    public static int newNumber()
    {
        return current++;
    }

    public int getNumber()
    {
        return this.number;
    }

    public String getName()
    {
        return this.name + this.number;
    }

    public Instruction getDefinition()
    {
        return this.definition;
    }

    public void setDefinition(Instruction definition)
    {
        this.definition = definition;
    }

    public LinkedList<Instruction> getUsers()
    {
        return this.users;
    }

    public void addUser(Instruction user)
    {
        this.users.add(user);
    }

    public llvm.Register toLLVM()
    {
        return new llvm.Register(this.getType(), this.getName());
    }
}