package com.InmersoAI.app.Tarea;

import org.springframework.stereotype.Service;

import com.InmersoAI.domain.FocusScheduleRequest;
import com.InmersoAI.domain.SchedulePort;
import com.InmersoAI.domain.model.Schedule;

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

    public Optional<Schedule> actualizarSchedule(String text) {
        return schedulePort.actualizarSchedule(new FocusScheduleRequest(text));
    }
}
