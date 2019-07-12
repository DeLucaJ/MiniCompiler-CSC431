import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import visitor.*;
import semantics.*;
import llvm.*;

import java.io.*;
import java.util.*;
import javax.json.JsonValue;

public class MiniCompiler {
   public static void main(String[] args) throws IOException
   {
      parseParameters(args);

      CommonTokenStream tokens = new CommonTokenStream(createLexer());
      MiniParser parser = new MiniParser(tokens);
      ParseTree tree = parser.program();

      if (parser.getNumberOfSyntaxErrors() == 0)
      {
         /*
            This visitor will create a JSON representation of the AST.
            This is primarily intended to allow use of languages other
            than Java.  The parser can thusly be used to generate JSON
            and the next phase of the compiler can read the JSON to build
            a language-specific AST representation.
         */
         /*MiniToJsonVisitor jsonVisitor = new MiniToJsonVisitor();
         JsonValue json = jsonVisitor.visit(tree);
         System.out.println(json);*/

         /*
            This visitor will build an object representation of the AST
            in Java using the provided classes.
         */
         MiniToAstProgramVisitor programVisitor =
            new MiniToAstProgramVisitor();
         ast.Program program = programVisitor.visit(tree);

         // Milestone 1 - Semantic Analysis
         boolean passed = program.analyze();
         if (!passed) return;
         
         // Milestone 2 & 3 - Control Flow
         //List<CFGraph> cfgs = new LinkedList<CFGraph>();
         llvm.Program llvmprog = program.transform(ssa);

         //String givenFile = args[0]; _inputFile exists
         //new File("./output").mkdir(); //makes an output directory if none exists;
         
         String pass1 = ssa ? _inputFile.replaceAll("\\.mini", "_ssa.ll") : _inputFile.replaceAll("\\.mini", "_stack.ll");
         String pass2 = pass1.replaceAll("\\.\\./", "super/");
         String pass3 = "./output/" + pass2.replaceAll("\\./", "");

         new File(pass3.replaceAll("/\\w*\\.ll$", "")).mkdirs(); //create the directories
         
         File output = new File(pass3);
         FileWriter fw = new FileWriter(output);
         fw.write(llvmprog.llvm());
         fw.close();

         /* new File("./output").mkdir();
         String newFilename = "./output/" + args[0].replaceAll("\\./", "").replaceAll(".mini", ".ll").replaceAll("\\.\\./", "");
         
         new File(newFilename.replaceAll("\\.ll", "").replaceAll("/\\w*$", "")).mkdirs();

         File output = new File(newFilename);
         FileWriter fw = new FileWriter(output);
         fw.write(llvmprog.llvm());
         fw.close(); */
      }
   }

   private static String _inputFile = null;
   private static boolean ssa = true;

   private static void parseParameters(String[] args) {
      for (int i = 0; i < args.length; i++) {
         if (args[i].equals("-stack")) {
            ssa = false;
         } else if (args[i].charAt(0) == '-') {
            System.err.println("unexpected option: " + args[i]);
            System.exit(1);
         } else if (_inputFile != null) {
            System.err.println("too many files specified");
            System.exit(1);
         } else {
            _inputFile = args[i];
         }
      }
   }

   private static void error(String msg) {
      System.err.println(msg);
      System.exit(1);
   }

   private static MiniLexer createLexer() {
      try {
         CharStream input;
         if (_inputFile == null) {
            input = CharStreams.fromStream(System.in);
         } else {
            input = CharStreams.fromFileName(_inputFile);
         }
         return new MiniLexer(input);
      } catch (java.io.IOException e) {
         System.err.println("file not found: " + _inputFile);
         System.exit(1);
         return null;
      }
   }
}
