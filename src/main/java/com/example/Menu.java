
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

import java.io.FileInputStream;
import java.util.Base64;
import java.io.IOException;

// * The main library used in this project is Smack and then the specific methods 
// * used are called, the dependencies are inside the pom.xml file.

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
// import org.jivesoftware.smack.chat2.MultiUserChat;
import org.jivesoftware.smack.packet.Message;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.jid.parts.Resourcepart;


// * The function of the menu class corresponds to the logic of the way in which 
// * the user can navigate within the project. If a controller view model were used,
// *  it would correspond to the controller but for time reasons the view was a 
// * problem when using it within other methods.

public class Menu 
{

    public static void main( String[] args )
    {

        // * Unique welcome print

        System.out.println("\n ----------------------------------------------------------------------");
        System.out.println(" --------------- Project 1 - Using an existing protocol ---------------");
        System.out.println(" ---------------------------------------- Author: Gabriel Vicente 20498\n");

        // * Scanner instance to receive terminal information
        Scanner scanner = new Scanner(System.in);

        // * User selection
        int choice;


        // * Main cycle of the Account Management menu
        do {
            System.out.println("\nAccount Management Menu:\n");
            System.out.println("1.) Register a new account on the server");
            System.out.println("2.) Sign in with an account");
            System.out.println("3.) Bye client!");
            System.out.print("Select an option: ");

            
            try {
                choice = scanner.nextInt();

                // * User selection

                switch (choice) {
                    case 1:
                        RegisterUser(scanner);
                        break;
                    case 2:
                        System.out.println("\nLogin in...\n");
                        LogIn(scanner);
                        break;
                    case 3:
                        System.out.println("\nBye Bye!!!!!!!! :D\n");
                        choice = 4;
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
            
            System.out.print("Password: ");
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

        // The big menu it is gonna be LogIn Because it has the connection with de domain, so its is easier if the user select from here
        // and I just call the ramaining funcions

        private static void LogIn(Scanner scanner) {
            System.out.print("User name: ");
            String username = scanner.next();
            System.out.print("Password: ");
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

                        // if the connection it is succesful we can continue, otherwise the program stops

                        connection.login(username, password);
    
                        if (connection.isAuthenticated()) {
                            System.out.println("You are in!\n");
                        } else {
                            System.out.println("Get out! Register first.\n");
                        }
    
                        // This is the principal choice that allows to use all the functions possibles
                        int choice;

                        // * This listener is always ready to receives files and messages and print in a fancy way
                        ChatManager listening = ChatManager.getInstanceFor(connection);

                        listening.addIncomingListener(new IncomingChatMessageListener() {
                        @Override
                        public void newIncomingMessage(EntityBareJid sender, Message text, Chat chat) {
                            if(!text.getBody().toString().equals("")){
                                if(isBase64Encoded(text.getBody().toString())){
                                    byte[] decodedBytes = Base64.getDecoder().decode(text.getBody().toString());
                                    String decodedString = new String(decodedBytes);
                                    System.out.println("\nText file from  " +sender +" : " + decodedString);
                                    System.out.println("Original text   : " + text.getBody());
                                    System.out.print("\n>> ");
                                }else{
                                    System.out.println("\nMessage from "+sender + ": " + text.getBody());
                                    System.out.print("\n>> ");
                                }

                            }
                        }
                        
                        // * This function allows to encode the file
                        
                        public boolean isBase64Encoded(String input) {
                            String base64Pattern = "^[A-Za-z0-9+/]*={0,2}$";
                            Pattern pattern = Pattern.compile(base64Pattern);
                            Matcher matcher = pattern.matcher(input);
                            return matcher.matches();
                        }
                    });

                        // This listener is destined to always listen to notification presence about chances in the state and mode, 
                        try {
                            StanzaFilter presenceFilter = new StanzaTypeFilter(Presence.class);
                            connection.addAsyncStanzaListener(new StanzaListener() {
                                @Override
                                public void processStanza(Stanza stanza) {
                                    if (stanza instanceof Presence) {
                                        Presence presence = (Presence) stanza;
                                        if (presence.getType() == Presence.Type.available) {
                                            System.out.println("\n\nNotification: " + presence.getFrom() + "\nNow my mode is " + presence.getMode());
                                        } else if(presence.getType() == Presence.Type.unavailable){
                                            System.out.println("\n\nNotification: " + presence.getFrom() + "\nis " + presence.getStatus() + "\n");
                                        }
                                System.out.println("\n0.) Refresh all!");
                                System.out.println("1.) Show all contacts and their status");
                                System.out.println("2.) Add a user to contacts");
                                System.out.println("3.) Show contact details of a user");
                                System.out.println("4.) 1 to 1 communication with any user/contact");
                                System.out.println("5.) Participate in group conversations");
                                System.out.println("6.) Define presence message (And change status)");
                                System.out.println("7.) Send files");
                                System.out.println("8.) Log Out");
                                System.out.println("9.) Delete Account\n");
                                System.out.print("Select an option: ");
                                    }
                                }
                            }, presenceFilter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        


                        // * Main cycle of the User Menu
                        do {
                            Roster roster = Roster.getInstanceFor(connection);
                            roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
                            System.out.println("\nUser Menu:\n");

                            System.out.println("\n0.) Refresh all!");
                            System.out.println("1.) Show all contacts and their status");
                            System.out.println("2.) Add a user to contacts");
                            System.out.println("3.) Show contact details of a user");
                            System.out.println("4.) 1 to 1 communication with any user/contact");
                            System.out.println("5.) Participate in group conversations");
                            System.out.println("6.) Define presence message (And change status)");
                            System.out.println("7.) Send files");
                            System.out.println("8.) Log Out");
                            System.out.println("9.) Delete Account\n");
                            System.out.print("Select an option: ");
    
                            
                            try {
                                choice = scanner.nextInt();
    
                                // * User selection
    
                                switch (choice) {

                                    // * Every choice leads to a funcion that only requires conecction to funcion
                                    case 0:
                                        System.out.println("Option -->   0");
                                        RefreshRequests(connection);
                                        break;
                                    case 1:
                                        System.out.println("Option -->   1");
                                        Contacts(connection);
                                        break;
                                    case 2:
                                        System.out.println("Option -->   2");
                                        AddContact(connection, scanner);
                                        break;
                                    case 3:
                                        System.out.println("Option -->   3");
                                        SpecificContact(connection, scanner);
                                        break;
                                    case 4:
                                        System.out.println("Option -->   4");
                                        ChatWithContact(connection, scanner);
                                        break;
                                    case 5:
                                        System.out.println("Option -->   5");
                                        chatMenu(connection, scanner);
                                        break;
                                    case 6:
                                        System.out.println("Option -->   6");
                                        ChangeStatusPresence(connection, scanner);
                                        break;
                                    case 7:
                                        System.out.println("Option -->   7");
                                        FileToContact(connection, scanner);
                                        break;
                                    case 8:
                                        System.out.println("Option -->   8");
                                        connection.disconnect();
                                        choice = 12;
                                        break;
                                    case 9:
                                        System.out.println("Option -->   9");
                                        DeleteAccount(connection);
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
                        System.out.println("Get out! Register first please.\n");
                    }
                } else {
                    System.out.println("Failed to log in... try again");
                }
    
            } catch (Exception e) {
                System.out.println("Failed to log in... try again");
                e.printStackTrace();
            }
        }

        // * This function allows to create a chat and interact with messages with anothe user
        // * this link was usefull to create this funcion and with IA some corrections were made
        // * The reference of this link it is used https://www.baeldung.com/xmpp-smack-chat-client

        private static void ChatWithContact(AbstractXMPPConnection connection, Scanner scanner) {
            try {
                System.out.print("User Name you wanna talk: ");
                String recipientUsername = scanner.next() + "@" + "alumchat.xyz";
                scanner.nextLine();
                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                EntityBareJid jid = JidCreate.entityBareFrom(recipientUsername);
                Chat chat = chatManager.chatWith(jid);
    
                Boolean chating = true;
    
                while (chating) {
                    System.out.print(" >> ");
                    String message = scanner.nextLine();

                    if (message.equalsIgnoreCase("/LEAVE")) {
                        System.out.println("\nLeaving the chat\n");
                        chating = false;
                        break;
                    }
                    message = message + " ";
                    chat.send(message);
                }
    
            } catch (Exception e) {
                System.out.println("Failed to chat... try again");
                e.printStackTrace();
            }
        }
        
        // * This function is like a child from the previus one that shows all contact information
        // * perplexity was usefull in this function
        private static void SpecificContact(AbstractXMPPConnection connection, Scanner scanner) {
            try {
                Roster roster = Roster.getInstanceFor(connection);
        
                System.out.print("User Name to see minimal info: ");
                String username = scanner.next() + "@" + "alumchat.xyz";
        
                RosterEntry entry = roster.getEntry(JidCreate.bareFrom(username));
        
                if (entry != null) {
                    System.out.println("\nUser Details:\n");
                    System.out.println("User: " + entry.getJid().getLocalpartOrNull().toString());
                    System.out.println("Presence message: " + roster.getPresence(entry.getJid()).getStatus());
                    System.out.println("Modo: " + roster.getPresence(entry.getJid()).getMode());
                    System.out.println("Status: " + roster.getPresence(entry.getJid()).getType()+ "\n");
                } else {
                    System.out.println("The user " + username + " is not in the contact list");
                }
        
            } catch (Exception e) {
                System.out.println("ERROR: details not available");
                e.printStackTrace();
            }
        }

        // * Reference: https://stackoverflow.com/questions/31498985/delete-account-using-smack
        // * it was usefull to clarify the utility of account manager

        private static void DeleteAccount(AbstractXMPPConnection connection) {
            String domain = "alumchat.xyz";
            try {

                AccountManager accountManager = AccountManager.getInstance(connection);
                accountManager.sensitiveOperationOverInsecureConnection(true);
                accountManager.deleteAccount();

                System.out.println("¡User deleted from " + domain + "!");
            } catch (SmackException | XMPPException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("ERROR deleting account : " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ERROR deleting account : " + e.getMessage());
            }
        }

        // * It is like the singular shown information of a contact but insted of asking just throw all the infomation
        private static void Contacts(AbstractXMPPConnection connection) {
            try {
                Roster roster = Roster.getInstanceFor(connection);
                System.out.println("\nContact List:\n");
    
                for (RosterEntry entry : roster.getEntries()) {
                    System.out.println("User: " + entry.getJid().getLocalpartOrNull().toString());
                    System.out.println("Presence message: " + roster.getPresence(entry.getJid()).getStatus());
                    if(roster.getPresence(entry.getJid()).getType().toString().equals("unavailable")){
                        System.out.println("Status: " + roster.getPresence(entry.getJid()).getType()+ "\n");
                    }else{
                        System.out.println("Modo: " + roster.getPresence(entry.getJid()).getMode());
                        System.out.println("Status: " + roster.getPresence(entry.getJid()).getType()+ "\n");
                    }
                    
                }
            } catch (Exception e) {
                System.out.println("Error while fetching contact information.");
                e.printStackTrace();
            }
        }

        // *  This reference have all the basic information, and for add a contact it was usefull
        //  * https://www.baeldung.com/xmpp-smack-chat-client

        private static void AddContact(AbstractXMPPConnection connection, Scanner scanner) {
            try {
                
                Roster roster = Roster.getInstanceFor(connection);
        
                System.out.print("User Name to add:  ");
                String username = scanner.next() + "@" + "alumchat.xyz";
        
                roster.sendSubscriptionRequest(JidCreate.entityBareFrom(username));
                roster.sendSubscriptionRequest(JidCreate.entityBareFrom(username));
                System.out.println("Request sent to : " + username);
            } catch (Exception e) {
                System.out.println("ERROR trying to request");
                e.printStackTrace();
            }
        }

        
        // * This was the most repetitive function, since it was the same for each state, it was based on the five states that gajim 
        // * shows and the mode and status were tested to see what the settings were for each state. AI was used to know how to make 
        // * the notification and in this way polish some redundancies that were

        private static void ChangeStatusPresence(AbstractXMPPConnection connection, Scanner scanner) {
            try {

                System.out.println("\n1.) Available");
                System.out.println("2.) Absent");
                System.out.println("3.) Not available");
                System.out.println("4.) Busy");
                System.out.println("5.) Disconnected");
                System.out.println("\nSelect a status:");
                
                int view = scanner.nextInt();
                scanner.nextLine();
                String message;
                Presence presence;
                Roster miniroster;
                switch (view) {
                    case 1:
                        System.out.println("\nYou selected: Available");
                        System.out.print("\nNew Presences Message: ");
                        message = scanner.nextLine();
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.available);
                        presence.setStatus(message);

                        connection.sendStanza(presence);
                        System.out.println("Presence message changed successfully!");

                        miniroster = Roster.getInstanceFor(connection);
                        for (RosterEntry entry : miniroster.getEntries()) {
                            Presence updatedPresence = new Presence(Presence.Type.available);
                            updatedPresence.setMode(Presence.Mode.available);
                            updatedPresence.setStatus(message);
                            updatedPresence.setTo(entry.getJid());
                            connection.sendStanza(updatedPresence);
                        }
                        break;
                    case 2:
                        System.out.println("\nYou selected: Absent");
                        System.out.print("\nNew Presences Message: ");
                        message = scanner.nextLine();
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.away);
                        presence.setStatus(message);

                        connection.sendStanza(presence);
                        
                        System.out.println("Presence message changed successfully!");
                        miniroster = Roster.getInstanceFor(connection);
                        for (RosterEntry entry : miniroster.getEntries()) {
                            Presence updatedPresence = new Presence(Presence.Type.available);
                            updatedPresence.setMode(Presence.Mode.away);
                            updatedPresence.setStatus(message);
                            updatedPresence.setTo(entry.getJid());
                            connection.sendStanza(updatedPresence);
                        }
                        break;
                    case 3:
                        System.out.println("\nYou selected: Not available");
                        System.out.print("\nNew Presences Message: ");
                        message = scanner.nextLine();
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.xa);
                        presence.setStatus(message);

                        connection.sendStanza(presence);
                        
                        System.out.println("Presence message changed successfully!");
                        miniroster = Roster.getInstanceFor(connection);
                        for (RosterEntry entry : miniroster.getEntries()) {
                            Presence updatedPresence = new Presence(Presence.Type.available);
                            updatedPresence.setMode(Presence.Mode.xa);
                            updatedPresence.setStatus(message);
                            updatedPresence.setTo(entry.getJid());
                            connection.sendStanza(updatedPresence);
                        }
                        break;
                    case 4:
                        System.out.println("\nYou selected: Busy");
                        System.out.print("\nNew Presences Message: ");
                        message = scanner.nextLine();
                        presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.dnd);
                        presence.setStatus(message);

                        connection.sendStanza(presence);
                        
                        System.out.println("Presence message changed successfully!");
                        miniroster = Roster.getInstanceFor(connection);
                        for (RosterEntry entry : miniroster.getEntries()) {
                            Presence updatedPresence = new Presence(Presence.Type.available);
                            updatedPresence.setMode(Presence.Mode.dnd);
                            updatedPresence.setStatus(message);
                            updatedPresence.setTo(entry.getJid());
                            connection.sendStanza(updatedPresence);
                        }
                        break;
                    case 5:
                        System.out.println("\nYou selected: Disconnected");
                        System.out.print("\nNew Presences Message: ");
                        message = scanner.nextLine();
                        presence = new Presence(Presence.Type.unavailable);
                        presence.setMode(Presence.Mode.available);
                        presence.setStatus(message);

                        connection.sendStanza(presence);
                        
                        System.out.println("Presence message changed successfully!");
                        miniroster = Roster.getInstanceFor(connection);
                        for (RosterEntry entry : miniroster.getEntries()) {
                            Presence updatedPresence = new Presence(Presence.Type.unavailable);
                            updatedPresence.setMode(Presence.Mode.available);
                            updatedPresence.setStatus(message);
                            updatedPresence.setTo(entry.getJid());
                            connection.sendStanza(updatedPresence);
                        }
                        break;
                    default:
                        System.out.println("\nInvalid selection");
                        break;
                }


            } catch (Exception e) {
                System.out.println("Error while changing presence message.");
                e.printStackTrace();
            }
        }

        // * It is used to always accept the request sent. It is like an automatic acceptance
        private static void RefreshRequests(AbstractXMPPConnection connection) {
            try {
                Roster roster = Roster.getInstanceFor(connection);
                roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        
                System.out.println("All pending friend requests accepted.");
            } catch (Exception e) {
                System.out.println("Error while accepting friend requests.");
                e.printStackTrace();
            }
        }

        public static void FileToContact(AbstractXMPPConnection connection, Scanner scanner) {
            try {
                System.out.print("User Name to Send File:  ");
                String contactUsername = scanner.next() + "@" + "alumchat.xyz";

                System.out.print("Filepath:  ");
                String filePath = scanner.next();
                EntityBareJid jid = JidCreate.entityBareFrom(contactUsername);

                ChatManager chatManager = ChatManager.getInstanceFor(connection);
                Chat chat = chatManager.chatWith(jid);

                byte[] fileData;
                try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                    fileData = fileInputStream.readAllBytes();
                }
                String base64EncodedFile = Base64.getEncoder().encodeToString(fileData);

                Message message = new Message();
                message.setBody(base64EncodedFile);
                chat.send(message);

                System.out.println("File sent to " + contactUsername);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error sending file: " + e.getMessage());
            }
        }

        // * This function allows to create a chat group, create and chat with one.

        public static void chatGroup(AbstractXMPPConnection connection, Scanner scanner) {
            try {
                MultiUserChatManager mucManager = MultiUserChatManager.getInstanceFor(connection);

                System.out.print("Group Name: ");
                String groupName = scanner.nextLine();

                MultiUserChat muc = mucManager.getMultiUserChat(JidCreate.entityBareFrom(groupName + "@conference.alumchat.xyz"));

                muc.create(Resourcepart.from(connection.getUser().getResourceOrThrow().toString()));
                muc.sendConfigurationForm(new Form(DataForm.Type.submit)); // Configuración predeterminada para grupo público

                // Configuración adicional para hacer el grupo persistente
                Form persistentForm = muc.getConfigurationForm().createAnswerForm();
                persistentForm.setAnswer("muc#roomconfig_persistentroom", true);
                muc.sendConfigurationForm(persistentForm);

                System.out.println("¡Group '" + groupName + "' is now available!");
            } catch (Exception e) {
                System.out.println("Error creating group: " + e.getMessage());
                e.printStackTrace();
            }
        }

        public static void chatMenu(AbstractXMPPConnection connection, Scanner scanner) {
            System.out.println("1. Create group");
            System.out.println("2. Join and chat");
            System.out.print("Select an option: ");

            int optiones = scanner.nextInt();
            scanner.nextLine();

            switch (optiones) {
                case 1:
                    System.out.println("Creating a new group...");
                    chatGroup(connection, scanner);
                    break;
                case 2:
                    System.out.println("Joining the group and chatting...");
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }

}

