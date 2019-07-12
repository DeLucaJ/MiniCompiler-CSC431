script=./compile.sh

find . -type f -name "*.mini" -exec echo $1 {} \; -exec $script $1 {} \;