package com.example.dto;

import java.util.List;

public record ScheduleResponse(
    String status,
    ScheduleData data
) {
    public static record ScheduleData(
        TaskInfo current_task,          
        Integer current_ends_in_minutes,
        TaskInfo next_task,
        Integer next_starts_in_minutes,
        TimeRemaining time_remaining,
        List<BusyBlock> busy_blocks
    ) {}

    public static record TaskInfo(
        String title,
        String start,
        String end
    ) {}

    public static record TimeRemaining(
        Integer current_ends_in_minutes,
        Integer next_starts_in_minutes
    ) {}

    public static record BusyBlock(
        String title,
        String start,
        String end
    ) {}
}