package table;

import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
// import javafx.util.Callback;


public class App extends Application {

    private ObservableList<Person> tableData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Editable JavaFX Table");

        TableView<Person> tableView = new TableView<>();
        tableView.setEditable(true);

        TableColumn<Person, String> idColumn = new TableCol().createColumn("ID", "id");
        TableColumn<Person, String> firstNameColumn =  new TableCol().createColumn("Имя", "firstName");
        TableColumn<Person, String> lastNameColumn =  new TableCol().createColumn("Фамилия", "lastName");
        TableColumn<Person, String> phoneColumn =  new TableCol().createColumn("Телефон", "phone");
        TableColumn<Person, String> emailColumn =  new TableCol().createColumn("Почта", "email");

        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, phoneColumn, emailColumn);

        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");

        addButton.setOnAction(event -> {
            try {
                int nextId = tableData.isEmpty() ? 0
                        : tableData.stream()
                                .mapToInt(person -> Integer.parseInt(person.getId()))
                                .max()
                                .orElse(0) + 1;
                tableData.add(new Person(Integer.toString(nextId), "", "", "", ""));
                tableView.setItems(tableData);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть числом.");
            }
        });

        deleteButton.setOnAction(event -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                tableData.remove(selectedIndex);
            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(
                tableView,
                new HBox(10, addButton, deleteButton));

        Scene scene = new Scene(vbox, 600, 400);

        primaryStage.setScene(scene);

        primaryStage.show();
    }


}
