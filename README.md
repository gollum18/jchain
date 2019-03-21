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
To execute the `TestBC` class, execute the following command from the projects root directory:

`java -cp out jchain.TestBC`

---
## Sample Output
Example output from running the TestBC class:


> Retrieving the block at height 1:
> Block Hash: 550CAA59FBE773A69B470AB63FC5587ED0E885A1875827CB32104136FFA6C461
> Transactions: 
> TX Hash: ABE9B2252B3FC899EBDB0A5BA1F4B0B7807ADB09F6132A5652A0F03A6026C042
> Inputs:
>    A:54
> Outputs:
>    B:50
> 
> TX Hash: BB7962303AE353E48C443ED90E3C1FF1844A8FA7D150EA9FA73DA8F840A91A6C
> Inputs:
>    A:4
> Outputs:
>    B:0
> 
> TX Hash: 063DF9A5C07A3366FA4E963AA5FFE7336DDEE44D1CE61547DC553B2A81118A33
> Inputs:
>    A:66
> Outputs:
>    B:62
> 
> 
> 
> TX Hash: D0FAB6CC1423C8A2F35203F1E08437186B3ECE456229AFEB51B78FDC257FB926
> Inputs:
>    A:25
> Outputs:
>    B:19
> 
> TX Hash: CBC82F56670BF203DFCA64E3A08BF2E9C6B3FA4457D844794374B715475B5BD5
> Inputs:
>    A:75
> Outputs:
>    B:53
> 
> 
> 
> 
> Getting tx with hash: AE7A0568B96EEF526EC978BE6FC509E69456A8442316E29B2149A1C093FD9BF6 from the chain:
> TX Hash: AE7A0568B96EEF526EC978BE6FC509E69456A8442316E29B2149A1C093FD9BF6
> Inputs:
>    A:20
> Outputs:
>    B:15


---
## Existing Bugs

