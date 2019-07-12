script='clang -m32'

find . -type f -name "*.ll" -exec echo {} \; -exec $script {} \; -exec exho {} \;
