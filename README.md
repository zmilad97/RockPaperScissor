# RockPaperScissor
Rock Paper Scissor game (Limited) 

It's a Rock,Paper,Scissor game but with differents rules that built using java. read game details below .

## Getting Started

### Start com.github.zmilad97.rps.Server
build a jar file and run it using below commands. 

```
git clone https://github.com/zmilad97/RockPaperScissor.git
cd RockPaperScissor
./mvnw package
java -jar target/*.jar
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
The com.github.zmilad97.rps.model.Player lost if he left with 0 life or ran out of cards and his life was under 3.

### Win condition
com.github.zmilad97.rps.model.Player win if he has 3 or more lives and ran out of cards or all player left and he has 3 or more lives.

