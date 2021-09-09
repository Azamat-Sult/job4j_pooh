package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>>
            subscribers = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        String[] nameAndData = req.text().split("/");
        String respText = "";
        int respStatus = 400;
        if (req.method().equals("POST")) {
            for (ConcurrentMap.Entry<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>>
                    subscriber : subscribers.entrySet()) {
                for (ConcurrentMap.Entry<String, ConcurrentLinkedQueue<String>>
                        theme : subscriber.getValue().entrySet()) {
                    if (theme.getKey().equals(nameAndData[0])) {
                        theme.getValue().add(nameAndData[1]);
                    }
                }
            }
            respText = "Msg delivered to subscribers";
            respStatus = 200;
        }
        if (req.method().equals("GET")) {
            if (subscribers.get(nameAndData[1]) == null) {
                subscribers.putIfAbsent(nameAndData[1], new ConcurrentHashMap<>());
                subscribers.get(nameAndData[1]).putIfAbsent(nameAndData[0], new ConcurrentLinkedQueue<>());
                respText = "User ID: " + nameAndData[1] + " subscribed to topic: " + nameAndData[0];
            } else if (subscribers.get(nameAndData[1]).get(nameAndData[0]) == null) {
                subscribers.get(nameAndData[1]).putIfAbsent(nameAndData[0], new ConcurrentLinkedQueue<>());
                respText = "User ID: " + nameAndData[1] + " subscribed to topic: " + nameAndData[0];
            } else {
                respText = subscribers.get(nameAndData[1]).get(nameAndData[0]).poll();
                if (respText == null) {
                    respText = "There are no new posts in the queue";
                }
            }
            respStatus = 200;
        }
        return new Resp(respText, respStatus);
    }
}
