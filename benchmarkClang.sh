script='clang -m32'

find . -type f -name "*.ll" -exec $script {} \;
