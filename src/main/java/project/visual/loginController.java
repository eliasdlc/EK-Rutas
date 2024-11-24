package project.visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.logic.MapController;
import project.logic.User;

import java.io.IOException;
import java.util.Objects;

public class loginController {

    private Stage stage;
    private Scene scene;
    @FXML
    private PasswordField loginPass;
    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private TextField loginUser;
    @FXML
    private PasswordField regPass;
    @FXML
    private TextField regUser;
    @FXML
    private PasswordField confirm;

    @FXML
    private void login(ActionEvent e) throws IOException {
        MapController.loadData();
        String username = loginUser.getText();
        String password = loginPass.getText();
        User user = MapController.getInstance().getConfirmedUser(username, password);
        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                double screenWidth = Screen.getPrimary().getBounds().getWidth();
                double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;
                Scene scene = new Scene(root,screenWidth,screenHeight);
                String css = Objects.requireNonNull(this.getClass().getResource("MainWindow.css")).toExternalForm();
                scene.getStylesheets().add(css);
                stage.setTitle("EK Routes");
                stage.centerOnScreen();
                stage.setMaximized(true);
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void register(ActionEvent e) throws IOException {
        String newUsername = regUser.getText();
        String newPassword = regPass.getText();
        String confirmPassword = confirm.getText();

        if (newPassword.equals(confirmPassword)) {
            // Create new User object
            User newUser = new User(newUsername, newPassword);
            // Add user to the MapController
            MapController.getInstance().getUsers().add(newUser);
            // Save updated data
            MapController.saveData();
        }
    }
}
