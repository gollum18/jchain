#! /usr/bin/python

# Name: scripts.py
# Purpose: Defines commands used to interact with the jchain build
# tree.
# Author: Christen Ford
# Since: 04/22/2019

import os
import sys

# constants, DO NOT change these!!
cp = 'src'
class_d = 'out'
doc_d = 'docs'
src_files = 'sources.txt'

def usage():
    print('-'*80)
    print('Usage: python scripts.py [script]')
    print('Valid scripts include:')
    print('\tbuild')
    print('\tdocgen')
    print('-'*80)

# walks the source tree and builds the sources.txt file
def walk_tree(src=cp):
    try:
        with open(src_files, 'w') as sources:
            write = ''
            # step through the directories
            for r, dirs, _ in os.walk(src):
                for d in dirs:
                    # filter out all of the files that are not java files
                    files = [os.path.join(r, d, f) for f in os.listdir(os.path.join(r, d)) if f.endswith('.java')]
                    write = write + ' '.join(files) + ' '
            sources.write(write)
    except IOError:
        print('An IOError occurred when generating sources.txt, unable to continue!')
        sys.exit(-1)

def build_script():
    '''
    Compiles the jchain build tree using javac.
    '''
    walk_tree()
    os.execlp('javac', 'javac', '-cp', cp, '-d', class_d, '@'+src_files)

def check_dir(out):
    '''
    Checks to see if the directory specifed by out exists on disk, if not is creates it.
    out: The directory to check existence for.
    '''
    if not os.path.exists(out):
        os.mkdir(out)

def docgen_script():
    '''
    Compiles documentation for the jchain build tree using javadoc
    '''
    walk_tree()
    os.execlp('javadoc', 'javadoc', '-cp', cp, '-d', doc_d, '@'+src_files)

def invoke(script):
    '''
    Attempts to invoke the script specified by \'script\'.
    script: The script to execute.
    '''
    script = script.lower()
    if script == 'build':
        build_script()
    elif script == 'docgen':
        docgen_script()
    else:
        print('ERROR: Invalid command passed!')
        usage()
        sys.exit(-1)

# entry point of the script
if __name__ == '__main__':
    if len(sys.argv) != 2:
        #debug, pull this out when done
        print('argv contents: ', sys.argv)
        #--------
        usage()
        sys.exit(-1)
    invoke(sys.argv[1])
