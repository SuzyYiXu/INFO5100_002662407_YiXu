package imagemanager.model.converter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// This class is responsible for converting images to JPG format.
// It implements the ImageConverter interface, which defines the convert method.    
// The convert method takes an input file and an output file as parameters.
// It reads the input image, checks if it has an alpha channel, and if so, removes it.
// Finally, it writes the image to the output file in JPG format.
// The JPG format does not support transparency, so any alpha channel must be removed.

public class JPGConverter implements ImageConverter {
    @Override
    public void convert(File input, File output) throws IOException {
        BufferedImage image = ImageIO.read(input);
        if (image == null) {
            throw new IOException("Invalid image file: " + input.getName());
        }

        // Check if the image has an alpha channel
        if (image.getColorModel().hasAlpha()) {
            // Remove alpha channel
            BufferedImage rgbImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g = rgbImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = rgbImage; // Replace original image with RGB-only image
        }

        // Write the image as JPG
        boolean success = ImageIO.write(image, "jpg", output);
        if (!success) {
            throw new IOException("Failed to write the image as JPG.");
        }
    }
}
