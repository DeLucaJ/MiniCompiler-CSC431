package ssa;

import llvm.*;
import java.util.Hashtable;

public class State
{
    public Hashtable<String, Hashtable<Block, Value>> currentDefs;
    public Hashtable<String, Type> varTypes;
    public Hashtable<String, Integer> varNums;
    public Hashtable<String, Ident> globals;
    //public Block global;

    public State()
    {
        this.currentDefs = new Hashtable<>();
        this.varTypes = new Hashtable<>();
        this.varNums = new Hashtable<>();
        this.globals = new Hashtable<>();
    }

    public void writeVariable(String variable, Block block, Value value)
    {
        if (!currentDefs.containsKey(variable))
        {
            currentDefs.put(variable, new Hashtable<>());
            varTypes.put(variable, value.getType());
        }
        currentDefs.get(variable).put(block, value);
        /* System.out.println(
            String.format(
                "$ %s written to %s with value %s of type %s",
                variable,
                block.getLabel(),
                value.toLLVM().llvm(),
                value.getType().llvm()
            )
        ); */
    }

    public Value readVariable(String variable, Block block)
    {
        /* System.out.println(
            String.format(
                "Reading %s in block %s",
                variable,
                block.getLabel()
            )
        ); */
        if (!currentDefs.containsKey(variable))
        {
            currentDefs.put(variable, new Hashtable<>());
        }
        if (currentDefs.get(variable).containsKey(block))
        {
            /* System.out.println(
                String.format(
                    "%s read as %s of type %s",
                    variable, 
                    currentDefs.get(variable).get(block).toLLVM().llvm(),
                    currentDefs.get(variable).get(block).getType().llvm()
                )
            ); */
            return currentDefs.get(variable).get(block);
        }
        return readVariableRecursive(variable, block);
    }

    private Value readVariableRecursive(String variable, Block block)
    {
        /* System.out.println(
            String.format(
                "Reading %s recursively in block %s",
                variable,
                block.getLabel()
            )
        ); */
        Value val;
        if (!block.isSealed())
        {
            // System.out.println("! " + block.getLabel() + " is not sealed");
            Register phireg = new Register(block, varTypes.get(variable), variable, varNums.get(variable));
            varNums.put(variable, varNums.get(variable) + 1);
            PhiInstruction phi = new PhiInstruction(phireg, variable);
            phireg.setDefinition(phi);
            val = phireg;

            block.getIncomplete().add(phi);
            block.getPhis().add(phi);
        }
        else if (block.getParents().size() == 0)
        {
            val = globals.get(variable);
            return val;
        }
        else if (block.getParents().size() == 1)
        {
            val = readVariable(variable, block.getParents().getFirst().getSouce());
        }
        else
        {
            Register phireg = new Register(block, varTypes.get(variable), variable, varNums.get(variable));
            varNums.put(variable, varNums.get(variable) + 1);
            PhiInstruction phi = new PhiInstruction(phireg, variable);
            phireg.setDefinition(phi);
            block.getPhis().add(phi);
            val = phireg;
            
            writeVariable(variable, block, val);
            
            //val = phi.addPhiOperands(variable, this);\
            val = phi.addPhiOperands(variable, this);
        }
        writeVariable(variable, block, val);
        /* System.out.println(
            String.format(
                "%s read as %s of type %s",
                variable, 
                currentDefs.get(variable).get(block).toLLVM().llvm(),
                currentDefs.get(variable).get(block).getType().llvm()
            )
        ); */
        return val;
    }

    public void sealBlock(Block block)
    {
        for (PhiInstruction phi : block.getIncomplete())
        {
            String variable = phi.getVariable();
            phi.addPhiOperands(variable, this);
        }
        block.setSealed();
    }
}