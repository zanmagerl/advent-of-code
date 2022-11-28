#!/bin/bash


function display_help () {
    # Display Help
    echo "This script runs tests for puzzle for the day that is given as an input parameter."
    echo
    echo "Syntax: ./test.sh -d DAY -m MODE"
    echo
    echo "Options:"
    echo "-d DAY     Run tests for day DAY of the Advent of Code puzzle."
    echo "-m MODE    Run program in mode MODE. It's either result (default) or debug."
    echo
}

function addZero () {
    if (( $1 < 10 )); then
        echo "0$1"
    else
        echo "$1"
    fi;
}

RED='\033[1;31m'
GREEN='\033[1;32m'
ORANGE='\033[1;33m'
NC='\033[0m'

if (( $# == 0 )); then
    display_help
    exit
fi;

mode="result"
while [ $# -ne 0 ]
do
    if [[ $1 = "-d" ]];then
        shift;
        day=$1
        shift;
    elif [[ $1 = "-m" ]];then
        shift;
        mode=$1
        shift;
    else
        display_help
        exit
    fi;
done;

day=$(addZero $day)

programName="Day$day"

echo "+-----------------+"
echo "|      DAY $day     |"
echo "+-----------------+"
echo ""
echo "+-----------------+"
echo -n "| COMPILING: "

compile=$((kotlinc -d classes/${programName}.jar -include-runtime src/days/${programName}.kt src/utilities/*.kt) 2>&1)

if [ $? != "0" ]; then 
    echo -e "${RED}FAIL${NC} |"
    echo "+-----------------+"
    echo 
    echo "$compile"
else
    echo -e "${GREEN}DONE${NC} |"
    echo "+-----------------+"
    echo
    if [ "$mode" = "result" ]; then
 
        echo "+---+-------------+"
        echo "| N |    RESULT   |"
        echo "+---+-------------+"
        i=1
        for f in $(ls tests/test-$day*.in)
        do  
            testNumber=$(addZero $i)

            out=$(java -jar classes/${programName}.jar < $f)
            sol=$(cat tests/test-$day-$testNumber.out)
            sol=$(echo "$sol" | tr -d '\r') # to remove \r characters
            if [ "$out" == "$sol" ]; then 
                echo -e "| $i |    ${GREEN}true${NC}     |"
            else 
                echo -e "| $i |    ${RED}false${NC}    |"
            fi;
            echo "+---+-------------+"
            i=$((($i+1) % 10))
        done;
    
    elif [ "$mode" = "debug" ]; then
        
        echo "+-----------------+"
        echo "|    DEBUG MODE   |"
        echo "+-----------------+"
        echo
        
        echo "+---+-------------+"
        echo "| N |    RESULT   |"
        echo "+---+-------------+"
        i=1
        for f in $(ls tests/test-$day*.in)
        do  
            testNumber=$(addZero $i)

            out=$(java -jar classes/${programName}.jar < $f)
            
            echo -e "| $i |    ${ORANGE}debug${NC}    |"
            echo "+---+-------------+"
            echo "$out"

            i=$((($i+1) % 10))
        done;

    fi;
    rm -r classes
fi;

