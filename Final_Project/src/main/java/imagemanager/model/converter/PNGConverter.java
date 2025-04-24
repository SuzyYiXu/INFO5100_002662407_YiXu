package imagemanager.model.converter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// This class implements the ImageConverter interface for converting images to PNG format.
// It uses the ImageIO class to read and write images in PNG format.
// The convert method takes an input file and an output file as parameters.
// It reads the image from the input file and writes it to the output file in PNG format.
// The method throws an IOException if there is an error during the reading or writing process.

public class PNGConverter implements ImageConverter {
    @Override
    public void convert(File input, File output) throws IOException {
        BufferedImage image = ImageIO.read(input);
        ImageIO.write(image, "png", output);
    }
}
