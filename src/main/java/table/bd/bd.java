package table.bd;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.query.Query;

import table.bd.models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import org.hibernate.metamodel.spi.MetamodelImplementor;
import java.util.Map;

import table.TableCol;

public class bd {

    public static List<TableColumn<Users, String>> getColumns(SessionFactory sessionFactory, Class<Users> _class) {

        Session session = sessionFactory.openSession();
        MetamodelImplementor metamodel = (MetamodelImplementor) session.getSessionFactory().getMetamodel();
        Map<String, EntityPersister> entities = metamodel.entityPersisters();

        for (EntityPersister entityType : entities.values()) {
            String entityName = entityType.getEntityName();
            System.out.println("Entity: " + entityName);

            if (entityType instanceof AbstractEntityPersister) {
                AbstractEntityPersister entityPersister = (AbstractEntityPersister) entityType;

                if (entityPersister.getMappedClass().equals(_class)) {

                    List<TableColumn<Users, String>> columns = new ArrayList<>();

                    String[] columKey = entityPersister.getKeyColumnNames();
                    for (String columnName : columKey) {
                        System.out.println("    Внешний ключь: " + columnName);

                        TableColumn<Users, String> column = new TableColumn<>(columnName + " (key)");
                        column.setCellValueFactory(new PropertyValueFactory<>(columnName));

                        columns.add(column);
                    }
                    String[] columnNames = entityPersister.getPropertyNames();

                    for (String columnName : columnNames) {
                        System.out.println("    Столбец: " + columnName);

                        TableColumn<Users, String> column = new TableCol().createColumn(columnName, columnName);

                        columns.add(column);
                    }

                    return columns;
                }
            }
        }
        return null;
    }

    public static ObservableList<Users> getValues(SessionFactory sessionFactory, Class<Users> _class) {
        Session session = sessionFactory.openSession();
        ObservableList<Users> tableData = null;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query<Users> query = session.createQuery("from Users", _class);
            List<Users> resultList = query.getResultList();
            tableData = FXCollections.observableArrayList(resultList);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return tableData;
    }

    public static void deleteUsersByIds(Session session, List<String> idsArray) {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Перебираем массив идентификаторов и удаляем записи
            for (String id : idsArray) {
                Users user = session.get(Users.class, id);
                if (user != null) {
                    session.delete(user);
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}
