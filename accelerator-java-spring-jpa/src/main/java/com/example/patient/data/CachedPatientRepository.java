package com.example.patient.data;

import com.example.patient.domain.NewPatient;
import com.example.patient.domain.Patient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class CachedPatientRepository {

    private final PatientJpaRepository jpaRepository;

    public CachedPatientRepository(PatientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @CacheEvict(cacheNames = "patient-list", allEntries = true)
    public Patient create(NewPatient newPatient) {
        PatientEntity savedEntity = jpaRepository.save(toEntity(newPatient));
        return toPatient(savedEntity);
    }

    @Cacheable(cacheNames = "patients", key = "#id")
    public Optional<Patient> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toPatient);
    }

    @Cacheable("patient-list")
    public List<Patient> findAll() {
        return StreamSupport.stream(jpaRepository.findAll().spliterator(), false)
                .map(this::toPatient)
                .collect(toList());
    }
    @CacheEvict(cacheNames = "patient-list", allEntries = true)
    public Patient update(Patient updatedPatient) {
        PatientEntity savedEntity = jpaRepository.save(toUpdateEntity(updatedPatient));
        return toPatient(savedEntity);
    }
    @Caching(evict = {
            @CacheEvict(cacheNames = "patients", key = "#id"),
            @CacheEvict(cacheNames = "patient-list", allEntries = true)
    })
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

    private Patient toPatient(PatientEntity patientEntity) {
        return new Patient(
                patientEntity.getId(),
                patientEntity.getFirstName(),
                patientEntity.getLastName(),
                patientEntity.getEmail()
        );
    }
    private PatientEntity toUpdateEntity(Patient updatedPatient) {
        PatientEntity entity = new PatientEntity();
        entity.setId(updatedPatient.getId());
        entity.setFirstName(updatedPatient.getFirstName());
        entity.setLastName(updatedPatient.getLastName());
        entity.setEmail(updatedPatient.getEmail());
        return entity;
    }
}
