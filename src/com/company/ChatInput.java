package com.company;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatInput {
    private static final String TERMINATE = "Exit";
    static String name;
    static volatile boolean finished = false;

    String host;
    int port;

    public ChatInput(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startChat() {
        try {
            InetAddress group = InetAddress.getByName(host);
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter your name: ");
            name = sc.nextLine();
            MulticastSocket socket = new MulticastSocket(port);

            // Since we are deploying
            socket.setTimeToLive(0);

            //this on localhost only (For a subnet set it as 1)
            socket.joinGroup(group);

            Thread t = new Thread(new
                    ReadThread(socket, group, port));
            // Spawn a thread for reading messages
            t.start();

            // sent to the current group
            System.out.println("Start typing messages...\n");
            while (true) {
                String message;
                message = sc.nextLine();
                if (message.equalsIgnoreCase(ChatInput.TERMINATE)) {
                    finished = true;
                    socket.leaveGroup(group);
                    socket.close();
                    break;
                }
                message = name + ": " + message;
                byte[] buffer = message.getBytes();
                DatagramPacket datagram = new
                        DatagramPacket(buffer, buffer.length, group, port);
                socket.send(datagram);
            }
        } catch (SocketException se) {
            System.out.println("Error creating socket");
            se.printStackTrace();
        } catch (IOException ie) {
            System.out.println("Error reading/writing from/to socket");
            ie.printStackTrace();
        }
    }

}
