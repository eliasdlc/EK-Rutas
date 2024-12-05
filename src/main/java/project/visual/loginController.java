package project.visual;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import project.logic.MapController;
import project.logic.User;

import java.io.IOException;
import java.util.Objects;

public class loginController {

    private Stage stage;
    private Scene scene;

    @FXML
    public Tab singInTab, logInTab;
    @FXML
    public TabPane logInTabPane;
    @FXML
    private PasswordField loginPass;
    @FXML
    private Button logInButton;
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
    private ImageView closeWindow;

    @FXML
    private void login(ActionEvent e) throws IOException {
        MapController.loadData();
        String username = loginUser.getText();
        String password = loginPass.getText();
        User user = MapController.getInstance().getConfirmedUser(username, password);

        logInTabPane.setClip(null);

        if (user != null) {
            try {
                MainWindow.showMainWindow();

                // Cierra la ventana de inicio de sesión
                Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                currentStage.close();
            } catch (IOException ex) {
                ex.printStackTrace(); // Muestra el error en la consola
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al abrir la ventana principal");
                alert.setContentText("No se pudo cargar la ventana principal. Verifica la configuración del archivo FXML.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Inicio de Sesión Fallido");
            alert.setHeaderText("Usuario o contraseña incorrectos");
            alert.setContentText("Por favor, verifica tus credenciales.");
            alert.showAndWait();
        }
    }


    @FXML
    private void register(ActionEvent e) {
        String newUsername = regUser.getText();
        String newPassword = regPass.getText();
        String confirmPassword = confirm.getText();

        if (newUsername.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            // Muestra una alerta si algún campo está vacío
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error en el Registro");
            alert.setHeaderText("Campos Vacíos");
            alert.setContentText("Por favor, completa todos los campos antes de continuar.");
            alert.showAndWait();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            // Muestra una alerta si las contraseñas no coinciden
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en el Registro");
            alert.setHeaderText("Contraseñas No Coinciden");
            alert.setContentText("La contraseña y la confirmación deben ser iguales.");
            alert.showAndWait();
            return;
        }

        // Verifica si el usuario ya existe
        if (MapController.getInstance().getUsers().stream()
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(newUsername))) {
            // Muestra una alerta si el nombre de usuario ya está registrado
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en el Registro");
            alert.setHeaderText("Usuario Existente");
            alert.setContentText("El nombre de usuario ya está registrado. Por favor, elige otro.");
            alert.showAndWait();
            return;
        }

        // Intentar agregar el nuevo usuario
        try {
            User newUser = new User(newUsername, newPassword);
            MapController.getInstance().getUsers().add(newUser);
            MapController.saveData();

            // Muestra una alerta de éxito si se creó correctamente
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registro Exitoso");
            alert.setHeaderText("Usuario Creado");
            alert.setContentText("El usuario se ha registrado exitosamente.");
            alert.showAndWait();

            // Limpia los campos del formulario
            regUser.clear();
            regPass.clear();
            confirm.clear();
        } catch (Exception ex) {
            ex.printStackTrace();

            // Muestra una alerta si hubo un error al guardar los datos
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en el Registro");
            alert.setHeaderText("Error Interno");
            alert.setContentText("Ocurrió un error al intentar guardar el usuario. Por favor, intenta de nuevo.");
            alert.showAndWait();
        }
    }


    public void initialize(){
        setupButtonAnimations(logInButton);
        setupButtonAnimations(registerBtn);
    }

    @FXML
    private void handleCloseWindow() {
        // Obtener el Stage a través de cualquier nodo
        Stage stage = (Stage) closeWindow.getScene().getWindow();
        stage.close();
    }

    static void setupSidebarBttnAnimation(Button bt) {
        DropShadow shadow = new DropShadow(16, Color.rgb(7, 146, 189, 0.3));
        shadow.setOffsetY(0);
        bt.setEffect(shadow);

        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2), // Duration of animation
                        new KeyValue(shadow.spreadProperty(), 0.8), // Increase shadow spread
                        new KeyValue(bt.scaleXProperty(), 1.05), // Slightly increase button scale
                        new KeyValue(bt.scaleYProperty(), 1.05)) // Slightly increase button scale
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(shadow.spreadProperty(), 0.52),
                        new KeyValue(bt.scaleXProperty(), 1),
                        new KeyValue(bt.scaleYProperty(), 1))
        );

        bt.setOnMouseEntered(e -> {
            hoverAnimation.playFromStart();
        });

        bt.setOnMouseExited(e -> {
            exitAnimation.playFromStart();
        });
    }

    private void setupButtonAnimations(Button bt){
        DropShadow shadow = new DropShadow(16, Color.rgb(3, 102, 133, 0.2));
        shadow.setOffsetY(0);
        bt.setEffect(shadow);

        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2), // Duration of animation
                        new KeyValue(shadow.radiusProperty(), 20), // Increase shadow radius
                        new KeyValue(shadow.spreadProperty(), 0.4), // Increase shadow spread
                        new KeyValue(bt.scaleXProperty(), 1.05), // Slightly increase button scale
                        new KeyValue(bt.scaleYProperty(), 1.05)) // Slightly increase button scale
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(shadow.radiusProperty(), 16),
                        new KeyValue(shadow.spreadProperty(), 0.26),
                        new KeyValue(bt.scaleXProperty(), 1),
                        new KeyValue(bt.scaleYProperty(), 1))
        );

        bt.setOnMouseEntered(e -> {
            hoverAnimation.playFromStart();

            if (bt == logInButton)
                bt.setStyle("""
                            -fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #a2e4ff 10%, #0eb6f1 75.29%);
                            -fx-effect: dropshadow(gaussian, rgba(109,211,252,0.25), 16, 0.26, 0, 4);
                        """);
            else if (bt == registerBtn)
                bt.setStyle("""
                            -fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #a2e4ff 10%, #0eb6f1 75.29%);
                            -fx-effect: dropshadow(gaussian, rgba(109,211,252,0.25), 16, 0.26, 0, 4);
                        """);


        });

        bt.setOnMouseExited(e -> {
            exitAnimation.playFromStart();
            if (bt == logInButton)
                bt.setStyle("""
                        -fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #07bbff 15%, #22a4d3 75%);""");
            else if (bt == registerBtn)
                bt.setStyle("""
                        -fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #07bbff 15%, #22a4d3 75%);
                        """);
        });
    }
}
