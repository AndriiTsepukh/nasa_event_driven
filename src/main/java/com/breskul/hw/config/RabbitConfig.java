package com.breskul.hw.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Exchange pictureExchange() {
        return new DirectExchange("picture-exchange");
    }

    @Bean
    public Queue largestPictureQueue() {
        return new Queue("largest-picture-command-queue");
    }

    @Bean
    public Binding participantsQueueBinding() {
        return BindingBuilder
                .bind(largestPictureQueue())
                .to(pictureExchange())
                .with("")
                .noargs();
    }
}
