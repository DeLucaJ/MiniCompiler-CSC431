script=./compile.sh

find . -type f -name "*.mini" -exec echo -stack {} \; -exec $script -stack {} \;
find . -type f -name "*.mini" -exec echo {} \; -exec $script {} \;