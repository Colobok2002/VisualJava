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
import org.hibernate.metamodel.spi.MetamodelImplementor;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

                        TableColumn<Users, String> column = new TableCol().createColumn(columnName + " (key)",
                                columnName);

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

    public static class UserDao {
        private EntityManagerFactory emf;
        private EntityManager em;

        public UserDao() {
            emf = Persistence.createEntityManagerFactory("usersPersistenceUnit");
            em = emf.createEntityManager();
        }

        public void insertUser(Users user) {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        }

        public void close() {
            em.close();
            emf.close();
        }
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

}
