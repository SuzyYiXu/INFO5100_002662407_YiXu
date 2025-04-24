package imagemanager.model.converter;

import java.io.File;
import java.io.IOException;

// This interface defines the contract for image conversion classes.
// It requires implementing classes to provide a method for converting images.
// The convert method takes an input file and an output file as parameters.
public interface ImageConverter {
    void convert(File input, File output) throws IOException;
}
