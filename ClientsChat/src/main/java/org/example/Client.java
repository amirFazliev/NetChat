package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static org.example.MainBasic.*;

public class Client {

    private static String clientNickname;

    private String host;

    private int port;

    private static File fileLoggerAllMessagesInClient = new File(String.format("ClientsChat/fileClient%d.log", countClient.getAndIncrement()));

    private static Socket clientSocket;

    private static BufferedWriter out;

    private static BufferedReader in;

    private static Scanner scannerClient = new Scanner(System.in);

    public Client(String clientNickname) {
        this.clientNickname = clientNickname;
    }

    public static String getClientNickname() {
        return clientNickname;
    }

    public void startMessagesInServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        try {
            clientSocket = new Socket(host, port);
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
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
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static class OutputThread extends Thread {
        @Override
        public void run() {
            while (true) {
                String question = scannerClient.nextLine();
                if (question.equals("/exit")) {
                    String textExit = messagePush(getClientNickname(), question);
                    try {
                        out.write(textExit);
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    fileLoggerAllMessage(fileLoggerAllMessagesInClient, textExit);
                    break;
                }

                try {
                    out.write(messagePush(getClientNickname(), question));
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
            }
        } catch (IOException ignored) {
        }
    }
}
