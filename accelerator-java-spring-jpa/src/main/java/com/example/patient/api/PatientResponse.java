package com.example.patient.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter @AllArgsConstructor @ToString
class PatientResponse {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String email;
}
