package com.example.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Patient implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
