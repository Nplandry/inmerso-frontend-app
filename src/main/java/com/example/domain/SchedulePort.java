package com.example.domain;

import java.util.Optional;

public interface SchedulePort {
    Optional<Schedule> obtenerSchedule();
    Optional<Schedule> actualizarSchedule(FocusScheduleRequest request);
}
