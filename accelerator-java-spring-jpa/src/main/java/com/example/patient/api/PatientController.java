package com.example.patient.api;

import com.example.patient.domain.NewPatient;
import com.example.patient.domain.Patient;
import com.example.patient.domain.PersistedPatientService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/patient")
@OpenAPIDefinition(
        info = @Info(title = "Patient Management API", version = "1.0"),
        tags = @Tag(name = "Patient REST API")
)
class PatientController {

    private final PersistedPatientService patientService;

    PatientController(PersistedPatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Creates a patient", method = "POST", tags = "Patient CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Patient successfully created",
                    headers = @Header(
                            name = "Location",
                            description = "Contains path which can be used to retrieve saved patient",
                            required = true,
                            schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Passed patient data is invalid"
            )
    })
    public ResponseEntity<Void> createPatient(@RequestBody @Valid PatientModificationRequest request) {
        NewPatient newPatient = toNewPatient(request);
        Patient savedPatient = patientService.create(newPatient);
        return created(toLocationUri(savedPatient.getId())).build();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get patient", method = "GET", tags = "Patient CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found."
            )
    })
    public ResponseEntity<PatientResponse> toUpdatedPatient(@PathVariable UUID id) {
        return patientService
                .find(id)
                .map(patient -> ok(toPatientResponse(patient)))
                .orElseGet(() -> notFound().build());

    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get list of patients", method = "GET", tags = "Patient CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patients retrieved successfully"
            ),
    })
    public List<PatientResponse> listPatients() {
        return patientService.list().stream()
                .map(this::toPatientResponse)
                .collect(toList());
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing patient", method = "PUT", tags = "Patient CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Passed patient data is invalid"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Patient not found."
            )

    })
    ResponseEntity<PatientResponse> updatePatient(@PathVariable UUID id, @RequestBody @Valid PatientModificationRequest request) {
        Patient patient = toUpdatedPatient(id, request);
        Optional<Patient> updatedPatient = patientService.update(patient);
        return updatedPatient
                .map(p -> ResponseEntity.ok(toPatientResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", method = "DELETE", tags = "Patient CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Patient deleted successfully"
            ),
    })
    public void deletePatient(@PathVariable UUID id) {
        patientService.delete(id);
    }

    private NewPatient toNewPatient(PatientModificationRequest request) {
        return new NewPatient(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );
    }

    private Patient toUpdatedPatient(UUID id, PatientModificationRequest request) {
        return new Patient(id, request.getFirstName(), request.getLastName(), request.getEmail());
    }

    private PatientResponse toPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail()
        );
    }

    private URI toLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
