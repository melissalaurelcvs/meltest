package com.example.patient.data;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

interface PatientJpaRepository extends CrudRepository<PatientEntity, UUID> {
}
