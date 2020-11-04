import java.util.Scanner;

import common.*;

import java.io.*;



public class ServerConsole implements ChatIF{

  private static int DEFAULT_PORT = 5555;

  //The instance of the server that created this ConsoleServer.
  private EchoServer server;

  Scanner fromConsole;


  public ServerConsole(int port)
  {
    try
    {
      server = new EchoServer(port, this);

    }
    catch (IOException exception)
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating server.");
      System.exit(1);
    }

    // Create scanner object to read from console
    fromConsole = new Scanner(System.in);
  }

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the servers's message handler.
   */
  public void accept()
  {
    try
    {

      String message;

      while (true)
      {
        message = fromConsole.nextLine();
        server.handleMessageFromServerUI(message);

      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  public void display(String message) {
    System.out.println("> " + message);
  }


  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    ServerConsole log = new ServerConsole(port);

    try
    {
      log.server.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    log.accept();
  }



}
