from termcolor import colored
import click
import os
import subprocess

@click.command()
@click.option("--day", help="Run tests for day DAY of the Advent of Code puzzle.")
@click.option("--mode", default="result", help="Run program in mode MODE. It's either result (default) or debug.")
@click.option("--test", default=-1, help="Which test to run, defaults to -1 which means that all tests are run.")
def run_tests(day: int, mode: str, test: int) -> None:
    # Prefix with zeros
    day = day.zfill(2)
    
    program_name = f'Day{day}'
    try:
        subprocess.check_output(f"kotlinc -d classes/{program_name}.jar -include-runtime src/days/{program_name}.kt src/utilities/ParserUtil.kt", shell=True)
    except subprocess.CalledProcessError as e:
        print("Compilation", colored("x", "red"))
        return
    
    print("Compilation", colored("\u2713", "green"))
    print()

    test_directory = f"tests/{day}"
    test_inputs = list(filter(lambda x: x.endswith(".in"), os.listdir(test_directory)))
    test_outputs = list(filter(lambda x: x.endswith(".out"), os.listdir(test_directory)))

    for index in range(0, len(test_inputs)):

        if test != -1 and test != index:
            continue

        test_input = test_inputs[index]
        test_output = open(f"{test_directory}/{test_outputs[index]}", "r").read()

        if mode == "debug":
            with subprocess.Popen(f"java -jar classes/{program_name}.jar < {test_directory}/{test_input}", stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True) as process:
                for line in process.stdout:
                    print(line.decode('utf8'))
        else:
            try:
                output = subprocess.check_output(f"java -jar classes/{program_name}.jar < {test_directory}/{test_input}", shell=True).decode('utf-8')
                if output.replace("\r", "") == test_output:
                    print(f"Test {index+1}", colored("\u2713", "green"))
                else:
                    print(f"Test {index+1}", colored("x", "red"))
            except subprocess.CalledProcessError as e:
                print(e)
            




if __name__ == '__main__':
    run_tests()