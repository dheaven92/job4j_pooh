package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if (HTTP_REQUEST_GET.equals(req.getHttpRequestType())) {
            topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String result = topics.get(req.getSourceName()).get(req.getParam()).poll();
            if (result != null) {
                return new Resp(result, HTTP_CODE_SUCCESS);
            } else {
                return new Resp("", HTTP_CODE_NO_CONTENT);
            }
        }
        if (HTTP_REQUEST_POST.equals(req.getHttpRequestType())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> clients = topics.get(req.getSourceName());
            if (clients == null) {
                return new Resp("", HTTP_CODE_NOT_FOUND);
            }
            for (ConcurrentLinkedQueue<String> queue : clients.values()) {
                queue.add(req.getParam());
            }
            return new Resp(req.getParam(), HTTP_CODE_SUCCESS);
        }
        return new Resp("", HTTP_CODE_METHOD_NOT_ALLOWED);
    }
}
