package org.example;


import java.io.*;
import java.net.Socket;

import static org.example.MainBasic.*;

public class Client2 {

    protected static File fileLoggerAllMessagesInClient;

    private static String clientNickname;

    private static Socket clientSocket;

    private static PrintWriter out;

    private static BufferedReader in;

    private static BufferedReader inTerminalClient;

    public Client2(String clientNickname) {
        this.clientNickname = clientNickname;
        fileLoggerAllMessagesInClient = getFileForClient("Client2/");
    }

    public static String getClientNickname() {
        return clientNickname;
    }

    public void startMessagesInServer(String host, int port) throws IOException {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            inTerminalClient = new BufferedReader(new InputStreamReader(System.in));
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
                try {
                    String question = inTerminalClient.readLine();
                    String text = messagePush(getClientNickname(), question);
                    out.println(text);
                    fileLoggerAllMessage(fileLoggerAllMessagesInClient, text);
                    if (question.equals("/exit")) {
                        String texts = String.format("Client %s is disconnected\n", clientNickname);
                        System.out.printf(texts);
                        fileLoggerAllMessage(fileLoggerAllMessagesInClient, texts);
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        closedBuffer();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            try {
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
                inTerminalClient.close();
            }
        } catch (IOException ignored) {
        }
    }
}
