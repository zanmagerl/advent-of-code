from termcolor import colored
import click
import os
import os.path
import subprocess

@click.command()
@click.option("--day", help="Run tests for day DAY of the Advent of Code puzzle.")
@click.option("--mode", default="result", help="Run program in mode MODE. It's either result (default) or debug.")
def run_tests(day: int, mode: str) -> None:
    # Prefix with zeros
    day = day.zfill(2)
    
    program_name = f'Day{day}'
    input_file = f"inputs/input{day}.in"
    # Getting input
    if os.path.exists(input_file):
        print("Fetched input", colored("\u2713", "green"))
    else:
        try:
            subprocess.check_output(f'wget -q --no-check-certificate --load-cookies=".env" -O "{input_file}" "https://adventofcode.com/2022/day/{day}/input"', shell=True)
            print("Fetching file", colored("\u2713", "green"))
        except subprocess.CalledProcessError as e:
            print("Fetching file", colored("x", "red"))
            return 
    print()

    try:
        subprocess.check_output(f"kotlinc -d classes/{program_name}.jar -include-runtime src/twentytwo/days/{program_name}.kt src/twentytwo/utilities", shell=True)
    except subprocess.CalledProcessError as e:
        print("Compilation", colored("x", "red"))
        return
    
    print("Compilation", colored("\u2713", "green"))
    print()

    if mode == "debug":
        with subprocess.Popen(f"java -jar classes/{program_name}.jar < {input_file}", stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True) as process:
            for line in process.stdout:
                print(line.decode('utf8'), end="")
    else:
        try:
            output = subprocess.check_output(f"java -jar classes/{program_name}.jar < {input_file}", shell=True).decode('utf-8')
            print(output)
        except subprocess.CalledProcessError as e:
            print(e)
            




if __name__ == '__main__':
    run_tests()