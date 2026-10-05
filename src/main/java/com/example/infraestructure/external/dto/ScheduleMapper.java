package com.example.infraestructure.external.dto;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ScheduleMapper {
    private final RestTemplate restTemplate;
    
    public ScheduleMapper(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public ScheduleResponse obtenerScheduleCompleto() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "inmerso_dev_key_change_me");
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
                "https://inmerso-backend.onrender.com/api/v1/focus/status",
                HttpMethod.GET,
                entity,
                ScheduleResponse.class
            );

            return response.getBody();
        } catch (RestClientException exception) {
            return null;
        }
    }
    
    public String obtenerTareaActual() {
        ScheduleResponse response = obtenerScheduleCompleto();
        if (response != null && response.data() != null && response.data().current_task() != null) {
            return response.data().current_task().title();
        }
        return null;
    }
    
    public int obtenerTiempoRestante() {
        ScheduleResponse response = obtenerScheduleCompleto();
        if (response != null && response.data() != null && response.data().time_remaining() != null
                && response.data().time_remaining().current_ends_in_minutes() != null) {
            return response.data().time_remaining().current_ends_in_minutes();
        }
        return 0;
    }
    
    public int obtenerTiempoSiguiente() {
        ScheduleResponse response = obtenerScheduleCompleto();
        if (response != null && response.data() != null && response.data().time_remaining() != null 
                && response.data().time_remaining().next_starts_in_minutes() != null) {
            return response.data().time_remaining().next_starts_in_minutes();
        }
        return 0;
    }
    
    public String obtenerSiguienteTarea() {
        ScheduleResponse response = obtenerScheduleCompleto();
        if (response != null && response.data() != null && response.data().next_task() != null) {
            return response.data().next_task().title();
        }
        return null;
    }
}