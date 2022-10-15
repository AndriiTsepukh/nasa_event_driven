package com.breskul.hw.listeners;

import com.breskul.hw.controllers.NasaPictureController;
import com.breskul.hw.endpoints.dto.LargestNasaPictureRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NasaPictureListener {

    Logger logger = LoggerFactory.getLogger(NasaPictureListener.class);

    @Autowired
    NasaPictureController nasaPictureController;

    @RabbitListener(queues = "largest-picture-command-queue")
    public void processLargestPicture(LargestNasaPictureRequest request) {
        logger.info("Find largest picture command received. commandId: " + request.getCommandId());
        nasaPictureController.findTheBiggestPicture(request.getSol(), request.getCamera(), request.getCommandId());
    }
}
