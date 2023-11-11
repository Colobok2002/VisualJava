package table.bd;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;

import javafx.scene.control.cell.PropertyValueFactory;
import table.bd.models.Users;

import javafx.scene.control.TableColumn;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import java.util.Map;

public class bd {

    public static List<TableColumn<Users, String>> getColumns(Session session, Class<Users> _class) {
        MetamodelImplementor metamodel = (MetamodelImplementor) session.getSessionFactory().getMetamodel();
        Map<String, EntityPersister> entities = metamodel.entityPersisters();

        for (EntityPersister entityType : entities.values()) {
            String entityName = entityType.getEntityName();
            System.out.println("Entity: " + entityName);

            if (entityType instanceof AbstractEntityPersister) {
                AbstractEntityPersister entityPersister = (AbstractEntityPersister) entityType;

                if (entityPersister.getMappedClass().equals(_class)) {
                    // Found the entity matching the specified class
                    String[] columnNames = entityPersister.getPropertyNames();
                    List<TableColumn<Users, String>> columns = new ArrayList<>();

                    for (String columnName : columnNames) {
                        System.out.println("    Столбец: " + columnName);

                        TableColumn<Users, String> column = new TableColumn<>(columnName);
                        column.setCellValueFactory(new PropertyValueFactory<>(columnName));
                        columns.add(column);
                    }

                    return columns;
                }
            }
        }
        return null;

        // return Collections.emptyList(); 
    }

}
