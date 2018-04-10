package com.chirag.todolist;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<Todoitem> todoitems;
    @FXML
    private ListView<Todoitem> toDoListView;
    @FXML
    private TextArea textDisplay;

    public void initialize() {
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

        toDoListView.getItems().setAll(todoitems);
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    @FXML
    public void toDoDisplay(){
        Todoitem item = toDoListView.getSelectionModel().getSelectedItem();
        StringBuilder sb = new StringBuilder(item.getDetails());
        sb.append("\n\n\n\n\n");
        sb.append("Due Date: ");
        sb.append(item.getDate().toString());
        textDisplay.setText(sb.toString());
    }
}
