import subprocess
import os

def exec():
    file = open("dataset.sh", "r")
    commands = file.readlines()

    for cmd in check_files_exist(commands):
        cmd = cmd.rstrip().split(" ")
        list_files = subprocess.run(cmd)
        # print("The exit code was: %d" % list_files.returncode)

def check_files_exist(commands):
    files = ["submission/test_post_competition_scoring_clips.csv", "data/train/0a5cbf90.wav", "data/test/0a0a8d4c.wav"]
    for file in files:
        print(file)
        if os.path.exists(file):
            print("EXIST")
            return commands[10:]

    return commands