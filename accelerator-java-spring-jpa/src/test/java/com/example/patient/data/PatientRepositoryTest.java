package com.example.patient.data;

import com.example.patient.domain.NewPatient;
import com.example.patient.domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PatientRepositoryTest {

    @Autowired
    private PatientJpaRepository jpaRepository;

    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository = new PatientRepository(jpaRepository);
        jpaRepository.deleteAll();
    }

    @Test
    void createsNewPatient() {
        insertTestPatient();

        List<Patient> patients = patientRepository.findAll();
        assertThat(patients)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(new Patient(null, "Joe", "Doe", "joe.doe@example.com"));
    }

    @Test
    void updatesExistingPatient() {
        Patient savedPatient = insertTestPatient();
        Patient patientToBeUpdated = new Patient(savedPatient.getId(), "new first name", savedPatient.getLastName(), savedPatient.getEmail());

        Optional<Patient> updatedPatient = patientRepository.update(patientToBeUpdated);

        assertThat(updatedPatient).get()
                .usingRecursiveComparison()
                .isEqualTo(patientToBeUpdated);
        assertThat(patientRepository.findById(savedPatient.getId())).get()
                .usingRecursiveComparison()
                .isEqualTo(patientToBeUpdated);
    }

    @Test
    void update_ForNonExistentId_ReturnsEmpty() {
        Patient patientWithNonExistentId = new Patient(UUID.randomUUID(), "first-name", "last-name", "em@il.com");

        Optional<Patient> updatedPatient = patientRepository.update(patientWithNonExistentId);

        assertThat(updatedPatient).isEmpty();
    }

    @Test
    void findsPatientById() {
        Patient savedPatient = insertTestPatient();

        Optional<Patient> foundPatient = patientRepository.findById(savedPatient.getId());

        assertThat(foundPatient).get().usingRecursiveComparison().isEqualTo(savedPatient);
    }

    @Test
    void find_ForNonExistentId_ReturnsEmpty() {
        Optional<Patient> potentialPatient = patientRepository.findById(UUID.randomUUID());

        assertThat(potentialPatient).isEmpty();
    }

    @Test
    void deletesPatient() {
        Patient savedPatient = insertTestPatient();

        boolean deleted = patientRepository.delete(savedPatient.getId());

        assertThat(deleted).isTrue();
        assertThat(patientRepository.findAll()).isEmpty();
    }

    @Test
    void deleteReturnsFalseWhenNoPatient() {
        boolean deleted = patientRepository.delete(UUID.randomUUID());

        assertThat(deleted).isFalse();
    }

    private Patient insertTestPatient() {
        return patientRepository.create(
                new NewPatient(
                        "Joe",
                        "Doe",
                        "joe.doe@example.com"
                ));
    }
}
