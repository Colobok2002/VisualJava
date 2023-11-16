package table;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import table.bd.Connect;
import table.bd.models.Users;

import table.bd.bd;

import java.util.List;

public class App extends Application {

    private ObservableList<Users> tableData = FXCollections.observableArrayList();
    // private ObservableList<Person> tableData_change =
    // FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    SessionFactory sessionFactory = Connect.getSessionFactory();

    Session session = sessionFactory.openSession();

    List<TableColumn<Users, String>> columns = bd.getColumns(session, Users.class);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Editable JavaFX Table");

        TableView<Users> tableView = new TableView<>();
        tableView.setEditable(false);

        // Fetch data from the database
        // tableData.addAll(getAllPeopleFromDatabase());

        tableView.setItems(tableData);

        // List<TableColumn<Person, String>> columns =
        // bd.getTableColumnsFromModel(connect, Person.class);

        // Add the generated columns to the table
        // tableView.getColumns().addAll(columns);

        tableView.getColumns().addAll(columns);

        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");
        Button editButton = new Button("Изменить");
        Button saveButton = new Button("Сохранить");
        Button backButton = new Button("Отменить");

        addButton.setOnAction(event -> {
            try {
                // BigInteger nextId = tableData.isEmpty() ? BigInteger.ZERO
                // : tableData.stream()
                // .map(Users::getId)
                // .max(Comparator.naturalOrder())
                // .orElse(BigInteger.ZERO)
                // .add(BigInteger.ONE);

                // // savePersonToDatabase(new Users(nextId, "", "", "", ""));

                // tableData.add(new Users(nextId, "", "", "", ""));
                // tableView.setItems(tableData);
                Users user = new Users(null, "1", "2",
                        "3", "4");

                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();

                    // сохранение объекта user
                    session.save(user);

                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    e.printStackTrace();
                } finally {
                    session.close();
                }

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
    // private ObservableList<Users> getAllPeopleFromDatabase() {
    // try (Session session = sessionFactory.openSession()) {
    // return FXCollections.observableArrayList(session.createQuery("FROM Person",
    // Users.class).list());
    // }
    // }

    // // Save a person to the database
    // private void savePersonToDatabase(Users person) {
    // try (Session session = sessionFactory.openSession()) {
    // session.beginTransaction();
    // session.save(person);
    // session.getTransaction().commit();
    // }
    // }

}
