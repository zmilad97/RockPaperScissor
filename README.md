# RockPaperScissor
Rock Paper Scissor game (Limited) 

It's a Rock,Paper,Scissor game but with differents rules that built using java. read game details below .

## Getting Started

### Start Server
#### build using maven :

```
git clone https://github.com/zmilad97/RockPaperScissor.git
cd RockPaperScissor
mvn package
mvn exec:java
```

#### using docker : 

```
docker pull zmilad97/rps-game:latest
docker run -p 6060:6060 -p 4567:4567 zmilad97/rps-game:latest
```



#### Connect Players
players can connect to the server using " telnet " in any terminal simulator
default port is 6060
if you run on localhost you can use below codes


```
telnet localhost 6060
```



## Game Details
Players have limited cards of each rock, paper and scissor and all cards count shows before every game
Each player has 3 lives.

### Lose Condition
The Player lost if he left with 0 life or ran out of cards and his life was under 3.

### Win condition
Player win if he has 3 or more lives and ran out of cards or all player left and he has 3 or more lives.

