package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is(paramForPostMethod));
        assertThat(result.status(), is("200"));
    }

    @Test
    public void whenGetWithoutQueue() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is(""));
        assertThat(result.status(), is("404"));
    }

    @Test
    public void whenPostThenGetFromEmptyQueue() {
        QueueService queueService = new QueueService();
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=18")
        );
        queueService.process(new Req("GET", "queue", "weather", null));
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is(""));
        assertThat(result.status(), is("204"));
    }

    @Test
    public void whenPostMultipleThenGetMultiple() {
        QueueService queueService = new QueueService();
        String paramForPostMethod1 = "temperature=18";
        String paramForPostMethod2 = "temperature=2-";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod1)
        );
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod2)
        );
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result1.text(), is(paramForPostMethod1));
        assertThat(result1.status(), is("200"));
        assertThat(result2.text(), is(paramForPostMethod2));
        assertThat(result2.status(), is("200"));
    }

    @Test
    public void whenUnsupportedHttpMethodCall() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("PUT", "queue", "weather", "temperature=18")
        );
        assertThat(result.text(), is(""));
        assertThat(result.status(), is("405"));
    }
}