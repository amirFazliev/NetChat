package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class MainBasic {
    static AtomicInteger countClient = new AtomicInteger(1);

    public static String messagePush(String name, String text) {
        return name + " [" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.mm.yyyy hh:mm:ss")) + "] " + text;
    }

    public static void fileLoggerAllMessage(File file, String text) {
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            byte[] bytes = (text + "\n").getBytes();
            fos.write(bytes);
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}