package com.example.patient.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryPatientServiceTest {
    private InMemoryPatientService patientService = new InMemoryPatientService();

    @Test
    void createReturnsCreatedPatient() {
        Patient patient = patientService.create(testNewPatient());

        assertThat(patient).usingRecursiveComparison().isEqualTo(testPatientWithId(patient.getId()));
    }

    @Test
    void updateSetsExistingPatient() {
        Patient patient = patientService.create(testNewPatient());
        Patient modifiedPatient = new Patient(patient.getId(), "different-first-name", patient.getLastName(), patient.getEmail());

        Optional<Patient> updatedPatient = patientService.update(modifiedPatient);

        assertThat(updatedPatient).get().usingRecursiveComparison().isEqualTo(modifiedPatient);
        assertThat(patientService.find(modifiedPatient.getId())).get().usingRecursiveComparison().isEqualTo(modifiedPatient);
    }

    @Test
    void update_WithNonExistentId_ReturnsEmpty() {
        Optional<Patient> potentialPatient = patientService.update(testPatientWithId(UUID.randomUUID()));

        assertThat(potentialPatient).isEmpty();
    }

    @Test
    void findReturnsPatient() {
        Patient createdPatient = patientService.create(testNewPatient());
        UUID patientId = createdPatient.getId();

        Optional<Patient> patient = patientService.find(patientId);

        assertThat(patient).get().usingRecursiveComparison().isEqualTo(testPatientWithId(patientId));
    }

    @Test
    void findReturnsEmpty() { // FIXME is this valuable, compared to find_WithNonExistentId_ReturnsEmpty?
        Optional<Patient> patient = patientService.find(UUID.randomUUID());

        assertThat(patient).isEmpty();
    }

    @Test
    void find_WithNonExistentId_ReturnsEmpty() {
        patientService.create(testNewPatient());
        UUID nonExistentPatientId = UUID.randomUUID();

        Optional<Patient> patient = patientService.find(nonExistentPatientId);

        assertThat(patient).isEmpty();
    }

    @Test
    void listReturnsEmpty() { // FIXME is this independently valuable?
        List<Patient> patients = patientService.list();

        assertThat(patients).isEmpty();
    }

    @Test
    void listReturnsCreatedPatients() {
        Patient firstPatient = patientService.create(new NewPatient("some-other-first-name", "some-other-last-name", "some-other-email"));
        Patient secondPatient = patientService.create(testNewPatient());

        List<Patient> patients = patientService.list();

        assertThat(patients).usingRecursiveFieldByFieldElementComparator().containsExactly(
                new Patient(firstPatient.getId(), "some-other-first-name", "some-other-last-name", "some-other-email"),
                testPatientWithId(secondPatient.getId())
        );
    }

    @Test
    void deleteRemovesPatient() {
        Patient patient = patientService.create(testNewPatient());

        patientService.delete(patient.getId());

        assertThat(patientService.find(patient.getId())).isEmpty();
    }

    private Patient testPatientWithId(UUID patientId) {
        return new Patient(patientId, "some-first-name","some-last-name", "some-email");
    }

    private static NewPatient testNewPatient() {
        return new NewPatient("some-first-name","some-last-name", "some-email");
    }
}
