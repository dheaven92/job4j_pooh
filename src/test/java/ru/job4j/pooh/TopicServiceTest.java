package ru.job4j.pooh;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenAddFromNonExistingTopicThen204() {
        TopicService topicService = new TopicService();
        Resp result = topicService.process(
                new Req("GET", "topic", "weather", "client407")
        );
        assertThat(result.text(), is(""));
        assertThat(result.status(), is("204"));
    }

    @Test
    public void whenPostWithoutSubscribers() {
        TopicService topicService = new TopicService();
        Resp result = topicService.process(
                new Req("POST", "topic", "weather", "temperature=18")
        );
        assertThat(result.text(), is(""));
        assertThat(result.status(), is("404"));
    }

    @Test
    public void whenUnsupportedHttpMethodCall() {
        TopicService topicService = new TopicService();
        topicService.process(
                new Req("GET", "topic", "weather", "client407")
        );
        Resp result = topicService.process(
                new Req("PUT", "topic", "weather", "temperature=18")
        );
        assertThat(result.text(), is(""));
        assertThat(result.status(), is("405"));
    }
}