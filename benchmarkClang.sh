script='clang -m32 -o'

function compile {
    file=$1
    echo $file
    $script ${file%%.ll}".out" $1
}

export -f compile
find . -type f -name "*.ll" -exec bash -c 'compile "$0"' {} \; 