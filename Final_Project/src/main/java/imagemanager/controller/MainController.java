/* This file is part of ImageManager.
 * ImageManager is a simple JavaFX application for managing and converting images.
 * It allows users to upload images, view their metadata, and convert them to different formats.
*/
package imagemanager.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import imagemanager.model.ImageItem;
import imagemanager.model.converter.ImageConverter;
import imagemanager.model.converter.JPGConverter;
import imagemanager.model.converter.PNGConverter;
import imagemanager.util.ImageMetadataUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
    @FXML private FlowPane thumbnailPane;
    @FXML private TextArea metadataArea;
    @FXML private ChoiceBox<String> formatChoiceBox;
    private List<ImageItem> images = new ArrayList<>();
    private ImageItem selectedImage;

    // Initialize the controller
    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    public void initialize() {
        formatChoiceBox.setItems(FXCollections.observableArrayList("JPG", "PNG"));
        formatChoiceBox.getSelectionModel().selectFirst();
    }
    
    // Handle the upload button click
    // This method is called when the user clicks the "Upload" button
    // It opens a file chooser dialog to select image files
    // The selected images are displayed as thumbnails in the FlowPane
    // The user can select multiple images at once
    // The metadata of each image is extracted and stored in the ImageItem object
    @FXML
    public void onUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image(s)");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (files != null) {
            for (File file : files) {
                try {
                    Image fxImage = new Image(file.toURI().toString(), 100, 100, true, true);
                    Map<String, String> metadata = ImageMetadataUtil.extractMetadata(file);
                    ImageItem item = new ImageItem(file, file.getName(), (int)fxImage.getWidth(), (int)fxImage.getHeight(), metadata);
                    images.add(item);

                    ImageView imageView = new ImageView(fxImage);
                    imageView.setUserData(item);
                    imageView.setOnMouseClicked(e -> showMetadata(item));
                    thumbnailPane.getChildren().add(imageView);
                } catch (Exception ex) {
                    showAlert("Upload Failed", ex.getMessage());
                }
            }
        }
    }

    // The metadata is displayed in the TextArea when an image is clicked
    // The metadata includes the image name, width, height, and any additional metadata
    private void showMetadata(ImageItem item) {
        selectedImage = item;
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(item.getName()).append("\n");
        sb.append("Width: ").append(item.getWidth()).append("\n");
        sb.append("Height: ").append(item.getHeight()).append("\n");
        for (Map.Entry<String, String> entry : item.getMetadata().entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        metadataArea.setText(sb.toString());
    }

    // This method is called when the user clicks the "Convert" button
    // It converts the selected image to the chosen format (JPG or PNG)
    // The converted image is saved to a new file using a file chooser dialog
    // The user can choose the location and name of the converted file
    @FXML
    public void onConvert() {
        if (selectedImage == null) {
            showAlert("Notice", "Please select an image first.");
            return;
        }
        String format = formatChoiceBox.getValue();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.setInitialFileName(selectedImage.getName() + "." + format.toLowerCase());
        File output = fileChooser.showSaveDialog(new Stage());
        if (output != null) {
            try {
                ImageConverter converter = getConverter(format);
                converter.convert(selectedImage.getFile(), output);
                showAlert("Success", "Image converted and saved!");
            } catch (IOException ex) {
                showAlert("Conversion Failed", ex.getMessage());
            }
        }
    }

    // This method returns the appropriate ImageConverter based on the selected format
    // It uses a switch expression to determine which converter to use
    private ImageConverter getConverter(String format) {
        return switch (format.toUpperCase()) {
            case "JPG" -> new JPGConverter();
            case "PNG" -> new PNGConverter();
            default -> throw new IllegalArgumentException("Unsupported format");
        };
    }

    // This method shows an alert dialog with the specified title and message
    // It is used to inform the user about success or failure of operations
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
