package edu.ap.softwareproject.api.dto;

import java.time.LocalDateTime;

public record DocumentDTO(String id, String name, LocalDateTime created, long size) { }
