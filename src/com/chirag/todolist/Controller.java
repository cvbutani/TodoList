package com.chirag.todolist;

import com.chirag.todolist.dataModel.TodoData;
import com.chirag.todolist.dataModel.Todoitem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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


    public void initialize() {
/*        todoitems = new ArrayList<>();
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

        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Todoitem>() {
            @Override
            public void changed(ObservableValue<? extends Todoitem> observable, Todoitem oldValue, Todoitem newValue) {
                if (newValue != null) {
                    Todoitem item = toDoListView.getSelectionModel().getSelectedItem();
                    textDisplay.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d,yyyy");
                    deadlineLabel.setText(df.format(item.getDate()));
                }
            }
        });

        toDoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();
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
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dialogController controller = fxmlLoader.getController();
            controller.processResults();                            //  Change line 84 and 87 as listed in comment line 86 for different way of output.
            toDoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
            toDoListView.getSelectionModel().selectLast();          //  Todoitem newItem = controller.processResults(); toDoListView.getSelectionModel().select(newItem);
        } else {
            System.out.println("Cancel Pressed");
        }
    }

    @FXML
    public void toDoDisplay() {
        Todoitem item = toDoListView.getSelectionModel().getSelectedItem();
        textDisplay.setText(item.getDetails());
        deadlineLabel.setText(item.getDate().toString());
    }
}
