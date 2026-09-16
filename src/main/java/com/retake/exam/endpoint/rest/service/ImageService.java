package com.retake.exam.endpoint.rest.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  public byte[] resizeToThumbnail(byte[] imageBytes) throws IOException {
    BufferedImage source = ImageIO.read(new ByteArrayInputStream(imageBytes));

    if (source == null) {
      throw new IOException("Invalid image");
    }

    Image scaled = source.getScaledInstance(256, 256, Image.SCALE_SMOOTH);

    BufferedImage thumbnail = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics = thumbnail.createGraphics();
    graphics.drawImage(scaled, 0, 0, null);
    graphics.dispose();

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageIO.write(thumbnail, "jpg", output);

    return output.toByteArray();
  }
}
