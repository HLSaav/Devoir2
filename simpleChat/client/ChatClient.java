// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  /**
   *  The login ID variable. Allows to differentiate multiple clients.
   */
  String loginID;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String loginID, String host, int port, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    login();
  }


  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {
    //Determine if the line is a command
    if (message.startsWith("#"))
    {
      handleCommandFromClientUI(message.split(" "));
    }

    //Else send message to server
    else
    {
      try
      {
        sendToServer(message);
      }
      catch(IOException e)
      {
        clientUI.display
          ("Could not send message to server.  Terminating client.");
        quit();
      }
    }
  }

  public void handleCommandFromClientUI(String[] command)
  {

    switch(command[0]) {
      case "#quit":
        quit();
        break;
      case "#logoff":
        logoff();
        break;
      case "#setport":
        if(!isConnected())
        {
          try
          {
            setPort(Integer.parseInt(command[1]));
            clientUI.display("Port set to "+command[1]);
          }
          catch (Exception e)
          {
            clientUI.display("Error. Port not specified.");
          }
        }
        else
        {
          clientUI.display("Error. Client already connected to a server.");
        }
        break;
      case "#sethost":
        try
        {
          setHost(command[1]);
          clientUI.display("Host set to: "+ command[1]);
        }
        catch (Exception e) {}
        break;
      case "#login":
        login();
        break;
      case "#gethost":
        clientUI.display(getHost());
        break;
      case "#getport":
        clientUI.display(Integer.toString(getPort()));
        break;
      default:
        clientUI.display("Unrecognised command.");
        break;

    }
  }


public void connectionClosed() {
    clientUI.display
    ("Connection closed.");
}

public void connectionException(Exception exception) {
    clientUI.display
    ("WARNING - The server has stopped listening for connections." +
    "\nSERVER SHUTTING DOWN! DISCONNECTING!."+
    "\nAbnormal termination of connection.");
    //System.exit(1);
}


public void connectionEstablished()
{
  try
  {
    sendToServer("#login " + loginID);
  }
  catch (IOException e)
  {
    clientUI.display("Unexpected error sending loginID.");
  }
}

//Command-specific methods

  /**
   * This method closes the connection (logoff).
   */
  public void logoff()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
  }


  /**
   *  This method forces a new connection with the server only if the
   *  client is not already connected.
   */
  public void login()
  {
    if (isConnected()) {
      clientUI.display("Error. You're already logged in a server.");
      return;
    }
    try
    {
    openConnection();
    }
    catch(IOException e)
    {
      clientUI.display("Cannot open connection. Awaiting command.");
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
