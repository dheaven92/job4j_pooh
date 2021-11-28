package ru.job4j.pooh;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Req {

    private final static String CURL_FIRST_LINE_REGEX = "^(POST|GET) /(queue|topic)/(\\w+)?(/(.+))? HTTP/1.1";
    private final static String HTTP_REQUEST_POST = "POST";
    private final static String POOH_MODE_TOPIC = "topic";

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        try {
            String[] parts = content.split(System.lineSeparator());
            Pattern patternFirstLine = Pattern.compile(CURL_FIRST_LINE_REGEX);
            Matcher matcherFirstLine = patternFirstLine.matcher(parts[0]);
            matcherFirstLine.matches();
            String httpRequestType = matcherFirstLine.group(1);
            String poohMode = matcherFirstLine.group(2);
            String sourceName = matcherFirstLine.group(3);
            String param = "";
            if (HTTP_REQUEST_POST.equals(httpRequestType)) {
                param = parts[parts.length - 1];
            } else if (POOH_MODE_TOPIC.equals(poohMode)) {
                param = matcherFirstLine.group(5);
            }
            return new Req(httpRequestType, poohMode, sourceName, param);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid curl request");
        }
    }

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
