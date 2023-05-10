package com.example.patient.domain;

import com.example.patient.data.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersistedPatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    private PersistedPatientService patientService;
    private final UUID patientId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        patientService = new PersistedPatientService(patientRepository);
    }

    @Test
    void createDelegatesToRepository() {
        Patient testPatient = testPatient();
        when(patientRepository.create(any())).thenReturn(testPatient);
        NewPatient newPatient = new NewPatient("some-first-name","some-last-name", "some-email");

        Patient patient = patientService.create(newPatient);

        assertThat(patient).isSameAs(testPatient);
        verify(patientRepository).create(newPatient);
    }

    @Test
    void updateDelegatesToRepository() {
        Patient updatedPatient = testPatient();
        when(patientRepository.update(any())).thenReturn(Optional.of(updatedPatient));
        Patient existingPatient = new Patient(patientId,"some-first-name","some-last-name", "some-email");

        Optional<Patient> patient = patientService.update(existingPatient);

        assertThat(patient).get().isSameAs(updatedPatient);
        verify(patientRepository).update(existingPatient);
    }

    @Test
    void findDelegatesToRepository() {
        Patient testPatient = testPatient();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(testPatient));

        Optional<Patient> patient = patientService.find(patientId);

        assertThat(patient).get().isSameAs(testPatient);
        verify(patientRepository).findById(patientId);
    }

    @Test
    void listDelegatesToRepository() {
        List<Patient> testPatients = Arrays.asList(
                new Patient(UUID.randomUUID(), "first-name-one","last-name-one", "email-two"),
                new Patient(UUID.randomUUID(), "first-name-two", "last-name-two","email-two")
        );
        when(patientRepository.findAll()).thenReturn(testPatients);

        List<Patient> patients = patientService.list();

        assertThat(patients).containsExactlyElementsOf(testPatients);
        verify(patientRepository).findAll();
    }

    @Test
    void deleteDelegatesToRepository() {
        patientService.delete(patientId);

        verify(patientRepository).delete(patientId);
    }

    private Patient testPatient() {
        return new Patient(patientId, "some-first-name","some-last-name", "some-email");
    }
}
