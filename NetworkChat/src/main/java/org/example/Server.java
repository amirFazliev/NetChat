package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static org.example.MainBasic.messagePush;
import static org.example.MainNetwork.fileLoggerAllMessagesInServer;
import static org.example.MainBasic.fileLoggerAllMessage;
import static org.example.MainNetwork.serverMap;

public class Server {
    protected static Socket clientSocket;
    protected static String serverName = "SERVER";
    protected static BufferedWriter out;
    protected static BufferedReader in;
    protected static Scanner scannerNetwork = new Scanner(System.in);

    public Server(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String textFirstServerAfterConnectedClient =
                String.format("Connected new client. Your inet-address is %s, your port is %d\n", clientSocket.getInetAddress(), clientSocket.getPort());
        System.out.printf(textFirstServerAfterConnectedClient);
        fileLoggerAllMessage(fileLoggerAllMessagesInServer, textFirstServerAfterConnectedClient);

        new InputServerThread().start();
        new OutputServerThread().start();

    }

    private static class InputServerThread extends Thread {
        @Override
        public void run() {

            while (true) {
                try {
                    final String answer = in.readLine();
                    System.out.println(answer);
                    fileLoggerAllMessage(fileLoggerAllMessagesInServer, answer);
                    if (answer.endsWith("/exit")) {
                        String text = String.format("Client with inet-address %s and port %d disconnected\n", clientSocket.getInetAddress(), clientSocket.getPort());
                        System.out.printf(text);
                        fileLoggerAllMessage(fileLoggerAllMessagesInServer, text);
                        break;
                    }
                } catch (IOException e) {
                    try {
                        closedBuffer(this, clientSocket);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    private static class OutputServerThread extends Thread {
        @Override
        public void run() {

            while (true) {
                String text = scannerNetwork.nextLine();
                String question = messagePush(serverName, text);
                fileLoggerAllMessage(fileLoggerAllMessagesInServer, question);
                for (Socket client : serverMap.keySet()) {
                    try {
                        serverMap.get(client).sendMessageClient(question);
                    } catch (IOException e) {
                        try {
                            closedBuffer(this, clientSocket);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
    }

    private void sendMessageClient(String text) throws IOException {
        out.write(text);
        out.flush();
    }

    private static void closedBuffer(Thread thread, Socket socket) throws IOException {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                out.close();
                thread.interrupt();
                serverMap.remove(socket);
            }
        } catch (IOException ignored) {
        }
    }
}
