script='clang -m32'

function compile {
    file=$1
    echo $file
    exec clang -m32 -o ${file%.ll}".out" $1
}

export -f compile
find . -type f -name "*.ll" -exec bash -c 'compile "$0"' {} \; 
