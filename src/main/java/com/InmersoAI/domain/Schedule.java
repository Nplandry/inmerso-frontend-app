package com.InmersoAI.domain;

public record Schedule(
        String currentTaskTitle,
        String nextTaskTitle,
        Integer currentEndsInMinutes,
        Integer nextStartsInMinutes
) {}

