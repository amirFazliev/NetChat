package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Servers {
    public static final String serverName = "SERVER";

    private static final List<ClientServer> serverList = new LinkedList<>();

    private static final List<PrintWriter> printWriterList = new LinkedList<>();

    public static List<ClientServer> getServerList() {
        return serverList;
    }

    public static List<PrintWriter> getPrintWriterList() {
        return printWriterList;
    }

    public void addClient(ClientServer clientServer) {
        serverList.add(clientServer);
        printWriterList.add(clientServer.getOutput());
     }

    public static void sendMessageClient(String text) throws IOException {
        for (PrintWriter pw : printWriterList) {
            pw.println(text);
        }
    }

    public static void sendMessageFromClientForOtherClients(String text, List<PrintWriter> list) {
        for (PrintWriter pw : list) {
            pw.println(text);
        }
    }

    public static List<PrintWriter> listForSendMessage (PrintWriter pw) {
        List<PrintWriter> list = new LinkedList<>();
        for (PrintWriter printWriter : printWriterList) {
            list.add(printWriter);
        }
        list.remove(pw);
        return list;
    }
}