package com.example.dbadapterwithcontentprovider.SQLMagic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoItem {

  String task;
  Date created;
  int id;
  
  public String getTask() {
    return task;
  }
  
  public Date getCreated() {
    return created;    
  }

  public int getId() { return id; }

  public void setId(int id) { this.id = id; }

  public ToDoItem(String _task) {
    this(_task, new Date(java.lang.System.currentTimeMillis()));
  }
  
  public ToDoItem(String _task, Date _created) {
    task = _task;
    created = _created;
  }

  @Override
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    String dateString = sdf.format(created);  
    return "(" + dateString +  ") " + task; 
  }
}