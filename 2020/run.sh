#!/bin/bash

function display_help () {
    # Display Help
    echo "This script gets the input file (if not already saved) and runs the program on given input."
    echo
    echo "Syntax: ./run.sh -d DAY -m MODE"
    echo
    echo "Options:"
    echo "-d DAY     Run program for day DAY of the Advent of Code puzzle."
    echo "-m MODE    Run program in mode MODE. It's either result (default) or debug."
    echo
}

RED='\033[1;31m'
GREEN='\033[1;32m'
ORANGE='\033[1;33m'
NC='\033[0m'

function addZero () {
    if [ $1 -lt 10 ] 
    then
        echo "0$1"
    else
        echo "$1"
    fi;
}

function get_input () {
    d=$(addZero $1)
    
    if [ -f "inputs/input$d.in" ]; then
        echo "+-----------------+"
        echo -e "|  INPUT: ${ORANGE}LOADED${NC}  |"
        echo "+-----------------+"
        echo 
    else
        wget -q --load-cookies=".env" -O "inputs/input$d.in" "https://adventofcode.com/2020/day/$1/input"  
        echo "+-----------------+"
        echo -e "|  INPUT: ${ORANGE}FETCHED${NC} |"
        echo "+-----------------+"
        echo 
    fi;
}

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

echo "+-----------------+"
echo "|      DAY $(addZero $day)     |"
echo "+-----------------+"
echo

get_input $day

day=$(addZero $day)

programName="Day$day"

echo "+-----------------+"
echo -n "| COMPILING: "

compile=$((javac -d classes -sourcepath src -cp classes src/days/$programName.java) 2>&1)

if [ $? != "0" ]; then 
    echo -e "${RED}FAIL${NC} |"
    echo "+-----------------+"
    echo 
    echo "$compile"
else
    echo -e "${GREEN}DONE${NC} |"
    echo "+-----------------+"
    echo
    output="$(java -cp classes days.$programName < "inputs/input$day.in")"
    
    if [ "$mode" = "result" ]; then 
        echo "+------+----------+"
        echo "| PART | OUTPUT   |"
        echo "+------+----------+"
        i=1
        for out in $output
        do
            echo "| $i    | $out"
            echo "+------+----------+"
            i=$((($i+1) % 10))
        done;
    elif [ "$mode" = "debug" ]; then
        echo "+-----------------+"
        echo "|    DEBUG MODE   |"
        echo "+-----------------+"
        echo
        echo "$output"
    fi;
    rm -r classes
fi;
