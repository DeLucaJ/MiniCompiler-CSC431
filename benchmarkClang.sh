script='clang -m32'

find . -type f -name "./output/*.ll" -exec $script {} \;