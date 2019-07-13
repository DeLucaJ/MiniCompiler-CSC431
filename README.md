# MiniCompiler
Project for Aaron Keen's, Programming Languages II. A compiler written in Java for the Mini programming language.

## Scripts
- **./minic** : compiles a given file in register-based IR, use "-stack" stack-based IR
- **./build** : compiles the project
- **./clean** : removes all compiled project files
- **./benchmark** : compiles all benchmark files in register-based and stack-based IR
- **./benchmarkClang** : compiles all ".ll" files in the local directory and subdirectories and outputs executable ".out" files for each one

## Packages (All are in the ./compiler/ directory)
- **ast** : contains the given parser files with modifications
- **llvm** : contains the llvm ast and files for stack-based IR
- **semantics** : contains the files for semantic analysis
- **ssa** : contains the files necessary for register-based IR
- **visitor** : containst the interfaces ExpressionVisitor<T> and StatementVisitor<T> which are used by the bulk of the program.
