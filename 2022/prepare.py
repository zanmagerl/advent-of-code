from termcolor import colored
import click
import os
import os.path
import subprocess

@click.command()
@click.option("--day", help="Run tests for day DAY of the Advent of Code puzzle.")
@click.option("--num-tests", default=1, help="Prepares num-tests of test files.")
def prepare(day: int, num_tests: int) -> None:
    # Prefix with zeros
    day = day.zfill(2)
    
    program_name = f'Day{day}'

    if not os.path.exists(f'src/days/{program_name}.kt'):
        with open(f'src/days/{program_name}.kt', "w"): pass
        print("Created new program file", colored("\u2713", "green"))
    else:
        print("Program file already exists", colored("\u2713", "yellow"))

    if not os.path.exists(f"tests/{day}"):
        os.mkdir(f"tests/{day}")
        print("Created test directory", colored("\u2713", "green"))
    else:
        print("Test directory already exists", colored("\u2713", "yellow"))



    for index in range(1, num_tests+1):
        index = str(index).zfill(2)
        if not os.path.exists(f"tests/{day}/test-{day}-{index}.in"):
            with open(f"tests/{day}/test-{day}-{index}.in", "w"): pass
            with open(f"tests/{day}/test-{day}-{index}.out", "w"): pass
            print(f"Created test file test {day}-{index}", colored("\u2713", "green"))
        else:
            print(f"Test file test {day}-{index} already exists", colored("\u2713", "yellow"))

if __name__ == '__main__':
    prepare()