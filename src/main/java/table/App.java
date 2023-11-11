package table;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
// import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import table.bd.Connect;
import table.bd.models.Person;

import table.bd.bd;

import java.math.BigInteger;
// import java.sql.ResultSetMetaData;
// import java.sql.SQLException;
// import java.util.ArrayList;
import java.util.Comparator;

import java.util.List;

public class App extends Application {

    private ObservableList<Person> tableData = FXCollections.observableArrayList();
    // private ObservableList<Person> tableData_change = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    SessionFactory sessionFactory = Connect.getSessionFactory();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Editable JavaFX Table");

        TableView<Person> tableView = new TableView<>();
        tableView.setEditable(false);

        // Fetch data from the database
        tableData.addAll(getAllPeopleFromDatabase());

        tableView.setItems(tableData);

        List<TableColumn<Person, String>> columns = bd.getTableColumnsFromModel(Person.class);

        // Add the generated columns to the table
        tableView.getColumns().addAll(columns);

        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");
        Button editButton = new Button("Изменить");
        Button saveButton = new Button("Сохранить");
        Button backButton = new Button("Отменить");

        addButton.setOnAction(event -> {
            try {
                BigInteger nextId = tableData.isEmpty() ? BigInteger.ZERO
                        : tableData.stream()
                                .map(Person::getId)
                                .max(Comparator.naturalOrder())
                                .orElse(BigInteger.ZERO)
                                .add(BigInteger.ONE);

                savePersonToDatabase(new Person(nextId, "", "", "", ""));

                tableData.add(new Person(nextId, "", "", "", ""));
                tableView.setItems(tableData);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть числом.");
            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(
                tableView,
                new HBox(10, editButton),
                new HBox(10, addButton, deleteButton),
                new HBox(10, saveButton, backButton));

        Scene scene = new Scene(vbox, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fetch all people from the database
    private ObservableList<Person> getAllPeopleFromDatabase() {
        try (Session session = sessionFactory.openSession()) {
            return FXCollections.observableArrayList(session.createQuery("FROM Person", Person.class).list());
        }
    }

    // Save a person to the database
    private void savePersonToDatabase(Person person) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(person);
            session.getTransaction().commit();
        }
    }

}
