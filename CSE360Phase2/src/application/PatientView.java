package application;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PatientView {
	private Stage stage;
	private String patientId;
	String directoryPath = System.getProperty("user.dir");
	private final String imagesDirectoryPath = directoryPath + "/";
	private Map<String, Control> controls = new HashMap<>();
	private CheckBox isChildOver12Checkbox;

	public PatientView(Stage stage, String patientId) {
		this.stage = stage;
		this.patientId = patientId;
		initializeControls();
		displayPatientInfo();
	}

	private void initializeControls() {
		// Initialize controls similarly to NurseView
		String[] controlKeys = { "PATIENTID", "FIRSTNAME", "LASTNAME", "DOB", "WEIGHT", "HEIGHT", "BODYTEMPERATURE",
				"BLOODPRESSURE", "KNOWNALLERGIES", "HEALTHCONCERNS", "PATIENTHISTORY", "PRESCRIBEDMEDICATIONS",
				"IMMUNIZATIONRECORDS", "CONTACTINFORMATION", "CHILDOVER12" // Assuming the last two are specific to
																			// PatientView
		};

		for (String key : controlKeys) {
			if (key.equals("CONTACTINFORMATION")) {
				TextArea textArea = new TextArea();
				textArea.setPrefHeight(100);
				controls.put(key, textArea);
			} else {
				TextField textField = new TextField();
				controls.put(key, textField);
			}
		}
		controls.put("DOCTORFINDINGS", new TextArea());
		((TextArea) controls.get("DOCTORFINDINGS")).setEditable(false); // Making it read-only
		((TextArea) controls.get("DOCTORFINDINGS")).setPrefHeight(100);

		controls.put("DOCTORREMARKS", new TextArea());
		((TextArea) controls.get("DOCTORREMARKS")).setEditable(false); // Making it read-only
		((TextArea) controls.get("DOCTORREMARKS")).setPrefHeight(100);

		isChildOver12Checkbox = new CheckBox("Child over 12");
		controls.put("CHILDOVER12", isChildOver12Checkbox);
	}

	private HBox createHeader() {
		HBox header = new HBox();
		header.setBackground(
				new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
		header.setPadding(new Insets(15));
		header.setSpacing(10);
		header.setAlignment(Pos.CENTER_LEFT);

		ImageView logo = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
		logo.setFitHeight(50);
		logo.setPreserveRatio(true);
		header.getChildren().add(logo);

		Label title = new Label("Patient Portal");
		title.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
		header.getChildren().add(title);

		Button backButton = new Button("Log Out");
		backButton.setOnAction(event -> {
			MainGUI mainGUI = new MainGUI(stage);
			mainGUI.showMainScreen(); // Show the main screen when Back button is pressed
		});
		header.getChildren().add(backButton);

		HBox rightHeader = new HBox();
		rightHeader.setAlignment(Pos.CENTER_RIGHT);

		ImageView bellIcon = new ImageView(new Image("file:" + imagesDirectoryPath + "/bell.png"));
		bellIcon.setFitHeight(30);
		bellIcon.setPreserveRatio(true);
		Button viewMessagesButton = new Button("", bellIcon);
		viewMessagesButton.setStyle("-fx-background-color: transparent;");
		viewMessagesButton.setOnAction(event -> {
			// Instantiate MessagingPortal with correct parameters
			MessagingPortal messagingPortal = new MessagingPortal();
			messagingPortal.showMessagingPortal(false);
		});

		Label viewMessagesLabel = new Label("Messages");
		viewMessagesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
		rightHeader.getChildren().addAll(viewMessagesLabel, viewMessagesButton);
		HBox.setHgrow(rightHeader, Priority.ALWAYS);

		header.getChildren().add(rightHeader);

		return header;
	}

	private GridPane createCenterGrid() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setAlignment(Pos.TOP_CENTER);

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);
		grid.getColumnConstraints().addAll(col1, col2);

		addLabelAndControl(grid, "Patient ID", "PATIENTID", 0, 0, true);
		addLabelAndControl(grid, "First Name", "FIRSTNAME", 0, 2, true);
		addLabelAndControl(grid, "Last Name", "LASTNAME", 0, 4, true);
		addLabelAndControl(grid, "DOB", "DOB", 0, 6, true);
		addLabelAndControl(grid, "Child over 12", "CHILDOVER12", 0, 8, true);
		addLabelAndControl(grid, "Weight", "WEIGHT", 0, 10, true);
		addLabelAndControl(grid, "Height", "HEIGHT", 0, 12, true);
		addLabelAndControl(grid, "Body Temperature", "BODYTEMPERATURE", 0, 14, true);
		addLabelAndControl(grid, "Blood Pressure", "BLOODPRESSURE", 0, 16, true);

		// Add right side elements with more width for text areas
		addLabelAndControl(grid, "Known Allergies", "KNOWNALLERGIES", 1, 0, true);
		addLabelAndControl(grid, "Health Concerns", "HEALTHCONCERNS", 1, 2, true);
		addLabelAndControl(grid, "Patient History", "PATIENTHISTORY", 1, 4, true);
		addLabelAndControl(grid, "Prescribed Medications", "PRESCRIBEDMEDICATIONS", 1, 7, true);
		addLabelAndControl(grid, "Immunization Records", "IMMUNIZATIONRECORDS", 1, 10, true);
		addLabelAndControl(grid, "Contact Information", "CONTACTINFORMATION", 1, 13, false);
		addLabelAndControl(grid, "Doctor Findings", "DOCTORFINDINGS", 1, 16, true);
		addLabelAndControl(grid, "Doctor Remarks", "DOCTORREMARKS", 0, 19, true);

		// Add Save button
		Button saveButton = new Button("Save");
		saveButton.setMaxWidth(100);
		GridPane.setHalignment(saveButton, HPos.RIGHT);
		GridPane.setValignment(saveButton, VPos.BOTTOM);
		grid.add(saveButton, 1, 22);
		saveButton.setOnAction(event -> {
			
			 TextArea contInfo = (TextArea) controls.get("CONTACTINFORMATION");
			    String infoChange = contInfo.getText();
			    if (infoChange.isEmpty()) {
			        showAlert("Contact Information is empty.");
			    }
			    else {
			    	savePatientInfo();
			    }
			
		});

		return grid;
	}

	private void addLabelAndControl(GridPane grid, String labelText, String controlKey, int colIndex, int rowIndex,
			boolean isReadOnly) {
		Label label = new Label(labelText + ":");
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		Control control = controls.get(controlKey);

		if (control instanceof TextField) {
			((TextField) control).setEditable(!isReadOnly);
			((TextField) control).setMaxWidth(Double.MAX_VALUE);
			GridPane.setHgrow(control, Priority.ALWAYS); // Allow TextField to grow
		}
		if (control instanceof TextArea) {
			((TextArea) control).setEditable(!isReadOnly);
			((TextArea) control).setPrefHeight(100);
			((TextArea) control).setMaxWidth(Double.MAX_VALUE);
			GridPane.setHgrow(control, Priority.ALWAYS); // Allow TextArea to grow
			GridPane.setValignment(label, VPos.TOP); // Align label to top
			GridPane.setRowSpan(control, 2); // TextArea takes 2 rows in height
		}
		if (control instanceof CheckBox) {
			((CheckBox) control).setAllowIndeterminate(false);
		}

		grid.add(label, colIndex, rowIndex);
		grid.add(control, colIndex, rowIndex + 1); // Add control below the label
		GridPane.setMargin(label, new Insets(0, 0, 0, 3)); // Add some spacing between label and control
	}

	public void displayPatientInfo() {
		BorderPane root = new BorderPane();
		root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		HBox header = createHeader();
		root.setTop(header);

		GridPane centerGrid = createCenterGrid();
		root.setCenter(centerGrid);

		Scene scene = new Scene(root, 1500, 800); // Adjust size as needed
		stage.setScene(scene);
		stage.setTitle("Patient Portal - Patient Information");
		stage.show();

		preloadPatientData();
	}

	private void savePatientInfo() {
        // Locate the existing patient file by listing files in the directory and matching the pattern
        File dir = new File(imagesDirectoryPath);
        String pattern = patientId + "_.*\\.txt"; // Regex pattern to match patient files
        File[] matchingFiles = dir.listFiles((dir1, name) -> name.matches(pattern));

        Path filePath;
        if (matchingFiles != null && matchingFiles.length > 0) {
            // Assuming the first file is the correct one if multiple matches exist
            filePath = matchingFiles[0].toPath();
        } else {
            // Log or notify about the inability to locate the file, if necessary
            System.err.println("No matching patient file found for ID: " + patientId);
            return; // Stop execution if no file is found
        }

        // Only update the "CONTACTINFORMATION" as per requirement
        String contactInfo = ((TextArea) controls.get("CONTACTINFORMATION")).getText();
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("CONTACTINFORMATION:")) {
                    lines.set(i, "CONTACTINFORMATION: " + contactInfo);
                    break; // Stop once the contact info line is updated
                }
            }
        } catch (IOException e) {
            showAlert("Error reading patient data: " + e.getMessage());
            return;
        }

        try {
            Files.write(filePath, lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            showAlert("Patient information updated successfully.");
        } catch (IOException e) {
            showAlert("Error updating patient data: " + e.getMessage());
        }
    }


	private void preloadPatientData() {
        // Attempt to find the file that starts with patientId and ends with .txt
        File dir = new File(imagesDirectoryPath);
        File[] matchingFiles = dir.listFiles((dir1, name) -> name.startsWith(patientId + "_") && name.endsWith(".txt"));

        if (matchingFiles != null && matchingFiles.length > 0) {
            // Assuming only one matching file per patientId, use the first file found
            Path filePath = matchingFiles[0].toPath();
            try {
                List<String> lines = Files.readAllLines(filePath);
                lines.forEach(line -> {
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        String key = parts[0].toUpperCase().replaceAll("\\s+", "");
                        String value = parts[1];
                        Control control = controls.get(key);
                        if (control instanceof CheckBox) {
                            ((CheckBox) control).setSelected("Yes".equals(value));
                        } else if (control instanceof TextInputControl) {
                            ((TextInputControl) control).setText(value);
                        }
                    }
                });
            } catch (IOException e) {
                showAlert("Error loading patient data: " + e.getMessage());
            }
        } else {
            // If no file is found, you might want to notify the user or log
            System.out.println("No data file found for patient ID: " + patientId);
        }
    }

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
