package com.example.patient.domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InMemoryPatientService {
    private final List<Patient> patients = new ArrayList<>();

    public Patient create(NewPatient newPatient) {
        Patient patient = new Patient(UUID.randomUUID(), newPatient.getFirstName(), newPatient.getLastName(),newPatient.getEmail());
        patients.add(patient);
        return patient;
    }

    public Optional<Patient> update(Patient updatedPatient) {
        return find(updatedPatient.getId()).map(p -> {
            patients.remove(p);
            patients.add(updatedPatient);
            return updatedPatient;
        });
    }

    public Optional<Patient> find(UUID id) {
        return patients.stream().filter(patient -> patient.getId().equals(id)).findFirst();
    }

    public List<Patient> list() {
        return patients;
    }

    public void delete(UUID id) {
        patients.removeIf(patient -> patient.getId().equals(id));
    }
}
