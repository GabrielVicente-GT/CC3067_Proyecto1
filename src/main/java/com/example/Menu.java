
/*

*   Project 1 - Using an existing protocol
*   Author: Gabriel Vicente 20498

This project aims to implement a protocol based on standards. understand the purpose of the XMPP protocol, 
understand how the services of the XMPP protocol work. and understand the basics of asynchronous programming 
required for some of the network development needs.


It is a simple project that will consist of a class with different methods which allow, through the smack library,
to manipulate xml and thus communicate with the "alumchat.xyz" domain.

*/

// * For this project, maven was used to manage the project developed in java. 
// * The Group Id that comes from default was used 

package com.example;

import java.io.IOException;
// import java.lang.reflect.Method;

// * The main library used in this project is Smack and then the specific methods 
// * used are called, the dependencies are inside the pom.xml file.

import java.util.Scanner;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.parts.Localpart;


// * The function of the menu class corresponds to the logic of the way in which 
// * the user can navigate within the project. If a controller view model were used,
// *  it would correspond to the controller but for time reasons the view was a 
// * problem when using it within other methods.

public class Menu 
{

    public static void main( String[] args )
    {

        // * Unique welcome print

        System.out.println("\n ----------------------------------------------------------------------------------------------------");
        System.out.println(" ------------------------------ Project 1 - Using an existing protocol ------------------------------");
        System.out.println(" ---------------------------------------------------------------------- Author: Gabriel Vicente 20498\n");

        // * Scanner instance to receive terminal information
        Scanner scanner = new Scanner(System.in);
        

        // * User selection
        int choice;


        // * Main cycle of the Account Management menu
        do {
            System.out.println("\nAccount Management Menu:\n");
            System.out.println("1.) Register a new account on the server");
            System.out.println("2.) Sign in with an account");
            System.out.print("Select an option: ");

            
            try {
                choice = scanner.nextInt();

                // * User selection

                switch (choice) {
                    case 1:
                        RegisterUser(scanner);
                        break;
                    case 2:
                        System.out.println("Login in...");
                        // In progres
                        LogIn(scanner);
                        break;
                    default:
                        System.out.println("\n--> Invalid option. Please select an option from 1 to 4.\n");
                }
                
            } catch (Exception e) {
                System.out.println("\n--> Invalid option. Please select an option from 1 to 4.\n");
                scanner.nextLine();
                choice = 0;
            }

        } while (choice != 4);

        scanner.close();
    }


    // * Method that allows the registration of a user. The following video was taken as a reference https://youtu.be/iAuc3wp5Mt4
    public static void RegisterUser(Scanner scanner) {
            System.out.print("User Name: ");
            String username = scanner.next();
            
            System.out.print("Password");
            String password = scanner.next();
            
            String domain = "alumchat.xyz";
            
            try {
                    XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain(domain)
                    .setHost(domain)
                    .setUsernameAndPassword(username, password)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();
            
                    AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                    connection.connect();

                    AccountManager accountManager = AccountManager.getInstance(connection);
                    accountManager.sensitiveOperationOverInsecureConnection(true);
                    accountManager.createAccount(Localpart.from(username), password);

                    connection.disconnect();

                System.out.println("Now you are registered in  " + domain + "!");
            } catch (SmackException | IOException | XMPPException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("Failed to register:  " + e.getMessage());
            }
        }

        private static void LogIn(Scanner scanner) {
            System.out.println("User name: ");
            String username = scanner.next();
            System.out.println("Password");
            String password = scanner.next();
            String domain = "alumchat.xyz";
            try {
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(domain)
                .setHost(domain)
                .setUsernameAndPassword(username, password)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();
    
                AbstractXMPPConnection connection = new XMPPTCPConnection(config);
                connection.connect();
                
                if (connection.isConnected()) {
    
    
                    try {
                            
    
                        connection.login(username, password);
    
                        if (connection.isAuthenticated()) {
                            System.out.println("You are in!\n");
                        } else {
                            System.out.println("Get out! Register first.\n");
                        }
    
                        int choice;
    
    
                        // * Main cycle of the User Menu
                        do {
                            System.out.println("\nUser Menu:\n");
                            System.out.println("1.) Show all contacts and their status");
                            System.out.println("2.) Add a user to contacts");
                            System.out.println("3.) Show contact details of a user");
                            System.out.println("4.) 1 to 1 communication with any user/contact");
                            System.out.println("5.) Participate in group conversations");
                            System.out.println("6.) Define presence message");
                            System.out.println("7.) Send/receive notifications");
                            System.out.println("8.) Send/receive files");
                            System.out.println("9.) Log Out");
                            System.out.println("10.) Delete Account\n");
                            System.out.print("Select an option: ");
    
                            
                            try {
                                choice = scanner.nextInt();
    
                                // * User selection
    
                                switch (choice) {
                                    case 1:
                                        System.out.println("Option -->   1");
                                        // printContactInfo(connection);
                                        break;
                                    case 2:
                                        System.out.println("Option -->   2");
                                        // addContact(connection, scanner);
                                        break;
                                    case 3:
                                        System.out.println("Option -->   3");
                                        // showUserDetails(connection, scanner);
                                        break;
                                    case 4:
                                        System.out.println("Option -->   4");
                                        // sendChatMessage(connection, scanner);
                                        openChat(connection, scanner);
                                        break;
                                    case 5:
                                        System.out.println("Option -->   5");
    
                                        break;
                                    case 6:
                                        System.out.println("Option -->   6");
    
                                        break;
                                    case 7:
                                        System.out.println("Option -->   7");
                                        break;
                                    case 8:
                                        System.out.println("Option -->   8");
                                        break;
                                    case 9:
                                        System.out.println("Option -->   9");
                                        // connection.disconnect();
                                        choice = 12;
                                        break;
                                    case 10:
                                        System.out.println("Option -->   10");
                                        // DeleteUser(connection);
                                        // connection.disconnect();
                                        choice = 12;
                                        break;
                                    default:
                                        System.out.println("\n--> Invalid option. Please select an option from 1 to 4.\n");
                                }
                                
                            } catch (Exception e) {
                                System.out.println("\n--> Invalid option. Please select an option from 1 to 4.\n");
                                scanner.nextLine();
                                choice = 0;
                            }
    
                        } while (choice != 12);
                    } catch (Exception e) {
                        System.out.println("Get out! Register first.\n");
                    }
                } else {
                    System.out.println("Failed to log in... try again");
                }
    
            } catch (Exception e) {
                System.out.println("Failed to log in... try again");
                e.printStackTrace();
            }
        }

        private static void openChat(AbstractXMPPConnection connection, Scanner scanner) {
            try {
                System.out.print("User Name you wanna talk: ");
                String recipientUsername = scanner.next() + "@" + "alumchat.xyz";
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                EntityBareJid jid = JidCreate.entityBareFrom(recipientUsername);
                Chat chat = chatManager.chatWith(jid);
    
    
                chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    System.out.println("New message from " + from + ": " + message.getBody());
                }
                });
    
                Boolean chating = true;
    
                while (chating) {
                    System.out.print(" >> ");
                    String message = scanner.nextLine();
    
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Leaving the chat");
                        chating = false;
                        break;
                    }
    
                    chat.send(message);
                    System.out.println("Message sended:  " + message);
                }
    
            } catch (Exception e) {
                System.out.println("Failed to chat... try again");
                e.printStackTrace();
            }
        }
    

}

