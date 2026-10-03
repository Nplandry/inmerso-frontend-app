package com.example.services;

import com.example.dto.ScheduleResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

  private final RestTemplate restTemplate;

  public ApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ScheduleResponse obtenerScheduleCompleto() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("x-api-key", "inmerso_dev_key_change_me");

    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
        "https://inmerso-backend.onrender.com/api/v1/focus/status",
        HttpMethod.GET,
        entity,
        ScheduleResponse.class
    );

    return response.getBody();
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
    if (response != null && response.data() != null && response.data().time_remaining() != null) {
      return response.data().time_remaining().current_ends_in_minutes();
    }
    return 0;
  }


 public int obtenerTiempoSiguente() {
    ScheduleResponse response = obtenerScheduleCompleto();
    if (response != null && response.data() != null && response.data().time_remaining() != null) {
      System.out.println(response.data().time_remaining().next_starts_in_minutes());
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