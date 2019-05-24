compiler=MiniCompiler
echo "$1:"
java -cp .:../libraries/antlr-4.7.1-complete.jar:../libraries/public/javax.json-1.0.4.jar $compiler $1