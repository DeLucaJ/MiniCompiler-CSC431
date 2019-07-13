function timetest {
    file=$1
    input=${file%\/*.out}
    input=${input/"/output/"/"/"}
    input=${input}"/input"
    output=${file%.out}"_output.txt"
    #echo $input
    #echo $file
    time exec $file < $input > $output
    #echo "$file < $input > $output"
}

export -f timetest
find . -type f -name "*.out" -exec bash -c 'timetest "$0"' {} \; 