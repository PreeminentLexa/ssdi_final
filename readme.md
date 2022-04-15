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

3. Choose to either join a game or create a new game
>If Join Game is chosen, skip to Step 6

4. New Game. Choose between Random or Selected. Choose the number of rounds. Password can be made by the host (user) or left blank. 
 If you wish to go back, choose Prev. Otherwise, click Next
 
 ![image](https://user-images.githubusercontent.com/71111397/163507880-e2dfb024-e1a4-42a1-b0a8-1a0641d201e5.png)

5. Wait for others to join. The host can choose next when they wish to start the game. Only the host can choose to proceed or go back.

![image](https://user-images.githubusercontent.com/71111397/163508015-ce0e63a2-4072-4256-801b-64dd74af5a01.png)

![image](https://user-images.githubusercontent.com/71111397/163520901-46e8147d-e7cf-4c39-85ab-59652b0ad04a.png)

6. If you are joining a game, enter the appropriate code and password   

![image](https://user-images.githubusercontent.com/71111397/163508058-2b05ed81-efeb-4a96-ae5c-fc3fd8c9ba73.png)



7. The game starts
8. A random user is chosen and is prompted to write a question

![image](https://user-images.githubusercontent.com/71111397/163565679-18507093-6c3c-4900-bb19-d82a3be0f4a6.png)


9. After choosing the question, choose 4 possible answers and then select which one is correct

![image](https://user-images.githubusercontent.com/71111397/163565699-738d3980-1630-4d3f-9a9b-dd76103e578f.png)

10. The question and answer is shown on screen for everyone to see, everyone but the questioner chosen can answer. Round finishes when everyone has answered or when time is up.


11. Screen displays the correct answer

![image](https://user-images.githubusercontent.com/71111397/163565839-a450138b-07db-4ce6-9cd1-031a9a897a2f.png)

12. A Scoreboard is shown showing who is winning. After this, the next round starts with someone else as the questioner

![image](https://user-images.githubusercontent.com/71111397/163565881-0ec19852-48f4-4c90-81fa-9d77194485c2.png)

13. Repeat for as many rounds as you have selected
14. When the game ends, all users are shown if they lost or won with scores included

![image](https://user-images.githubusercontent.com/71111397/163565979-472b6091-829d-49ac-9b11-180c4e617bc0.png)

![image](https://user-images.githubusercontent.com/71111397/163565984-b8faf5ac-8814-45ae-8b3a-61bd76ed882d.png)

### Server
1. 
```bash
java -jar Server.jar
```
2. Let client's know port number

![image](https://user-images.githubusercontent.com/71111397/163507796-9ae22601-1f02-40bf-bde0-89328c8432ae.png)


## Video Demo
[LINK](https://www.youtube.com/watch?v=zjqeh3lGpIM)
[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/zjqeh3lGpIM/0.jpg)](https://www.youtube.com/watch?v=zjqeh3lGpIM)
