package com.example.patient.data;

import com.example.patient.domain.NewPatient;
import com.example.patient.domain.Patient;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class PatientRepository {

    private final PatientJpaRepository jpaRepository;

    public PatientRepository(PatientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public Patient create(NewPatient newPatient) {
        PatientEntity savedEntity = jpaRepository.save(toEntity(newPatient));
        return toPatient(savedEntity);
    }

    public Optional<Patient> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toPatient);
    }

    public List<Patient> findAll() {
        return StreamSupport.stream(jpaRepository.findAll().spliterator(), false)
                .map(this::toPatient)
                .collect(toList());
    }

    public Optional<Patient> update(Patient patient) {
        if (jpaRepository.existsById(patient.getId())) {
            PatientEntity savedEntity = jpaRepository.save(toEntity(patient));
            return Optional.of(toPatient(savedEntity));
        } else {
            return Optional.empty();
        }
    }

    public boolean delete(UUID id) {
        try {
            jpaRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private PatientEntity toEntity(NewPatient newPatient) {
        PatientEntity entity = new PatientEntity();
        entity.setFirstName(newPatient.getFirstName());
        entity.setLastName(newPatient.getLastName());
        entity.setEmail(newPatient.getEmail());
        return entity;
    }

    private PatientEntity toEntity(Patient patient) {
        PatientEntity entity = new PatientEntity();
        entity.setId(patient.getId());
        entity.setFirstName(patient.getFirstName());
        entity.setLastName(patient.getLastName());
        entity.setEmail(patient.getEmail());
        return entity;
    }

    private Patient toPatient(PatientEntity patientEntity) {
        return new Patient(
                patientEntity.getId(),
                patientEntity.getFirstName(),
                patientEntity.getLastName(),
                patientEntity.getEmail()
        );
    }
}
