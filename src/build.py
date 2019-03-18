#! /usr/bin/python3

import os
import shutil

java_files = [x for x in os.listdir('./') if x.endswith(".java")]
javac = shutil.which('javac')

if javac:
    for f in java_files:
        # fork off a new process, check for child
        if os.fork() == 0:
            # replace the childs image with a call to javac
            os.execl(javac, "javac", f)
else:
    print('javac not in PATH, either add it or compile the project by hand')
