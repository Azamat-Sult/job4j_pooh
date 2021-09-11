package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        String[] nameAndData = req.text().split("/");
        String respText = "";
        int respStatus = 400;
        if (req.method().equals("POST")) {
            queue.putIfAbsent(nameAndData[0], new ConcurrentLinkedQueue<>());
            queue.get(nameAndData[0]).add(nameAndData[1]);
            respStatus = 200;
            respText = "Theme: " + nameAndData[0] + " updated by post: " + nameAndData[1];
        }
        if (req.method().equals("GET")) {
            respStatus = 200;
            ConcurrentLinkedQueue<String> theme = queue.get(nameAndData[0]);
            if (theme == null) {
                respText = "No queue with that name was found";
                respStatus = 404;
            } else {
                respText = queue.get(nameAndData[0]).poll();
                if (respText == null) {
                    respText = "There are no new posts in the queue";
                }
            }
        }
        return new Resp(respText, respStatus);
    }
}
