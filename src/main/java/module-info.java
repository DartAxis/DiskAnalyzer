module ru.dartinc.diskanalyzer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens ru.dartinc.diskanalyzer to javafx.fxml;
    exports ru.dartinc.diskanalyzer;
}