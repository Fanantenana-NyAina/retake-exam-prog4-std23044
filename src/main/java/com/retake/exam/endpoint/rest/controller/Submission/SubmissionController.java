package com.retake.exam.endpoint.rest.controller.Submission;

import com.retake.exam.endpoint.event.EventProducer;
import com.retake.exam.endpoint.event.model.SubmissionProcessingRequested;
import com.retake.exam.endpoint.rest.model.Submission;
import com.retake.exam.endpoint.rest.repository.SubmissionRepository;
import com.retake.exam.endpoint.rest.service.StorageService;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class SubmissionController {

  private final SubmissionRepository submissionRepository;
  private final EventProducer<SubmissionProcessingRequested> eventProducer;
  private final StorageService storageService;

  @PostMapping(value = "/submissions", consumes = "multipart/form-data")
  public ResponseEntity<Submission> createSubmission(
          @RequestParam MultipartFile file, @RequestParam String email) throws IOException {

    UUID id = UUID.randomUUID();

    Submission submission =
            Submission.builder()
                    .id(id)
                    .email(email)
                    .thumbnailKey(null)
                    .createdAt(Instant.now())
                    .build();

    submissionRepository.save(submission);

    String extension = "image/png".equals(file.getContentType()) ? ".png" : ".jpg";
    String originalKey = "originals/" + id + extension;
    storageService.upload(file.getBytes(), originalKey);

    var event =
            SubmissionProcessingRequested.builder()
                    .submissionId(id)
                    .email(email)
                    .originalKey(originalKey)
                    .build();

    eventProducer.accept(List.of(event));

    return ResponseEntity.status(HttpStatus.CREATED).body(submission);
  }

  @GetMapping("/submissions")
  public List<Submission> listSubmissions() {
    return submissionRepository.findAll();
  }
}