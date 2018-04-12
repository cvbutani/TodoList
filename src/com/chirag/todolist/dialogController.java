package com.chirag.todolist;

import com.chirag.todolist.dataModel.TodoData;
import com.chirag.todolist.dataModel.Todoitem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class dialogController {

    @FXML
    private TextField namePicker;

    @FXML
    private TextArea detailPicker;

    @FXML
    private DatePicker datePicker;

    public Todoitem processResults() {
        String shortDescription = namePicker.getText().trim();
        String details = detailPicker.getText().trim();
        LocalDate deadlineValue = datePicker.getValue();
        Todoitem newItem = new Todoitem(shortDescription, details, deadlineValue);
        TodoData.getInstance().addTodoItem(newItem);
        return newItem;
    }

}

