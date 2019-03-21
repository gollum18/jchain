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

<<<<<<< HEAD

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

=======

Testing function 1...<br>
Block Hash: C75292B1A9F5ACC9CE52C52320388F88D85A0F51439CA0D53F18DFA5BBDDFAF8<br>
Transactions:<br>
TX Hash: 627E8536477E785A9454E8D6D77B71AB136B2C4CA78A73C2BDC7D7B31D277CF6<br>
Inputs:<br>
   A:40<br>
Outputs:<br>
   B:6

TX Hash: 5CA6231957DA09C6ACBEE31759C5D3F36DFED68E8523B436F4910CC433FC86E5<br>
Inputs:<br>
   A:28<br>
Outputs:<br>
   B:15

TX Hash: A0CE5B6209C7BF92BB2ABA2ABFDAEF1260E3F46EC2D5D12C61747B5BC9259779<br>
Inputs:<br>
   A:47<br>
Outputs:<br>
   B:42

TX Hash: 3543276DB1338007D0E9B642C43CF73896354EB5640FA3E0B9950B556CD5E654<br>
Inputs:<br>
   A:91<br>
Outputs:<br>
   B:71

TX Hash: F387D37DA2309A0D1D8C8237CECA1B960F43404AC57B317BE46050C401642228<br>
Inputs:<br>
   A:48<br>
Outputs:<br>
   B:28
   
Testing function 2...<br>
TX Hash: 627E8536477E785A9454E8D6D77B71AB136B2C4CA78A73C2BDC7D7B31D277CF6<br>
Inputs:<br>
   A:40<br>
Outputs:<br>
   B:6

>>>>>>> 15815294253c2ae4212c3a45604578e49ffe5f3a

---
## Existing Bugs

