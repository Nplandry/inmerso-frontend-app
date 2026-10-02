package com.example.services;

import com.example.dto.ScheduleResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
  private final RestTemplate restTemplate;

  public ApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ScheduleResponse obtenerTarea() {
    return restTemplate.getForObject(
        "https://tu-api-backend.com/schedule",
        ScheduleResponse.class
    );
  }
}
