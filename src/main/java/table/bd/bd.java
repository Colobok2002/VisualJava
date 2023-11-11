package table.bd;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.Column; // Import the Column annotation
import table.bd.models.Person;

public class bd {
    // Dynamically fetch column names from the model class
    public static List<TableColumn<Person, String>> getTableColumnsFromModel(Class<?> modelClass) {
        List<TableColumn<Person, String>> columns = new ArrayList<>();

        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                // Assuming your model has proper annotations like @Column
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null) {
                    TableColumn<Person, String> column = new TableColumn<>(columnAnnotation.name());
                    column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
                    columns.add(column);
                }
            }
        }

        return columns;
    }
}
