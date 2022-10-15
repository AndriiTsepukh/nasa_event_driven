package com.breskul.hw.endpoints.dto;

import lombok.Data;

@Data
public class LargestNasaPictureRequest {
    private int sol;
    private String camera;
    private String commandId;
}
