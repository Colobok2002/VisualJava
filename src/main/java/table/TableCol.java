package table;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.util.converter.DefaultStringConverter;
import table.bd.models.Users;

public class TableCol {
    public TableColumn<Users, String> createColumn(String columnName, String propertyName) {
        
        TableColumn<Users, String> column = new TableColumn<>(columnName);

        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        column.setOnEditCommit(event -> {
            TablePosition<Users, String> pos = event.getTablePosition();
            String newValue = event.getNewValue();
            Users person = event.getRowValue();
            switch (pos.getColumn()) {
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
