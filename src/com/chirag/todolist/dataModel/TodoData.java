package com.chirag.todolist.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoData {
    private static TodoData instance = new TodoData();
    private static String fileName = "ToDOList.txt";

    private ObservableList<Todoitem> todoItems;
    private DateTimeFormatter formatter;

    public static TodoData getInstance() {
        return instance;
    }

    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
    }

    public ObservableList<Todoitem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(ObservableList<Todoitem> todoItems) {
        this.todoItems = todoItems;
    }

    public void addTodoItem(Todoitem item){
        if (!todoItems.contains(item)) {
            todoItems.add(item);
        } else {
            int index = todoItems.indexOf(item);
            todoItems.remove(item);
            todoItems.add(index, item);
        }
    }

    public void loadTodoItems() throws IOException {
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br = Files.newBufferedReader(path);
        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");
                String shortInfo = itemPieces[0];
                String details = itemPieces[1];
                String dueDate = itemPieces[2];

                LocalDate date = LocalDate.parse(dueDate, formatter);
                Todoitem todoitem = new Todoitem(shortInfo, details, date);
                todoItems.add(todoitem);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public void storeTodoItems() throws IOException {
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {
            Iterator<Todoitem> input = todoItems.iterator();
            while (input.hasNext()) {
                Todoitem item = input.next();
                bw.write(String.format("%s\t%s\t%s", item.getDescription(), item.getDetails(), item.getDate().format(formatter)));

                bw.newLine();
            }
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public void deleteInItem(Todoitem item){
        todoItems.remove(item);
    }
}
