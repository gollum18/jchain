# JChain Documentation
##### (or what passes for it)

---
## Project Description

---
## Build/Compile Instructions
There are three ways to compile JChain:
- Compile with build.py
- Compile within your IDE
- Compile by hand

### Compiling with build.py
To compile with build.py:
`python build.py`

### Compiling with your IDE
Maintainers should be able to figure this out on their own, I do not plan on including instructions here as it is IDE dependent.

### Compiling by hand
To compile by hand, you can compile all of the files by compiling TestBC:

`javac -cp ./src -d ./out ./src/jchain/TestBC.java`

Note: I advise against compiling by hand and would instead, very strongly point you to use the *build.py* script or your IDE. Compiling by hand outside of the above command requires knowledge on Java classpath and the structure of the project in order to compile successfully. *build.py* will handle this for you and your IDE will maintain this for you, so you don't have to worry about it.

The above command may not work in the future. It just coincidentally ocmpiles successfully because `TestBC.java` references the required java files to make it work.

---
## Execution Instructions
To execute the `TestBC` class, enter the following command from the root directory of this project:

`java ./out/jchain/jchain.TestBC` on Linux/Mac or

`java .\out\jchain\jchain.TestBC` on Windows

or cd into the jchain directory in out and enter the following command
`java jchain.TestBC`

---
## Sample Output


---
## Existing Bugs

