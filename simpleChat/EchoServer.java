// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  ServerConsole serverUI;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ServerConsole serverUI)
    throws IOException
  {
    super(port);
    this.serverUI = serverUI;
  }



  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    //Determine if the message is a command, and handle the command.
    try
    {
      if (msg.toString().startsWith("#"))
      {
        handleCommandFromClient(msg.toString().split(" "), client);
        serverUI.display
        ("Message received: " + msg.toString() + " from " + client.getInfo("loginID"));
        return;
      }
    }
    catch (Exception E)
    {
      serverUI.display("Unexpected error when receiving message from client.");
    }

    // If message is not a command, send message to all clients with
    // a login ID.

    serverUI.display
      ("Message received: " + msg.toString() + " from " + client.getInfo("loginID"));

    this.sendToAllClients(client.getInfo("loginID") + ": " + msg.toString());

  }

  public void handleCommandFromClient(String[] command, ConnectionToClient client)
  {
    switch(command[0])
    {
      case "#login":
        try
        {
          if (client.getInfo("loginID") == null)
          {
            String loginID = command[1];
            client.setInfo("loginID", loginID);
            sendToAllClients(client.getInfo("loginID") + " has logged on.");
          }
          else
          {
            client.sendToClient
              ("Error. LoginID already established. Connection terminated.");
          }
        }
        catch (Exception e)
        {
          serverUI.display("Error. Unable to find a login ID.");
        }
        break;
      default:
        serverUI.display("Error. Unrecognised command.");
    }
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromServerUI(String message)
  {
    //Determine if the line is a command
    if (message.startsWith("#"))
    {
      handleCommandFromServerUI(message.split(" "));
    }

    //Else send message to server console and clients
    else
    {
      try
      {
        serverUI.display("SERVER MSG> : " + message);
        sendToAllClients("SERVER MSG> : " + message);
      }
      catch(Exception e)
      {
        serverUI.display
          ("Could not send message to server.  Terminating client.");
      }
    }
  }

  public void handleCommandFromServerUI(String[] command)
  {
    switch(command[0])
    {
      case "#quit":
        try
        {
          close();
        }
        catch (IOException e)
        {
          serverUI.display("Unexpected error while quitting the server.");
        }
        //serverUI.display("Server terminated.");
        System.exit(0);
        break;
      case "#stop":
        stopListening();
        break;
      case "#close":
        try
        {
          close();
        }
        catch (IOException e)
        {
          serverUI.display("Unexpected error while closing the server.");
        }

        break;
      case "#setport":
        try
        {
          setPort(Integer.parseInt(command[1]));
          serverUI.display("port set to: "+Integer.parseInt(command[1]));
        }
        catch (Exception e)
        {
          serverUI.display("Error setting the port.");
        }
        break;
      case "#start":
        if (!isListening() && (getNumberOfClients() == 0))
        {
          try
          {
            listen();
          }
          catch (Exception e)
          {
            serverUI.display("There's been an error starting the server.");
          }
        }
        else
        {
          serverUI.display("Cannot start server while it's currently running.");
        }
        break;
      case "#getport":
        serverUI.display("Current port :" + Integer.toString(getPort()));
        break;
      default:
        serverUI.display("Error. Unrecognised command.");
        break;
    }
  }


  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }

  protected void clientConnected(ConnectionToClient client)
  {
      serverUI.display
      ("A new client is attempting to connect to the server.");
      //sendToAllClients(client.getInfo("loginID")+ " has logged on.");

  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }


  // protected void serverClosed()
  // {
  //   serverUI.display
  //     ("Server is currently closed.");
  // }

  @Override
  public void clientDisconnected(ConnectionToClient client) {
      serverUI.display(client.getInfo("loginID") + " has disconnected.");
      sendToAllClients(client.getInfo("loginID") + " has disconnected.");
    }

  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  // public static void main(String[] args)
  // {
  //   int port = 0; //Port to listen on
  //
  //   try
  //   {
  //     port = Integer.parseInt(args[0]); //Get port from command line
  //   }
  //   catch(Throwable t)
  //   {
  //     port = DEFAULT_PORT; //Set port to 5555
  //   }
  //
  //   EchoServer sv = new EchoServer(port);
  //
  //   try
  //   {
  //     sv.listen(); //Start listening for connections
  //   }
  //   catch (Exception ex)
  //   {
  //     System.out.println("ERROR - Could not listen for clients!");
  //   }
  // }
}
//End of EchoServer class
