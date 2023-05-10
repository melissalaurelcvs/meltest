package com.example.patient.domain;

import com.example.patient.data.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersistedPatientService {

    private final PatientRepository patientRepository;

    public PersistedPatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(NewPatient newPatient) {
        return patientRepository.create(newPatient);
    }

    public Optional<Patient> update(Patient updatedPatient) {
        return patientRepository.update(updatedPatient);
    }

    public Optional<Patient> find(UUID id) {
        return patientRepository.findById(id);
    }

    public List<Patient> list() {
        return patientRepository.findAll();
    }

    public void delete(UUID id) {
        patientRepository.delete(id);
    }
}
