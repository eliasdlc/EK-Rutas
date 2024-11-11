module project.ekrutas {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.visual to javafx.fxml;
    exports project.visual;
}