package table;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import table.bd.models.Users;
import java.lang.reflect.Method;


public class TableCol {
    public TableColumn<Users, String> createColumn(String columnName, String propertyName) {

        TableColumn<Users, String> column = new TableColumn<>(columnName);

        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));

        column.setCellFactory(c -> {
            // Создаем экземпляр TextFieldTableCell для настройки ячеек как редактируемых.
            TextFieldTableCell<Users, String> cell = new TextFieldTableCell<>(new DefaultStringConverter()) {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    getTableRow().getStyleClass().remove("new-row");
                    if (empty) {
                        setText(null);
                        setStyle("");

                    } else {
                        setText(item);
                        getTableRow().getStyleClass().add("non-new-row");

                        TableView<?> tableView = getTableView();
                        if (tableView != null && getIndex() < tableView.getItems().size()) {
                            Users person = (Users) tableView.getItems().get(getIndex());
                            String firstCellItem = person.getId();
                            if (firstCellItem == null || firstCellItem.toString().isEmpty()) {
                                getTableRow().getStyleClass().remove("non-new-row");
                                getTableRow().getStyleClass().add("new-row");
                            } else {
                                getTableRow().getStyleClass().remove("new-row");
                                getTableRow().getStyleClass().add("non-new-row");
                            }
                        }
                    }
                }

            };

            return cell;
        });


        column.setOnEditCommit(event -> {
            TablePosition<Users, String> pos = event.getTablePosition();
            String newValue = event.getNewValue();
            Users person = event.getRowValue();
            System.out.println(pos.getColumn());
            // switch (pos.getColumn()) {
            // case 1:
            // person.setEmail(newValue);
            // break;
            // case 2:
            // person.setFirstName(newValue);
            // break;
            // case 3:
            // person.setLastName(newValue);
            // break;
            // case 4:
            // person.setPhone(newValue);
            // break;
            // }

            try {
                String _columnName = pos.getTableColumn().getText();
                System.out.println(_columnName);
                Method method = Users.class.getMethod("set" + columnName, String.class);
                method.invoke(person, newValue);
            } catch (Exception e) {
                e.printStackTrace(); // Обработка исключений, если не удалось вызвать метод
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
