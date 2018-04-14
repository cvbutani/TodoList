package com.chirag.todolist;

import com.chirag.todolist.dataModel.TodoData;
import com.chirag.todolist.dataModel.Todoitem;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    private List<Todoitem> todoitems;
    @FXML
    private ListView<Todoitem> toDoListView;
    @FXML
    private TextArea textDisplay;
    @FXML
    private Label deadlineLabel;
    @FXML
    private BorderPane mainTodoList;
    @FXML
    private ContextMenu listMenu;
    @FXML
    private ToggleButton filterButton;
    @FXML
    private FilteredList<Todoitem> filteredList;


    public void initialize() {
/*
        todoitems = new ArrayList<>();
       Todoitem item1 = new Todoitem("Birthday Card", "Buy a birthday card from dollarama and send it to Sagar.", LocalDate.of(2018, Month.JULY, 12));
        todoitems.add(item1);
        Todoitem item2 = new Todoitem("Call Skip", "Call Skip the dishes and quit from skip network.", LocalDate.of(2018, Month.APRIL, 12));
        todoitems.add(item2);
        Todoitem item3 = new Todoitem("Cancel Gym membership", "Cancel your gym membership at end of this month", LocalDate.of(2018, Month.APRIL, 30));
        todoitems.add(item3);
        Todoitem item4 = new Todoitem("Car Insurance", "Pay car insurance on 15th of every month.", LocalDate.of(2018, Month.MAY, 15));
        todoitems.add(item4);
        Todoitem item5 = new Todoitem("Mobile Bill", "Pay mobile bill which is $41 and is due on 10th May.", LocalDate.of(2018, Month.MAY, 10));
        todoitems.add(item5);
        Todoitem item6 = new Todoitem("Book USA Wichita Trip", "Book ticket to visit CTDI facility in Wichita, Kansas.", LocalDate.of(2018, Month.APRIL, 15));
        todoitems.add(item6);

        TodoData.getInstance().setTodoItems(todoitems);
*/

        listMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem edititem = new MenuItem("Edit");
        MenuItem newitem = new MenuItem("New");

        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Todoitem item = toDoListView.getSelectionModel().getSelectedItem();
                deleteListedItem(item);
            }
        });

        edititem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showEditItemDialog();
            }
        });

        newitem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showNewItemDialog();
            }
        });
        listMenu.getItems().addAll(newitem, edititem, deleteItem);

        toDoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Todoitem item = toDoListView.getSelectionModel().getSelectedItem();
                textDisplay.setText(item.getDetails());
                DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d,yyyy");
                deadlineLabel.setText(df.format(item.getDate()));
            }
        });
        filteredList = new FilteredList<Todoitem>(TodoData.getInstance().getTodoItems(), new Predicate<Todoitem>() {
            @Override
            public boolean test(Todoitem todoitem) {
                return true;
            }
        });

        SortedList<Todoitem> sortedList = new SortedList<Todoitem>(filteredList, new Comparator<Todoitem>() {
            @Override
            public int compare(Todoitem o1, Todoitem o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });


//        toDoListView.setItems(TodoData.getInstance().getTodoItems());
        toDoListView.setItems(sortedList);
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();

        toDoListView.setCellFactory(new Callback<ListView<Todoitem>, ListCell<Todoitem>>() {
                                        @Override
                                        public ListCell<Todoitem> call(ListView<Todoitem> param) {
                                            ListCell<Todoitem> cell = new ListCell<Todoitem>() {
                                                @Override
                                                protected void updateItem(Todoitem item, boolean empty) {
                                                    super.updateItem(item, empty);
                                                    if (empty) {
                                                        setText(null);
                                                    } else {
                                                        setText(item.getDescription());
                                                        if (item.getDate().equals(LocalDate.now())) {
                                                            setTextFill(Color.GREEN);
                                                        } else if (item.getDate().equals(LocalDate.now().plusDays(1))) {
                                                            setTextFill(Color.YELLOW);
                                                        } else if (item.getDate().isBefore(LocalDate.now().plusDays(1))) {
                                                            setTextFill(Color.RED);
                                                        }
                                                    }
                                                }
                                            };
                                            cell.emptyProperty().addListener(
                                                    (obs, wasEmpty, isNowEmpty) -> {
                                                        if (isNowEmpty) {
                                                            cell.setContextMenu(null);
                                                        } else {
                                                            cell.setContextMenu(listMenu);
                                                        }
                                                    });
                                            return cell;
                                        }
                                    }
        );
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainTodoList.getScene().getWindow());
        dialog.setTitle("Add new item in TodoList");
        dialog.setHeaderText("You are updating your todolist.");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogList.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't Load Dialog.");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dialogController controller = fxmlLoader.getController();
            controller.processResults();
            toDoListView.getSelectionModel().selectLast();          //  Todoitem newItem = controller.processResults(); toDoListView.getSelectionModel().select(newItem);
        }
    }

    @FXML
    public void showEditItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainTodoList.getScene().getWindow());
        dialog.setTitle("Edit Todo Item");
        dialog.setHeaderText("Use this dialog to edit the todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editTodoList.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the edit dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Todoitem selected = toDoListView.getSelectionModel().getSelectedItem();
        EdiitDialogController controller = fxmlLoader.getController();
        controller.showTodoBeingEdited(selected);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            Todoitem editedItem = controller.processResults();
            toDoListView.getSelectionModel().clearSelection();
            toDoListView.getSelectionModel().select(editedItem);
        }
    }

    public void deleteListedItem(Todoitem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete TodoList Item");
        alert.setHeaderText("Deleting " + item.getDescription());
        alert.setContentText("Are you sure?     Press OK to delete.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            TodoData.getInstance().deleteInItem(item);
        }
    }

    @FXML
    public void keyPressed(KeyEvent keyEvent) {
        Todoitem selectedItem = toDoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteListedItem(selectedItem);
            }
        }
    }

    @FXML
    public void toDoDisplay() {
        Todoitem item = toDoListView.getSelectionModel().getSelectedItem();
        textDisplay.setText(item.getDetails());
        deadlineLabel.setText(item.getDate().toString());
    }

    @FXML
    public void filterPressButton() {
        if (filterButton.isSelected()) {
            filteredList.setPredicate(new Predicate<Todoitem>() {
                @Override
                public boolean test(Todoitem todoitem) {
                    return (todoitem.getDate().equals(LocalDate.now()));
                }
            });
        } else {
            filteredList.setPredicate(new Predicate<Todoitem>() {
                @Override
                public boolean test(Todoitem todoitem) {
                    return true;
                }
            });
        }
    }

    @FXML
    public void handleExit(){
        Platform.exit();
    }
}
