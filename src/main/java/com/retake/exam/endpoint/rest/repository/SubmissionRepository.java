package com.retake.exam.endpoint.rest.repository;

import com.retake.exam.endpoint.rest.model.Submission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {}
