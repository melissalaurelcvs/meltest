package com.example.patient.data;

import com.example.patient.domain.NewPatient;
import com.example.patient.domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CachedPatientRepositoryTest {

    @Autowired
    private PatientJpaRepository jpaRepository;

    private CachedPatientRepository cachedPatientRepository;

    @BeforeEach
    void setUp() {
        cachedPatientRepository = new CachedPatientRepository(jpaRepository);
        jpaRepository.deleteAll();
    }

    @Test
    void createsNewPatient() {
        insertTestPatient();

        List<Patient> patients = cachedPatientRepository.findAll();
        assertThat(patients)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(new Patient(null, "Joe","Doe", "joe.doe@example.com"));
    }
    @Test
    void updatesExistingPatient() {
        Patient savedPatient = insertTestPatient();
        Patient updatedPatient = updateTestPatient(savedPatient.getId());

        Patient foundPatient  = cachedPatientRepository.findById(savedPatient.getId()).get();
        assertThat(foundPatient).usingRecursiveComparison().isEqualTo(updatedPatient);
    }
    @Test
    void findsPatientById() {
        Patient savedPatient = insertTestPatient();

        Patient foundPatient = cachedPatientRepository.findById(savedPatient.getId()).get();

        assertThat(foundPatient).usingRecursiveComparison().isEqualTo(savedPatient);
    }

    @Test
    void deletesPatient() {
        Patient savedPatient = insertTestPatient();

        boolean deleted = cachedPatientRepository.delete(savedPatient.getId());

        assertThat(deleted).isTrue();
        assertThat(cachedPatientRepository.findAll()).isEmpty();
    }

    @Test
    void deletesReturnsFalseWhenNoPatient() {
        boolean deleted = cachedPatientRepository.delete(UUID.randomUUID());

        assertThat(deleted).isFalse();
    }

    private Patient insertTestPatient() {
        return cachedPatientRepository.create(
                new NewPatient(
                        "Joe",
                        "Doe",
                        "joe.doe@example.com"
                ));
    }
    private Patient updateTestPatient(UUID id) {
        return cachedPatientRepository.update(
                new Patient(
                        id,
                        "Joe1",
                        "Doe",
                        "joe.doe@example.com"
                ));
    }
}
