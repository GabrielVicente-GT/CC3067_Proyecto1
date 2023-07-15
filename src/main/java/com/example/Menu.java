
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
                        // LogIn(scanner);
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
            System.out.print("Ingrese su nombre de usuario: ");
            String username = scanner.next();
            
            System.out.print("Ingrese su contraseña: ");
            String password = scanner.next();
            
            String domain = "alumchat.xyz";
            
            try {
                    XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain("alumchat.xyz")
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

                System.out.println("¡Registro exitoso en " + domain + "!");
            } catch (SmackException | IOException | XMPPException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error en el registro: " + e.getMessage());
            }
        }


}

