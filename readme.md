# CSCI2020U Quiz Game Group Project
A 4-player Quiz Game using socket programming in JavaFX with hosting capabilities. Answer multiple-choice questions with 4 possible answers in a round-based quiz game racing against the time against your fellow players in the lobby! Try to achieve the highest score in order to win!
## Authors
Bridget Green - bridget.green@ontariotechu.net

Azan Sikder - azan.sikder1@gmail.com

Dewan Mohammadd Tasinuzzaman - zmn.tasin@gmail.com

Lexa Torrance - lexa.torrance@outlook.com

## Instructions

### Client
1. Enter username and IP. (Enter IP as localhost:####) The port is displayed on the server (Step 2 of Server Instructions)
2. Connect to the game 
 
![image](https://user-images.githubusercontent.com/71111397/163507444-6583c413-b666-4ead-80b3-ceb60efe3533.png)

4. Choose to either join a game or create a new game
>If Join Game is chosen, skip to Step 6

4. New Game. Choose between Random or Selected. Choose the number of rounds. Password can be made by the host (user) or left blank. 
 If you wish to go back, choose Prev. Otherwise, click Next
 
 ![image](https://user-images.githubusercontent.com/71111397/163507880-e2dfb024-e1a4-42a1-b0a8-1a0641d201e5.png)

5. Wait for others to join. The host can choose next when they wish to start the game. Only the host can choose to proceed or go back.

![image](https://user-images.githubusercontent.com/71111397/163508015-ce0e63a2-4072-4256-801b-64dd74af5a01.png)


7. If you are joining a game, enter the appropriate code and password   

![image](https://user-images.githubusercontent.com/71111397/163508058-2b05ed81-efeb-4a96-ae5c-fc3fd8c9ba73.png)

9. The game starts
10. If you are the host, you choose the Question by inputting it into the TextField

![image](https://user-images.githubusercontent.com/71111397/163508227-44bb74a4-2070-4ad3-ae6e-501a25ba7e23.png)


12. After choosing the question, choose 4 possible answers by inputting them one at a time. Click Next to enter each answer. Select which answer is the correct answer. You can click Prev to return to 8. Otherwise, click next to proceed

![image](https://user-images.githubusercontent.com/71111397/163508300-a51b252a-2173-43e1-800b-85b08d0d19d8.png)

14. If you are not the host, select whichever button you think has the correct answer 
15. If you are the host, you can see how many people have selected an answer, as well as which one. Round finishes when everyone has answered or when time is up.
16. Screen displays the correct answer
17. A Scoreboard is shown, and the host is in? place. After this, the next round starts with someone else as the host
18. Repeat for as many rounds as you have selected back at 4

### Server
1. 
```bash
java -jar Server.jar
```
2. Let client's know port number

![image](https://user-images.githubusercontent.com/71111397/163507796-9ae22601-1f02-40bf-bde0-89328c8432ae.png)

## Usage

## Video Demo
>INSERT VIDEO HERE
