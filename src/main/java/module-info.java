module project.ekrutas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens project.logic to javafx.base;
    opens project.visual to javafx.fxml;
    exports project.visual;
}