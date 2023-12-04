package table;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
import java.util.ArrayList;

import table.bd.bd;

import java.util.List;

public class App extends Application {

    ObservableList<Users> tableData = FXCollections.observableArrayList();

    List<String> idsToDelete = new ArrayList<>();

    SessionFactory sessionFactory = Connect.getSessionFactory();
    Users[] existingUsers;

    private boolean edit = false;

    public static void main(String[] args) {
        launch(args);
    }

    List<TableColumn<Users, String>> columns = bd.getColumns(sessionFactory, Users.class);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Editable JavaFX Table");

        TableView<Users> tableView = new TableView<>();

        tableView.setEditable(false);

        tableView.getColumns().addAll(columns);

        tableData = bd.getValues(sessionFactory, Users.class);

        tableView.setItems(tableData);

        Button editButton = new Button("Изменить");
        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");
        Button saveButton = new Button("Сохранить");
        Button backButton = new Button("Отменить");
        Button InfoButton = new Button("?");

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

            // Users user = new Users(null, "1", "2",
            // "3", "4");

            // Transaction transaction = null;
            // Session session = sessionFactory.openSession();
            // try {

            // transaction = session.beginTransaction();

            // session.save(user);

            // transaction.commit();
            // } catch (Exception e) {
            // if (transaction != null) {
            // transaction.rollback();
            // }
            // e.printStackTrace();
            // } finally {
            // session.close();
            // }

            // } catch (NumberFormatException e) {
            // System.out.println("Ошибка: ID должен быть числом.");
            // }
            tableData.add(new Users());
            tableView.setItems(tableData);
        });

        deleteButton.setOnAction(event -> {
            Users selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                tableData.remove(selectedUser);

            } else {
                System.out.println("No User selected for deletion.");
            }
        });

        backButton.setOnAction(event -> {
            tableData = bd.getValues(sessionFactory, Users.class);
            tableView.setItems(tableData);
        });

        saveButton.setOnAction(event -> {
            Session session = sessionFactory.openSession();

            bd.deleteUsersByIds(session, idsToDelete);

            List<Users> existingUsers = session.createQuery("from Users", Users.class).getResultList();

            for (Users user : tableData) {
                Transaction transaction = null;

                try {
                    transaction = session.beginTransaction();

                    if (user.getId() != null && user.getId() != 0) {

                        Users existUser = session.get(Users.class, user.getId());
                        System.out.println(existUser);

                        if (existUser != null) {
                            existUser.setid(user.getId());
                            existUser.setfirstName(user.getFirstName());
                            existUser.setlastName(user.getLastName());
                            existUser.setphone(user.getPhone());
                            existUser.setemail(user.getEmail());
                        }

                    } else {
                        System.out.println("1");
                        session.save(user);
                    }
                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    e.printStackTrace();
                }
            }
            for (Users user : existingUsers) {
                boolean containsUser = tableData.stream().anyMatch(u -> u.getId().equals(user.getId()));

                if (!containsUser) {
                    Transaction transaction = null;
                    try {
                        transaction = session.beginTransaction();
                        session.delete(user);
                        transaction.commit();
                    } catch (Exception ex) {
                        if (transaction != null)
                            transaction.rollback();
                        ex.printStackTrace();
                    }
                }
            }

            session.close();
            tableData = bd.getValues(sessionFactory, Users.class);
            tableView.setItems(tableData);
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(
                tableView,
                new HBox(10, editButton),
                new HBox(10, addButton, deleteButton),
                new HBox(10, saveButton, backButton),
                new HBox(10, InfoButton));

        Scene scene = new Scene(vbox, 600, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        InfoButton.setOnAction(event -> {

            VBox InfoVBox = new VBox(10);
            InfoVBox.setPadding(new Insets(10, 10, 10, 10));

            Button backButton2 = new Button("Назад");
            backButton2.setOnAction(e -> {
                primaryStage.setScene(scene);
            });

            Hyperlink hyperlink = new Hyperlink("Сайт разработчика");

            hyperlink.setOnAction(e -> {
                getHostServices().showDocument("https://github.com/Colobok2002");
            });

            // https://github.com/Colobok2002
            InfoVBox.getChildren().addAll(
                    new Label("Info"), hyperlink, backButton2); 

            Scene InfoScene = new Scene(InfoVBox, 400, 300);
            primaryStage.setScene(InfoScene);
        });
    }

}
