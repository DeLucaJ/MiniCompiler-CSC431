package llvm;

import ast.*;
import visitor.*;

public interface LLVMInstruction extends LLVMElement
{
    public String llvm();
}