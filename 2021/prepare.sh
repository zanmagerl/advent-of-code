#!/bin/bash

function display_help () {
    # Display Help
    echo "This script runs preparation for puzzle for the day that is given as an input parameter."
    echo
    echo "Syntax: ./prepare.sh -d DAY"
    echo
    echo "Options:"
    echo "-d DAY     Run tests for day DAY of the Advent of Code puzzle."
    echo
}

function addZero () {
    if (( $1 < 10 )); then
        echo "0$1"
    else
        echo "$1"
    fi;
}

if (( $# == 0 )); then
    display_help
    exit
fi;

while [ $# -ne 0 ]
do
    if [[ $1 = "-d" ]];then
        shift;
        day=$1
        shift;
    else
        display_help
        exit
    fi;
done;

day=$(addZero $day)

programName="Day$day"

touch "src/days/${programName}.java"
touch "inputs/input${day}.in"
touch "inputs/input${day}.out"