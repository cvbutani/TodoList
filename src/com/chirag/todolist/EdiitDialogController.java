package com.chirag.todolist;

import com.chirag.todolist.dataModel.TodoData;
import com.chirag.todolist.dataModel.Todoitem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class EdiitDialogController {

    @FXML
    private TextField namePicker;

    @FXML
    private TextArea detailPicker;

    @FXML
    private DatePicker datePicker;

    private Todoitem selected;


    public Todoitem processResults() {
        String shortDescription = namePicker.getText().trim();
        String details = detailPicker.getText().trim();
        LocalDate deadlineValue = datePicker.getValue();

        selected.setDescription(shortDescription);
        selected.setDetails(details);
        selected.setDate(deadlineValue);
        TodoData.getInstance().addTodoItem(selected);
        return selected;
    }

    public void showTodoBeingEdited(Todoitem selected) {
        this.selected = selected;
        namePicker.setText(selected.getDescription());
        detailPicker.setText(selected.getDetails());
        datePicker.setValue(selected.getDate());
    }

}