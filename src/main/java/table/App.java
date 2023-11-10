package table;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import table.Person;

public class App extends Application {

    private Map<Integer, Person> data = new HashMap<>();
    private ObservableList<Person> tableData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("JavaFX Data Manipulation");

        // Создаем TableView для отображения данных
        TableView<Person> tableView = new TableView<>();
        tableView.setEditable(false);

        // Создаем столбцы
        TableColumn<Person, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Person, String> firstNameColumn = new TableColumn<>("Имя");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Person, String> phoneColumn = new TableColumn<>("Телефон");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Person, String> emailColumn = new TableColumn<>("Почта");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Добавляем столбцы в TableView
        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, phoneColumn, emailColumn);


        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Телефон");

        TextField emailField = new TextField();
        emailField.setPromptText("Почта");

        // Кнопки для добавления и удаления данных
        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");

        addButton.setOnAction(event -> {
            try {

                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();

                Person person = new Person(data.size(), firstName, lastName, phone, email);
                data.put(data.size(), person);
                tableData.add(person);

                firstNameField.clear();
                lastNameField.clear();
                phoneField.clear();
                emailField.clear();
                tableView.setItems(tableData);

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть числом.");
            }
        });

        deleteButton.setOnAction(event -> {
            int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
                data.remove(selectedPerson.getId());
                tableData.remove(selectedPerson);
            }
        });

        // Размещаем элементы в компоновке VBox
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(
                tableView,
                new HBox(10, firstNameField, lastNameField, phoneField, emailField),
                new HBox(10, addButton, deleteButton)
        );

        // Создаем сцену
        Scene scene = new Scene(vbox, 600, 400);

        primaryStage.setScene(scene);

        // Показываем primaryStage
        primaryStage.show();
    }

   
}
