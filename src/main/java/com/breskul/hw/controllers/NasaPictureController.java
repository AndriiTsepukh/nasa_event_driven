package com.breskul.hw.controllers;

import com.breskul.hw.endpoints.dto.LargestNasaPictureRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class NasaPictureController {

    Logger logger = LoggerFactory.getLogger(NasaPictureController.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    NasaController nasaController;
    Map<String, byte[]> biggestPictures = new HashMap<>();

    public String requestTheBiggestPicture(int sol, String camera) {
        String commandId = RandomStringUtils.random(10, true, true);

        LargestNasaPictureRequest request = new LargestNasaPictureRequest();
        request.setSol(sol);
        request.setCamera(camera);
        request.setCommandId(commandId);

        rabbitTemplate.convertAndSend("picture-exchange", "", request);

        logger.info("Command send to Rabbit. CommandId: " + commandId);

        return commandId;
    }

    public void findTheBiggestPicture(int sol, String camera, String commandId) {

        byte[] body = nasaController.getLargestPicture(sol, Optional.ofNullable(camera));
//        Attach body
//        byte[] body = "test body".getBytes(StandardCharsets.UTF_8);
        logger.info("Saving biggest picture into storage");
        biggestPictures.put(commandId, body);
    }

    public byte[] getPicture(String commandId) {
        var picture = biggestPictures.get(commandId);
        if (picture == null) throw new RuntimeException("Picture not found");
        return picture;
    }
}
