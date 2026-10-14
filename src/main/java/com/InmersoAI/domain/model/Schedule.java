package com.InmersoAI.domain.model;

public record Schedule(
        String currentTaskTitle,
        String nextTaskTitle,
        Integer currentEndsInMinutes,
        Integer nextStartsInMinutes
) {}

