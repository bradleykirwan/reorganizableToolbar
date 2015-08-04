import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

public class Sample extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ToolBar toolbar = new ReorganizableToolBar();
        toolbar.getItems().addAll(new Button("Button 1"), new Button("Button 2"), new ComboBox<String>());

        primaryStage.setTitle("Reorganizable ToolBar");
        primaryStage.setScene(new Scene(toolbar, 600, 50));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
