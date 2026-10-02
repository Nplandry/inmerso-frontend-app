package com.example.dto;

import java.util.List;

public record ScheduleResponse(
    String status,
    ScheduleData data
) {}

record ScheduleData(
    String current_task,
    int current_ends_in_minutes,
    String next_task,
    int next_starts_in_minutes,
    TimeRemaining time_remaining,
    List<BusyBlock> busy_blocks
) {}

record TimeRemaining(
    int current_ends_in_minutes,
    int current_ends_in_seconds
) {}

record BusyBlock(
    String title,
    String start,
    String end
) {}