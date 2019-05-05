#! /usr/bin/python

# Name: scripts.py
# Purpose: Defines commands used to interact with the jchain build
# tree.
# Author: Christen Ford
# Since: 04/22/2019

import os
import sys

# constants, DO NOT change these!!
cp = os.path.join('.', 'src')
class_d = os.path.join('.', 'out')
doc_d = os.path.join('.', 'docs')
src_files = 'sources.txt'

def usage(err=None):
    print('-'*80)
    print('Usage: python scripts.py [arg]')
    print('Valid scripts include:')
    print('\tbuild: builds jchain')
    print('\tdocgen: compiles documentation')
    print('\ttest: runs TestBC')
    print('Additionally passing -h or --help will show this message.')
    print('-'*80)
    if err:
        print('The following error has occurred:')
        print(err)
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

def test_script():
    '''
    Runs the test class inside the jchain src root directory.
    '''
    os.execlp('java' 'java', '-cp', class_d, 'jchain.TestBC')

def invoke(script):
    '''
    Attempts to invoke the script specified by \'script\'.
    script: The script to execute.
    '''
    try:
        if script == 'build':
            build_script()
        elif script == 'docgen':
            docgen_script()
        elif script == 'test':
            test_script()
        else:
            raise OSError('Specified script does not exist!')
    except OSError as e:
        usage(e)
        sys.exit(-1)

# entry point of the script
if __name__ == '__main__':
    if len(sys.argv) != 2:
        usage()
        sys.exit(-1)
    arg = sys.argv[1].lower()
    if arg == '-h' or arg == '--help':
        usage()
        sys.exit(0)
    invoke(sys.argv[1])
