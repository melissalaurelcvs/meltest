package com.example.patient.data;

import com.example.patient.domain.NewPatient;
import com.example.patient.domain.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class CachedPatientRepositoryWithCacheTest {

    @MockBean
    private PatientJpaRepository jpaRepository;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CachedPatientRepository patientRepository;

    @AfterEach
    void tearDown() {
        cacheManager.getCache("patients").clear();
        cacheManager.getCache("patient-list").clear();
    }

    @Test
    void findByIdCachesResults() {
        UUID patientId = UUID.randomUUID();
        when(jpaRepository.findById(patientId)).thenReturn(Optional.of(new PatientEntity()));

        Patient cacheMiss = patientRepository.findById(patientId).get();
        Patient cacheHit = patientRepository.findById(patientId).get();

        assertThat(cacheMiss).isNotNull();
        assertThat(cacheHit).isNotNull();
        assertThat(cacheManager.getCache("patients").get(patientId).get())
                .usingRecursiveComparison().isEqualTo(cacheHit);
        verify(jpaRepository, times(1)).findById(patientId);
    }

    @Test
    void findAllCachesResults() {
        when(jpaRepository.findAll()).thenReturn(Collections.singleton(new PatientEntity()));

        List<Patient> cacheMiss = patientRepository.findAll();
        List<Patient> cacheHit = patientRepository.findAll();

        assertThat(cacheMiss).isNotEmpty();
        assertThat(cacheHit).isNotEmpty();
        verifyCacheMissCount(1);
    }

    @Test
    void createEvictsPatientList() {
        when(jpaRepository.save(any())).thenReturn(new PatientEntity());
        populateCache();

        patientRepository.create(new NewPatient("some-first-name","some-last-name", "some-email@example.com"));
        populateCache();

        verifyCacheMissCount(2);
    }
    @Test
    void updateEvictsPatientList() {
        when(jpaRepository.save(any())).thenReturn(new PatientEntity());
        patientRepository.findAll();

        patientRepository.update(new Patient(UUID.randomUUID(),"some-first-name","some-last-name", "some-email@example.com"));
        patientRepository.findAll();

        verify(jpaRepository, times(2)).findAll();
    }
    @Test
    void deleteEvictsPatientList() {
        populateCache();

        patientRepository.delete(UUID.randomUUID());
        populateCache();

        verifyCacheMissCount(2);
    }

    @Test
    void deleteEvictsSpecificPatient() {
        UUID patientId = UUID.randomUUID();
        when(jpaRepository.findById(patientId)).thenReturn(Optional.of(new PatientEntity()));
        patientRepository.findById(patientId);

        patientRepository.delete(patientId);
        patientRepository.findById(patientId);

        verify(jpaRepository, times(2)).findById(patientId);
    }

    private void populateCache() {
        patientRepository.findAll();
    }

    private void verifyCacheMissCount(int wantedNumberOfInvocations) {
        verify(jpaRepository, times(wantedNumberOfInvocations)).findAll();
    }
}
