package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static org.example.MainBasic.*;

public class Client {

    protected static File fileLoggerAllMessagesInClient;

    private static String clientNickname;

    private static Socket clientSocket;

    private static PrintWriter out;

    private static BufferedReader in;


    public Client(String clientNickname) {
        this.clientNickname = clientNickname;
        fileLoggerAllMessagesInClient = getFileForClient("ClientsChat/");
    }

    public static String getClientNickname() {
        return clientNickname;
    }

    public void startMessagesInServer(String host, int port) throws IOException {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            new InputThread().start();
            new OutputThread().start();
        } catch (IOException e) {
            closedBuffer();
        }
    }


    private static class InputThread extends Thread {
        @Override
        public void run() {
            String text;
            while (true) {
                try {
                    text = in.readLine();
                    System.out.println(text);
                    fileLoggerAllMessage(fileLoggerAllMessagesInClient, text);
                } catch (IOException ignored) {
                    try {
                        closedBuffer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class OutputThread extends Thread {
        @Override
        public void run() {
            while (true)
            {
                String question = new Scanner(System.in).nextLine();
                String text = messagePush(getClientNickname(), question);
                out.println(text);
                fileLoggerAllMessage(fileLoggerAllMessagesInClient, text);
                if (question.equals("/exit")) {
                    String texts = String.format("Client %s is disconnected\n", clientNickname);
                    System.out.printf(texts);
                    fileLoggerAllMessage(fileLoggerAllMessagesInClient, texts);
                    break;
                }
            }
            try {
                System.out.println("sdffs");
                closedBuffer();
            } catch (IOException ignored) {
            }
        }
    }

    private static void closedBuffer() throws IOException {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                out.close();
                in.close();
            }
        } catch (IOException ignored) {
        }
    }
}
