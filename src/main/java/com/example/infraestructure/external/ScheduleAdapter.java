package com.example.infraestructure.external;

import com.example.app.ScheduleAppService;
import com.example.domain.FocusScheduleRequest;
import com.example.domain.Schedule;
import com.example.domain.SchedulePort;
import com.example.domain.Tarea;
import com.example.infraestructure.external.dto.ScheduleMapper;
import com.example.infraestructure.external.dto.ScheduleResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ScheduleAdapter implements SchedulePort {
    private static final Logger LOGGER = Logger.getLogger(ScheduleAdapter.class.getName());
    private static final String SCHEDULE_STATUS_URL = "https://inmerso-backend.onrender.com/api/v1/focus/status";
    private static final String SCHEDULE_UPDATE_URL = "https://inmerso-backend.onrender.com/api/v1/focus/schedule";
    private static final String API_KEY = "inmerso_dev_key_change_me";

    private final RestTemplate restTemplate;
    private final ScheduleMapper scheduleMapper;

    public ScheduleAdapter(RestTemplate restTemplate, ScheduleMapper scheduleMapper) {
        this.restTemplate = restTemplate;
        this.scheduleMapper = scheduleMapper;
    }

    @Override
    public Optional<Schedule> obtenerSchedule() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", API_KEY);

        try {
            ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
                    SCHEDULE_STATUS_URL,
                    HttpMethod.GET,
                    new HttpEntity<Void>(headers),
                    ScheduleResponse.class
            );
            return scheduleMapper.toDomain(response.getBody());
        } catch (RestClientException exception) {
            LOGGER.log(Level.WARNING, "No se pudo obtener el schedule del backend externo", exception);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Schedule> actualizarSchedule(FocusScheduleRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
                    SCHEDULE_UPDATE_URL,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    ScheduleResponse.class
            );
            return scheduleMapper.toDomain(response.getBody());
        } catch (RestClientException exception) {
            LOGGER.log(Level.WARNING, "No se pudo actualizar el schedule en el backend externo", exception);
            return Optional.empty();
        }
    }
}