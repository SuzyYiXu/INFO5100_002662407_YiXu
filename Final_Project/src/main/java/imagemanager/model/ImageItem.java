package imagemanager.model;

import java.io.File;
import java.util.Map;

// This class represents an image item in the image management tool.
// It contains properties such as the file, name, width, height, and metadata of the image.
// The constructor initializes these properties.
// The class provides getter methods to access the properties.

public class ImageItem {
    private File file;
    private String name;
    private int width;
    private int height;
    private Map<String, String> metadata;

    public ImageItem(File file, String name, int width, int height, Map<String, String> metadata) {
        this.file = file;
        this.name = name;
        this.width = width;
        this.height = height;
        this.metadata = metadata;
    }

    public File getFile() { return file; }
    public String getName() { return name; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Map<String, String> getMetadata() { return metadata; }
}
