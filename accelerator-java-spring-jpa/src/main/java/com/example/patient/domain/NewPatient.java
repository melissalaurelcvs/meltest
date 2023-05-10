package com.example.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter @AllArgsConstructor @ToString
public class NewPatient {
    private final String firstName;
    private final String lastName;
    private final String email;
}
