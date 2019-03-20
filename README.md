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

`java -cp ./out jchain.TestBC`

---
## Sample Output
Example output from running the TestBC class:


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


---
## Existing Bugs

