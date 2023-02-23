package org.example;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import static org.example.Client2.fileLoggerAllMessagesInClient;
import static org.example.MainBasic.*;
import static org.example.MainNetwork.*;

public class MainClient2 {


    public static void main(String[] args) throws IOException {

        String textFirst ="Write your name, before start to message in Server\n";
        System.out.printf(textFirst);

        String nameClient = new Scanner(System.in).nextLine();
        Client2 client = new Client2(nameClient);

        fileLoggerAllMessage(fileLoggerAllMessagesInClient, textFirst);

        String textClient ="\nClient was make with names is " + client.getClientNickname() + "\n";
        System.out.printf(textClient);
        fileLoggerAllMessage(fileLoggerAllMessagesInClient, textClient);

        String host = numberPortAndHost(portFile, "host");
        int port = Integer.parseInt(Objects.requireNonNull(numberPortAndHost(portFile, "port")));

        client.startMessagesInServer(host, port);
    }
}