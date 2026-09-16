package com.retake.exam.endpoint.rest.service;

import com.retake.exam.file.bucket.BucketComponent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StorageService {

  private final BucketComponent bucketComponent;

  public void upload(byte[] content, String key) throws IOException {
    File file = File.createTempFile("thumbnail-", ".jpg");

    try {
      try (FileOutputStream output = new FileOutputStream(file)) {
        output.write(content);
      }

      bucketComponent.upload(file, key);
    } finally {
      file.delete();
    }
  }

  public byte[] download(String key) throws IOException {
    File file = bucketComponent.download(key);
    try {
      return Files.readAllBytes(file.toPath());
    } finally {
      file.delete();
    }
  }

  public String presign(String key) {
    return bucketComponent.presign(key, Duration.ofMinutes(10)).toString();
  }
}