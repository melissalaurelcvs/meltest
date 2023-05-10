package com.example.patient.api;

import com.example.patient.domain.NewPatient;
import com.example.patient.domain.Patient;
import com.example.patient.domain.PersistedPatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersistedPatientService patientService;
    private final UUID patientId = UUID.randomUUID();

    @Captor
    private ArgumentCaptor<Patient> updateCaptor;

    @Nested
    class Create {

        @BeforeEach
        void setUp() {
            when(patientService.create(any(NewPatient.class)))
                    .thenReturn(testPatient());
        }

        @Test
        void returns201CreatedWithLocationHeader() throws Exception {
            mockMvc.perform(post("/api/patient")
                            .contentType(APPLICATION_JSON)
                            .content(testPatientCreateRequest()))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "http://localhost/api/patient/" + patientId));
        }

        @Test
        void returns400BadRequestForInvalidPayload() throws Exception {
            mockMvc.perform(post("/api/patient")
                            .contentType(APPLICATION_JSON)
                            .content("{\"wrong\": \"payload\"}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void returns415UnsupportedMediaTypeForInvalidContent() throws Exception {
            mockMvc.perform(post("/api/patient")
                            .contentType(APPLICATION_XML)
                            .content(testPatientCreateRequest()))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }

    @Nested
    class Update {
        @Test
        void returns200UpdateExistingPatient() throws Exception {
            when(patientService.update(any())).thenReturn(Optional.of(testPatient()));

            mockMvc.perform(put("/api/patient/" + patientId)
                            .contentType(APPLICATION_JSON)
                            .content(testPatientCreateRequest()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(testPatientResponse()));

            verify(patientService).update(updateCaptor.capture());
            assertThat(updateCaptor.getValue())
                    .usingRecursiveComparison()
                    .isEqualTo(testPatient());
        }

        @Test
        void returns400BadRequestForInvalidPayloadForUpdate() throws Exception {
            mockMvc.perform(put("/api/patient/" + patientId)
                            .contentType(APPLICATION_JSON)
                            .content("{\"wrong\": \"payload\"}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void returns415UnsupportedMediaTypeForInvalidContentForUpdate() throws Exception {
            mockMvc.perform(put("/api/patient/" + patientId)
                            .contentType(APPLICATION_XML)
                            .content(testPatientCreateRequest()))
                    .andExpect(status().isUnsupportedMediaType());
        }

        @Test
        void returns404UpdateExistingPatient() throws Exception {
            when(patientService.update(any())).thenReturn(Optional.empty());
            mockMvc.perform(put("/api/patient/" + patientId)
                            .contentType(APPLICATION_JSON)
                            .content(testPatientCreateRequest()))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetAll {

        @Test
        void returnsEmptyList() throws Exception {
            mockMvc.perform(get("/api/patient").accept(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }

        @Test
        void returnsListOfPatients() throws Exception {
            when(patientService.list())
                    .thenReturn(singletonList(testPatient()));

            mockMvc.perform(get("/api/patient").accept(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[" +
                            "{" +
                            "\"id\": " + patientId + "," +
                            "\"firstName\": \"some-first-name\"," +
                            "\"lastName\": \"some-last-name\"," +
                            "\"email\": \"some-email\"" +
                            "}" +
                            "]"));
        }
    }

    @Nested
    class Get {

        @Test
        void returnsPatientData() throws Exception {
            when(patientService.find(patientId))
                    .thenReturn(Optional.of(testPatient()));

            mockMvc.perform(get("/api/patient/" + patientId).accept(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(testPatientResponse()));
        }

        @Test
        void returns404NotFound() throws Exception {
            mockMvc.perform(get("/api/patient/" + patientId).accept(APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void returns406NotAcceptableWhenAcceptingOnlyXmlResponse() throws Exception {
            when(patientService.list())
                    .thenReturn(singletonList(testPatient()));

            mockMvc.perform(get("/api/patient/" + patientId).accept(APPLICATION_XML))
                    .andExpect(status().isNotAcceptable());
        }
    }

    @Nested
    class Delete {

        @Test
        void returns200ForExistingPatient() throws Exception {
            when(patientService.list())
                    .thenReturn(singletonList(testPatient()));

            mockMvc.perform(delete("/api/patient/" + patientId))
                    .andExpect(status().isOk());
        }

        @Test
        void returns200ForNonExistingPatient() throws Exception {
            mockMvc.perform(delete("/api/patient/" + UUID.randomUUID()))
                    .andExpect(status().isOk());
        }

        @Test
        void returns400ForWrongIdType() throws Exception {
            mockMvc.perform(delete("/api/patient/foo"))
                    .andExpect(status().isBadRequest());
        }
    }

    private Patient testPatient() {
        return new Patient(patientId, "some-first-name","some-last-name", "some-email");
    }

    private String testPatientCreateRequest() {
        return "{\"firstName\": \"some-first-name\", \"lastName\": \"some-last-name\",\"email\": \"some-email\"}";
    }

    private String testPatientResponse() {
        return "{\"id\": " + patientId + ", \"firstName\": \"some-first-name\", \"lastName\": \"some-last-name\", \"email\": \"some-email\"}";
    }
}
