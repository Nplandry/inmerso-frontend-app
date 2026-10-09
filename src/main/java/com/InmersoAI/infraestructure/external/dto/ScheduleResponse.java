package com.example.infraestructure.external.dto;

import java.util.List;

public record ScheduleResponse(String status, ScheduleData data) {
    public record ScheduleData(
            TaskInfo current_task,
            TaskInfo next_task,
            TimeRemaining time_remaining,
            List<BusyBlock> busy_blocks
    ) {
    }

    public record TaskInfo(String title, String start, String end) {
    }

    public record TimeRemaining(
            Integer current_ends_in_minutes,
            Integer next_starts_in_minutes
    ) {
    }

    public record BusyBlock(String title, String start, String end) {
    }
}
