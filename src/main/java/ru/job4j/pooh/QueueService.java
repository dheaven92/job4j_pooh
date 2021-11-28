package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queues = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if (HTTP_REQUEST_POST.equals(req.getHttpRequestType())) {
            queues.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queues.get(req.getSourceName()).add(req.getParam());
            return new Resp(req.getParam(), HTTP_CODE_SUCCESS);
        }
        if (HTTP_REQUEST_GET.equals(req.getHttpRequestType())) {
            ConcurrentLinkedQueue<String> queue = queues.get(req.getSourceName());
            if (queue == null) {
                return new Resp("", HTTP_CODE_NOT_FOUND);
            }
            String result = queue.poll();
            return result != null
                    ? new Resp(result, HTTP_CODE_SUCCESS)
                    : new Resp("", HTTP_CODE_NO_CONTENT);
        }
        return new Resp("", HTTP_CODE_METHOD_NOT_ALLOWED);
    }
}
