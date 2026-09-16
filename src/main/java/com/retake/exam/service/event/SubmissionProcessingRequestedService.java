package com.retake.exam.service.event;

import com.retake.exam.endpoint.event.model.SubmissionProcessingRequested;
import com.retake.exam.endpoint.rest.model.Submission;
import com.retake.exam.endpoint.rest.repository.SubmissionRepository;
import com.retake.exam.endpoint.rest.service.ImageService;
import com.retake.exam.endpoint.rest.service.StorageService;
import com.retake.exam.mail.Email;
import com.retake.exam.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubmissionProcessingRequestedService
    implements Consumer<SubmissionProcessingRequested> {

  private final SubmissionRepository submissionRepository;
  private final ImageService imageService;
  private final StorageService storageService;
  private final Mailer mailer;

  @Override
  @SneakyThrows
  public void accept(SubmissionProcessingRequested event) {

    byte[] original = storageService.download(event.getOriginalKey());
    byte[] thumbnail = imageService.resizeToThumbnail(original);

    String thumbnailKey = "thumbnails/" + event.getSubmissionId() + ".jpg";

    storageService.upload(thumbnail, thumbnailKey);

    Submission submission = submissionRepository.findById(event.getSubmissionId()).orElseThrow();

    submission.setThumbnailKey(thumbnailKey);
    submissionRepository.save(submission);

    String downloadUrl = storageService.presign(thumbnailKey);

    mailer.accept(
        new Email(
            new InternetAddress(event.getEmail()),
            List.of(),
            List.of(),
            "Your thumbnail is ready",
            "<p>Your thumbnail is ready.</p>"
                + "<p><a href=\""
                + downloadUrl
                + "\">Download</a></p>",
            List.of()));
  }
}
