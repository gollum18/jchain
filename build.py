#! /usr/bin/python3

import os
import shutil

javac = shutil.which('javac')

if javac:
    sep = os.sep
    src = '.'+sep+'src'+sep
    out = '.'+sep+'out'+sep
    if not os.path.exists(out):
        os.mkdir(out)
    dirs = os.walk(src)
    for r, dirs, _ in os.walk(src):
        for d in dirs:
            files = [r+sep+d+sep+f for f in os.listdir(r+sep+d+sep) if f.endswith('.java')]
            for f in files:
                if os.fork() == 0:
                    os.execl(javac, "javac", '-cp', src, '-d', out, f)
else:
    print('javac not found in path, unable to automatically build')
