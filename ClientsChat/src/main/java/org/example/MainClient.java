package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

import static org.example.MainNetwork.numberPortAndHost;
import static org.example.MainNetwork.portFile;

public class MainClient {

    static Scanner scannerClient = new Scanner(System.in);

    public static void main(String[] args) {
        String host = numberPortAndHost(portFile, "host");
        int port = Integer.parseInt(Objects.requireNonNull(numberPortAndHost(portFile, "port")));

        try (Socket clientSocket = new Socket(host, port);
             PrintWriter out =
                     new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
             BufferedReader in =
                     new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String textFirst = in.readLine();
            System.out.println(textFirst);

            while (true) {
                String question = scannerClient.nextLine();
                if (question.equals("/exit")) {
                    out.println("/exit");
                    break;
                }
                out.println(question);
                String text = in.readLine();
                System.out.println(text);
            }



//            String text = scannerClient.nextLine();
//            out.println(text);
//            String name = in.readLine();
//            System.out.println(name);
//
//            while (true) {
//                String answer = scannerClient.nextLine();
//                if (answer.equals("yes")) {
//                    out.println(answer);
//                    break;
//                } else if (answer.equals("no")) {
//                    out.println(answer);
//                    break;
//                } else {
//                    System.out.println("Try again");
//                }
//            }
//            String answerServer = in.readLine();
//            System.out.println(answerServer);
//
//            String ageAnswer = scannerClient.nextLine();
//            out.println(ageAnswer);
//            String age = in.readLine();
//            System.out.println(age);
//
//            String cityAnswer = scannerClient.nextLine();
//            out.println(cityAnswer);
//            String city = in.readLine();
//            System.out.println(city);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}