package org.example;

import java.io.*;
import java.net.Socket;

import static org.example.MainBasic.fileLoggerAllMessage;
import static org.example.MainBasic.messagePush;
import static org.example.MainNetwork.fileLoggerAllMessagesInServer;
import static org.example.Servers.*;

public class ClientServer {

    private static Socket clientSocketServer;
    private static PrintWriter output;
    private static BufferedReader input;
    private static BufferedReader inTerminalServer;

    private static ClientServer clientServer;

    public ClientServer(Socket clientSocket) throws IOException {
        clientSocketServer = clientSocket;
        output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocketServer.getOutputStream())), true);
        input = new BufferedReader(new InputStreamReader(clientSocketServer.getInputStream()));
        inTerminalServer = new BufferedReader(new InputStreamReader(System.in));
    }

    public PrintWriter getOutput() {
        return output;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void startClientServer() {
        String textFirstServerAfterConnectedClient =
                String.format("Connected new client. Your inet-address is %s, your port is %d\n", getClientSocketServer().getInetAddress(), getClientSocketServer().getPort());
        System.out.printf(textFirstServerAfterConnectedClient);
        fileLoggerAllMessage(fileLoggerAllMessagesInServer, textFirstServerAfterConnectedClient);

        new InputServerThread().start();
        new OutputServerThread().start();
    }

    public Socket getClientSocketServer() {
        return clientSocketServer;
    }

    public void setClient(ClientServer client) {
        clientServer = client;
    }

    private static class InputServerThread extends Thread {
        @Override
        public void run() {

            while (true) {
                try {
                    String answer = input.readLine();
                    System.out.println(answer);
                    fileLoggerAllMessage(fileLoggerAllMessagesInServer, answer);
                    if (answer.endsWith("/exit")) {
                        String text = String.format("Client with inet-address %s and port %d disconnected\n", clientSocketServer.getInetAddress(), clientSocketServer.getPort());
                        System.out.printf(text);
                        fileLoggerAllMessage(fileLoggerAllMessagesInServer, text);
                        closedBuffer(this);
                        break;
                    }
                } catch (IOException e) {
                    try {
                        closedBuffer(this);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            try {
                closedBuffer(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class OutputServerThread extends Thread {
        @Override
        public void run() {
            while (true) {
                String text;
                try {
                    text = inTerminalServer.readLine();
                    String question = messagePush(serverName, text);
                    fileLoggerAllMessage(fileLoggerAllMessagesInServer, question);
                    sendMessageClient(question);
                } catch (IOException ignored) {
                    try {
                        closedBuffer(this);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static void closedBuffer(Thread thread) throws IOException {
        try {
            if (!clientSocketServer.isClosed()) {
                getMapWriteAndRead().remove(output,input);
                clientSocketServer.close();
                input.close();
                output.close();
                inTerminalServer.close();
                thread.interrupt();
                getServerList().remove(clientServer);
            }
        } catch (IOException ignored) {
        }
    }
}

