function timetest {
    file=$1
    input=${file%\/*.out}
    input=${input/"/output/"/"/"}
    echo $input
    #echo $file
    #time exec $file < 
}

export -f timetest
find . -type f -name "*.out" -exec bash -c 'timetest "$0"' {} \; 