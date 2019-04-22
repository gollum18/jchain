#! /usr/bin/python

import os

try:
    # get the local path separator and build the src/out directories
    src = 'src'
    out = 'docs'
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
    os.execlp('javadoc', 'javadoc', '-cp', src, '-d', out, '@sources.txt')
except OSError:
    # print an error if execlp (or any other os method) fails
    print('There was an issue generating javadoc!')
