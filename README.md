# Description
Hi! You are welcome here! The point of the game is to get all the boxes in the docks.

To run this game you will need Android Studio and Android Smartphone or an emulator built into the IDE. This game can download levels from a server, to make it work, you will need to do the steps below.

# Instruction for server

1. First, you need to install a utility that connects to the server via SSH. In my case I used PuttY.
2. Then check if you can connect to it. If so, download a tool which connects to the server via SCP. In my case I used WinSCP. This is to make it easier for you to transfer files.
3. If the server does not have a JDK you will have to install it.
4. After that you can archive server directory and move it to the server.
5. Unzip it there. The executable jar file you need is located at /server/artifacts/SokobanGameServer_jar/SokobanGameServer.jar
6. Run it with the command bellow and the server will work.

```bash
java -jar command SokobanGameServer.jar
```

# Author
Atabek Shamshidinov
Made for InternLabs by NurTelecom
