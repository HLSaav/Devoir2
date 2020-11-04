
To compile a java file, open the batch in the directory
/simpleChat. Then execute the javac command like :

javac -cp "./simpleChat" ./simpleChat/ClientConsole.java

Note how you indicate the classpath despite running the compilation
on that same directory.

Have 3 windows on your terminal.

First one to compile code using commands on Devoir2 dir:

    javac -cp "./simpleChat" ./simpleChat/ClientConsole.java

    javac -cp "./simpleChat" ./simpleChat/EchoServer.java

    javac -cp "./simpleChat" ./simpleChat/client/ChatClient.java  

    javac -cp "./simpleChat" ./simpleChat/ServerConsole.java  

Second one to run EchoServer class on simpleChat directory

    java EchoServer

Third one to run ChatClient on simpleChat/client directory

    java ChatClient
