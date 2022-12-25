package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class MainNetwork {
    static File portFile = new File("settings.txt");

    static Scanner scannerNetwork = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        try (FileOutputStream fos = new FileOutputStream(portFile)){
            byte[]bytes = ("host: 127.0.0.1\n" +
                    "port: 8095").getBytes();
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server started!");
        int port = Integer.parseInt(Objects.requireNonNull(numberPortAndHost(portFile, "port")));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out =
                             new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
                     BufferedReader in =
                             new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    System.out.printf("Connected new client. Your inet-address is %s, your port is %d\n", clientSocket.getInetAddress(), clientSocket.getPort());
                    out.println(String.format("Hello client. Your inet-address is %s, your port is %d", clientSocket.getInetAddress(), clientSocket.getPort()));

                    while (true) {
                        final String answer = in.readLine();
                        System.out.println(answer);
                        if (answer.equals("/exit")) {
                            System.out.printf("Client with inet-address %s and port %d disconnected\n", clientSocket.getInetAddress(), clientSocket.getPort());
                            break;
                        } else {
                            String question = scannerNetwork.nextLine();
                            out.println(question);
                        }
                    }



//                    System.out.println("Write your name");
//                    final String name = in.readLine();
//                    out.println(String.format("Hi %s, your port is %d", name, clientSocket.getPort()));
//
//                    System.out.println("Are you child? (yes/no)");
//                    while (true) {
//                        final String answer = in.readLine();
//                        if (answer.equals("yes")) {
//                            out.println(String.format("Welcome to the kids area, %s! Let's play!", name));
//                            break;
//                        } else if (answer.equals("no")) {
//                            out.println(String.format("Welcome to the adult zone,%s! Have a good rest, or a good working day!", name));
//                            break;
//                        }
//                    }
//
//                    System.out.println("How old are you?");
//                    final String age = in.readLine();
//                    out.println(String.format("%s is greatest age for life", age));
//
//                    System.out.println("Where are you born?");
//                    final String city = in.readLine();
//                    out.println(String.format("%s is wonderful city in the World", city));
                }
            }
        }
    }

    public static String numberPortAndHost(File file, String textAboutInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        try(FileInputStream fis = new FileInputStream(file)) {
            int i;
            while ((i= fis.read())!= -1){
                stringBuilder.append((char)i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if ("host".equalsIgnoreCase(textAboutInfo)){
            return stringBuilder.substring(stringBuilder.indexOf("host: ") + 6, stringBuilder.indexOf("\n"));
        } else if ("port".equalsIgnoreCase(textAboutInfo)){
            return stringBuilder.substring(stringBuilder.indexOf("port: ") + 6);
        }
        System.out.println("Ошибка: вы не выбрали указатель host/port");
        return null;
    }
}