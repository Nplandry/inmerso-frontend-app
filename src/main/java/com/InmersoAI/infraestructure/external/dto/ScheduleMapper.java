package com.example.infraestructure.external.dto;

import com.example.domain.Schedule;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ScheduleMapper {
    public Optional<Schedule> toDomain(ScheduleResponse response) {
        if (response == null || response.data() == null) {
            return Optional.empty();
        }

        var data = response.data();
        var currentTask = data.current_task();
        var nextTask = data.next_task();
        var timeRemaining = data.time_remaining();

        return Optional.of(new Schedule(
                currentTask == null ? null : currentTask.title(),
                nextTask == null ? null : nextTask.title(),
                timeRemaining == null ? null : timeRemaining.current_ends_in_minutes(),
                timeRemaining == null ? null : timeRemaining.next_starts_in_minutes()
        ));
    }
}
