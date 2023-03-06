package org.example;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import static org.example.Client.fileLoggerAllMessagesInClient;
import static org.example.MainBasic.fileLoggerAllMessage;
import static org.example.MainNetwork.numberPortAndHost;
import static org.example.MainNetwork.portFile;

public class MainClient {

    public static void main(String[] args) throws IOException {

        String textFirst ="Write your name, before start to message in Server\n";
        System.out.printf(textFirst);

        String nameClient = new Scanner(System.in).nextLine();
        Client client = new Client(nameClient);

        fileLoggerAllMessage(fileLoggerAllMessagesInClient, textFirst);

        String textClient ="\nClient was make with names is " + client.getClientNickname() + "\n";
        System.out.printf(textClient);
        fileLoggerAllMessage(fileLoggerAllMessagesInClient, textClient);

        String host = numberPortAndHost(portFile, "host");
        int port = Integer.parseInt(Objects.requireNonNull(numberPortAndHost(portFile, "port")));

        client.startMessagesInServer(host, port);
    }
}