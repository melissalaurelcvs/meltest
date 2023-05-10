package com.example.patient.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "PATIENT")
@Getter
@Setter
class PatientEntity {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
}
