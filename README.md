# Blockchain
**Project description:** https://hyperskill.org/projects/50?track=15

Here miners (= threads) mining blocks to add them into blockchain.

Every block store its hash number and hash number of the previous block.

First miner who found "magic number" add its' block to the blockchain.

Miners make transaction with virtual coins between each other. Used private-public key encription technology.

**Example of output:**

Block:  
Created by: miner0  
miner0 gets 100 VC  
Id: 1
Timestamp: 1668176835398 
Magic number: 1  
Hash of the previous block:  
0  
Hash of the block:   
a6c58cf449110f61edd7914ae491c619ebc4bb57bed060c66e28382a616565f7  
Block data:  
No transactions  
Block was generating for 0 seconds  
N was increased to 0  

Block:  
Created by: miner2  
miner2 gets 100 VC  
Id: 2  
Timestamp: 1668176836639  
Magic number: 1  
Hash of the previous block:   
a6c58cf449110f61edd7914ae491c619ebc4bb57bed060c66e28382a616565f7  
Hash of the block:   
6b0c9e0474f526bedec64dba59b8f21fd49a91b5159a817d5b2464ffbfb41f94  
Block data:  
miner2 sent 42 VC to miner1  
Block was generating for 0 seconds  
N was increased to 1  

Block:  
Created by: miner9  
miner9 gets 100 VC  
Id: 3  
Timestamp: 1668176836642  
Magic number: 13  
Hash of the previous block:   
6b0c9e0474f526bedec64dba59b8f21fd49a91b5159a817d5b2464ffbfb41f94  
Hash of the block:   
0153ffc06ed76f1fa100bb744f0e5fc4f764b029d068e447c32bd0c5035b5e39  
Block data:  
No transactions  
Block was generating for 0 seconds  
N was increased to 2  


**P.S.** User doesn't input here.
