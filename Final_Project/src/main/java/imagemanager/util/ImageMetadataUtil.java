package imagemanager.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

// This utility class provides a method to extract metadata from image files.
// It uses the Metadata Extractor library to read metadata from various image formats.
// The extractMetadata method takes a File object as input and returns a Map containing the metadata.
// The Map contains key-value pairs where the key is the metadata tag name and the value is the tag description.
// The method handles exceptions and returns an error message if metadata extraction fails.

public class ImageMetadataUtil {
    public static Map<String, String> extractMetadata(File file) {
        Map<String, String> result = new HashMap<>();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    result.put(tag.getTagName(), tag.getDescription());
                }
            }
        } catch (Exception e) {
            result.put("Error", "Failed to extract metadata");
        }
        return result;
    }
}
