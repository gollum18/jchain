#! /usr/bin/python

import os

try:
    # create the temp file for javac source
    sep = os.sep
    src = '.'+sep+'src'+sep
    out = '.'+sep+'out'+sep
    # make the output directory if it does not exist
    if not os.path.exists(out):
        os.mkdir(out)
    # get all of the java file paths from all the subdirectories
    dirs = os.walk(src)
    with open('sources.txt', 'w') as temp:
        write = ''
        for r, dirs, _ in os.walk(src):
            for d in dirs:
                files = [r+sep+d+sep+f for f in os.listdir(r+sep+d+sep) if f.endswith('.java')]
                write = write + ' '.join(files) + ' '
        temp.write(write)
    # fork off javac
    if os.fork() == 0:
        os.execlp('javac', 'javac', '-cp', src, '-d', out, '@sources.txt')
except OSError:
    print('javac not found in path, unable to automatically build')
