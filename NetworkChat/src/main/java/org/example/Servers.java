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

    private static final Map<PrintWriter, BufferedReader> mapWriteAndRead = new HashMap<>();

    public static List<ClientServer> getServerList() {
        return serverList;
    }

    public static Map<PrintWriter, BufferedReader> getMapWriteAndRead() {
        return mapWriteAndRead;
    }

    public void addClient(ClientServer clientServer) {
        serverList.add(clientServer);
        mapWriteAndRead.put(clientServer.getOutput(), clientServer.getInput());
    }

    public static void sendMessageClient(String text) throws IOException {
        for (PrintWriter printWriter : getMapWriteAndRead().keySet()) {
            printWriter.println(text);
        }
    }
}