package com.InmersoAI.domain;

import java.util.Optional;

import com.InmersoAI.domain.model.Schedule;

public interface SchedulePort {
    Optional<Schedule> obtenerSchedule();
    Optional<Schedule> actualizarSchedule(FocusScheduleRequest request);
}
