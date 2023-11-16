package table;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import table.bd.Connect;
import table.bd.models.Users;

import table.bd.bd;

import java.util.List;

public class App extends Application {

    ObservableList<Users> tableData = FXCollections.observableArrayList();

    SessionFactory sessionFactory = Connect.getSessionFactory();

    Session session = sessionFactory.openSession();
    
    private boolean edit = false;


    public static void main(String[] args) {
        launch(args);
    }

    List<TableColumn<Users, String>> columns = bd.getColumns(session, Users.class);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Editable JavaFX Table");

        TableView<Users> tableView = new TableView<>();

        tableView.setEditable(false);

        tableView.getColumns().addAll(columns);

        try {

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Query<Users> query = session.createQuery("from Users", Users.class);
            List<Users> resultList = query.getResultList();
            
            tableData =FXCollections.observableArrayList(resultList);

            tableView.setItems(tableData);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        Button editButton = new Button("Изменить");
        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");
        Button saveButton = new Button("Сохранить");
        Button backButton = new Button("Отменить");
        

        addButton.setVisible(false);
        deleteButton.setVisible(false);
        saveButton.setVisible(false);
        backButton.setVisible(false);

        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                edit = !edit;
                tableView.setEditable(edit);
                addButton.setVisible(edit);
                deleteButton.setVisible(edit);
                saveButton.setVisible(edit);
                backButton.setVisible(edit);
            }
        });

        addButton.setOnAction(event -> {

            // try {

            //     Users user = new Users(null, "1", "2",
            //             "3", "4");

            //     Transaction transaction = null;
            //     Session session = sessionFactory.openSession();
            //     try {

            //         transaction = session.beginTransaction();

            //         session.save(user);

            //         transaction.commit();
            //     } catch (Exception e) {
            //         if (transaction != null) {
            //             transaction.rollback();
            //         }
            //         e.printStackTrace();
            //     } finally {
            //         session.close();
            //     }

            // } catch (NumberFormatException e) {
            //     System.out.println("Ошибка: ID должен быть числом.");
            // }
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

}
