package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

class ReadThread implements Runnable {
    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;

    ReadThread(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        while (!ChatInput.finished) {
            byte[] buffer = new byte[com.company.ReadThread.MAX_LEN];
            DatagramPacket datagram = new
                    DatagramPacket(buffer, buffer.length, group, port);
            System.out.println("ReadThread is running");
            String message;
            try {
                socket.receive(datagram);
                message = new
                        String(buffer, 0, datagram.getLength(), "UTF-8");
                if (!message.startsWith(ChatInput.name))
                    System.out.println(message);
            } catch (IOException e) {
                System.out.println("Socket closed!");
            }
        }
    }
}