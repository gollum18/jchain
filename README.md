# JChain Documentation
##### (or what passes for it)

---
## Project Description

---
## Build/Compile Instructions
There are three ways to compile JChain:
- Compile with build.py ***This is the recommended method of compiling***
- Compile within your IDE *This should be fine as long as you correctly import the project*
- Compile by hand *Not Recommended Ever...*

### Compiling with build.py
To compile with build.py:

**`python build.py`**

### Compiling with your IDE
Maintainers should be able to figure this out on their own, I do not plan on including instructions here as it is IDE dependent.

### Compiling by hand
To compile by hand, you can compile all of the files by compiling TestBC:

**`javac -cp ./src -d ./out ./src/jchain/TestBC.java`**

Note: I advise against compiling by hand and would instead, very strongly point you to use the *build.py* script or your IDE. Compiling by hand outside of the above command requires knowledge on Java classpath and the structure of the project in order to compile successfully. *build.py* will handle this for you and your IDE will maintain this for you, so you don't have to worry about it.

The above command may not work in the future. It just coincidentally ocmpiles successfully because `TestBC.java` references the required java files to make it work.

---
## Execution Instructions
To execute the `TestBC` class, execute the following command from the projects root directory:

**`java -cp out jchain.TestBC`**

---
## Sample Output
Example output from running the TestBC class:


> Retrieving the block at height 1:
> Block Hash: BFAAD22C1473B811DDE66EBF88D1CA81DB1B9CF4F9CF47FE25BE80A28033C113
> Transactions: 
> TX Hash: 4CF7FE31C8931587B3F13BA43FD17D834F09740BD0F56E59E06354AF85590840
> Inputs:
>    A:88
> Outputs:
>    B:0
> 
> TX Hash: D88FE0E344582DB5BB3921A9546DB5048CE1DC973A6F14686DD45E3372AB7473
> Inputs:
>    A:7
> Outputs:
>    B:1
> 
> 
> TX Hash: 89847C6C612B8CC8078F7A2636D0C5A797A7F6BD7C8C17199E124CDA53CA31E2
> Inputs:
>    A:25
> Outputs:
>    B:23
> 
> 
> TX Hash: 74D72F24485A0427DF8BC82A09025C2A7B1CF014B789A41FCBBB08E3727ABAAD
> Inputs:
>    A:31
> Outputs:
>    B:21
> 
> TX Hash: C7C2878E177F8ED13D5CEA88A109DA37A10CBF276BB91D52364B9EE5167A3CD0
> Inputs:
>    A:11
> Outputs:
>    B:1
> 
> 
> 
> 
> Getting tx with hash: 5FEBBA7A677029BCCE02988CC374FDFBA8E98690712A860910D0E8597640CA7B from the chain:
> TX Hash: 5FEBBA7A677029BCCE02988CC374FDFBA8E98690712A860910D0E8597640CA7B
> Inputs:
>    A:20
> Outputs:
>    B:4


---
## Existing Bugs

