package ru.dartinc.diskanalyzer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    private Stage stage;
    private Map<String, Long> sizes = new HashMap<>();

    @FXML
    private Label welcomeText;

    @FXML
    private Button chooseButton;
    private PieChart pieChart;

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        stage =(Stage)chooseButton.getScene().getWindow();
        var file = new DirectoryChooser().showDialog(stage);
        if(file!=null) {
            String path = file.getAbsolutePath();
            Analyser analyser = new Analyser();
            sizes = analyser.calculateDirectorySize(Paths.get(path));
            buildChart(path);
        }
        welcomeText.setText(String.valueOf(sizes.keySet().size()));
    }

    private void buildChart(String path) {
        pieChart = new PieChart(pieChartData);

        refillChart(path);

        Button button = new Button(path);
        button.setOnAction( event -> refillChart(path));

        BorderPane pane = new BorderPane();
        pane.setTop(button);
        pane.setCenter(pieChart);

        stage.setScene(new Scene(pane,900,600));
        stage.show();
    }

    private void refillChart(String path) {
        pieChartData.clear();
        pieChartData.addAll(
            sizes
                    .entrySet()
                    .parallelStream()
                    .filter(entry -> {
                        Path parent =Path.of(entry.getKey()).getParent();
                        return parent!=null && path.equals(parent.toString());
                    })
                    .map( entry -> new PieChart.Data(entry.getKey(),entry.getValue().doubleValue()))
                    .toList()
        );
        pieChart.getData().forEach( data -> {
            data
                    .getNode()
                    .addEventHandler(
                            MouseEvent.MOUSE_PRESSED,
                            event -> refillChart(data.getName()));
        });
    }
}