package ru.job4j.pooh;

public interface Service {

    String HTTP_REQUEST_GET = "GET";
    String HTTP_REQUEST_POST = "POST";
    String HTTP_CODE_SUCCESS = "200";
    String HTTP_CODE_NO_CONTENT = "204";
    String HTTP_CODE_NOT_FOUND = "404";
    String HTTP_CODE_METHOD_NOT_ALLOWED = "405";

    Resp process(Req req);
}
