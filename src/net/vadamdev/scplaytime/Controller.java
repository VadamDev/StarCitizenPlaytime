package net.vadamdev.scplaytime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author VadamDev
 * @since 08/12/2023
 */
public class Controller {
    private static final File CHOOSER_PATH = new File("C:\\Program Files\\Roberts Space Industries\\StarCitizen\\LIVE");
    private static final String DEFAULT_PATH = "C:\\Program Files\\Roberts Space Industries\\StarCitizen\\LIVE\\logbackups";

    private static final String RESULT_HEADER = "Current playtime:        ";

    @FXML
    private Button calculateButton;

    @FXML
    private TextField scPath;

    @FXML
    private Button choosePathButton;

    @FXML
    private Text resultText;

    @FXML
    private void initialize() {
        scPath.setText(DEFAULT_PATH);
        resultText.setText(RESULT_HEADER);
    }

    @FXML
    void onCalculateButtonClicked(ActionEvent event) {
        calculateButton.setDisable(true);
        scPath.setDisable(true);
        choosePathButton.setDisable(true);
        resultText.setText(RESULT_HEADER);

        Main.asyncExecutor.submit(() -> {
            final File directory = new File(scPath.getText());
            if(!directory.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occured");
                alert.setContentText("Could not find directory in provided path !");
                alert.showAndWait();

                return;
            }

            final File[] content = directory.listFiles();
            if(!PlaytimeHelper.hasLogFile(content)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error occured");
                alert.setContentText("Could not find any log file in provided path !");
                alert.showAndWait();

                return;
            }

            long total = 0;
            for(File file : content) {
                if(!file.getName().endsWith(".log"))
                    continue;

                total += PlaytimeHelper.calculateLogPlaytime(file);
            }

            final File latest = new File(directory.getParentFile(), "Game.log");
            if(latest.exists())
                total += PlaytimeHelper.calculateLogPlaytime(latest);

            resultText.setText(RESULT_HEADER + String.format("%dh %dm %ds", TimeUnit.MILLISECONDS.toHours(total), TimeUnit.MILLISECONDS.toMinutes(total) % 60, TimeUnit.MILLISECONDS.toSeconds(total) % 60));

            calculateButton.setDisable(false);
            scPath.setDisable(false);
            choosePathButton.setDisable(false);
        });
    }

    @FXML
    void onChoosePathButtonClicked(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(CHOOSER_PATH);

        final File file = directoryChooser.showDialog(null);
        if(file != null)
            scPath.setText(file.getAbsolutePath());
    }
}
