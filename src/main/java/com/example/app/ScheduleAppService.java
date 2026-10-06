package com.example.app;

import com.example.domain.Schedule;
import com.example.domain.SchedulePort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScheduleAppService {
    private final SchedulePort schedulePort;

    public ScheduleAppService(SchedulePort schedulePort) {
        this.schedulePort = schedulePort;
    }

    public Optional<Schedule> obtenerSchedule() {
        return schedulePort.obtenerSchedule();
    }
}
