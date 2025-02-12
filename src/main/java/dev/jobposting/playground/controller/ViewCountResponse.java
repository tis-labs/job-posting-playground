package dev.jobposting.playground.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ViewCountResponse {
    private String message;
    private int totalViewCount;
}
