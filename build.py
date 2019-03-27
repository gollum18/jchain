#! /usr/bin/python

# Generic python2/python3 program for invoking javac on a package.
#   Assumptions are made that the user has the source code for their
#   project stored in a folder named 'src' in the same directory as 
#   build.py. The user should also be aware that this program will 
#   generate output in a folder named 'out' in the same directory 
#   as this script. Note: This script makes use of execlp to invoke javac.
#   If javac is not in your PATH env variable, then the script will inform
#   you so.
#
# This script will automatically walk your directory structure and 
#   compile a list of java files to compile. The script will generate (but 
#   not automatically delete) a file named 'sources.txt' that contains all 
#   of the java files it found in your directory substructure in a list 
#   format. You are free to delete this file after your program 
#   successfully compiles, akthough I would suggest just levaing it there
#   because the script will just overwrite it the next time it runs. Any 
#   errors should be reported as normal by javac.
#
# To invoke this script merely enter (at cmd/powershell/terminal window):
#   python build.py

import os

try:
    # get the local path separator and build the src/out directories
    src = 'src'
    out = 'out'
    # make the output directory if it does not exist
    if not os.path.exists(out):
        os.mkdir(out)
    with open('sources.txt', 'w') as sources:
        write = ''
        # step through the directories
        for r, dirs, _ in os.walk(src):
            for d in dirs:
                # filter out all of the files that are not java files
                files = [os.path.join(r, d, f) for f in os.listdir(os.path.join(r, d)) if f.endswith('.java')]
                write = write + ' '.join(files) + ' '
        sources.write(write)
    # replace the script with javac passing in the src/out variables we got earlier
    #   and pointing it to sources.txt
    os.execlp('javac', 'javac', '-cp', src, '-d', out, '@sources.txt')
except OSError:
    # print an error if execlp (or any other os method) fails
    print('There was an issue calling javac!')
    print('Try compiling your project by hand.')
