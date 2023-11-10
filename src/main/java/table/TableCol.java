package table;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.util.converter.DefaultStringConverter;

public class TableCol {
    TableColumn<Person, String> createColumn(String columnName, String propertyName) {
        TableColumn<Person, String> column = new TableColumn<>(columnName);

        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        column.setOnEditCommit(event -> {
            TablePosition<Person, String> pos = event.getTablePosition();
            String newValue = event.getNewValue();
            Person person = event.getRowValue();
            switch (pos.getColumn()) {
                case 0:
                    person.setId(newValue);
                    break;
                case 1:
                    person.setFirstName(newValue);
                    break;
                case 2:
                    person.setLastName(newValue);
                    break;
                case 3:
                    person.setPhone(newValue);
                    break;
                case 4:
                    person.setEmail(newValue);
                    break;
            }
        });
        
        return column;
    }

    // private void setCellFactory(TableColumn<Person, String> column) {
    // column.setCellFactory(TextFieldTableCell.forTableColumn());
    // column.setOnEditCommit(event -> {
    // TableCell<Person, String> cell = column.getCellFactory().call(column);
    // cell.commitEdit(event.getNewValue());
    // });
    // }

}
