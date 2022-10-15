package com.breskul.hw.endpoints;

import com.breskul.hw.controllers.NasaPictureController;
import com.breskul.hw.endpoints.dto.LargestNasaPictureRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class NasaPictureEndpoints {

    Logger logger = LoggerFactory.getLogger(NasaPictureEndpoints.class);

    @Autowired
    NasaPictureController nasaPictureController;

    @PostMapping("/mars/pictures/largest")
    @SneakyThrows
    public ResponseEntity<byte[]> findLargestPicture(@RequestBody LargestNasaPictureRequest request) {
        logger.info("New request to find Picture received");
        String commandId = nasaPictureController.requestTheBiggestPicture(request.getSol(), request.getCamera());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("http://localhost:8080/mars/pictures/largest/" + commandId));
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(httpHeaders, HttpStatus.OK);
        logger.info("Sending response");
        return responseEntity;
    }

    @GetMapping("/mars/pictures/largest/{commandId}")
    public ResponseEntity<byte[]> getLargestPicture(@PathVariable("commandId") String commandId) {
        logger.info("Request to get picture by commandId received.");
        byte[] picture = nasaPictureController.getPicture(commandId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(picture, headers, HttpStatus.OK);
        return responseEntity;
    }
}
