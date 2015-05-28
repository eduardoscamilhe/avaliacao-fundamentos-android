package com.example.administrador.myapplication.models.persistence;

/**
 * Created by Administrador on 28/05/2015.
 */
public class UserContract {
    public static final String TABLE = "users";
    public static final String ID = "id";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "senha";

    public static final String[] COLUMNS = {ID, LOGIN, PASSWORD};

    public static String createTable() {
        final StringBuilder sql = new StringBuilder();
        sql.append(" CREATE TABLE " + TABLE);
        sql.append(" ( " + ID + " INTEGER PRIMARY KEY, ");
        sql.append(LOGIN + " TEXT, ");
        sql.append(PASSWORD + " TEXT );");
        return sql.toString();
    }
    public static String adminUser(){
        final StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO  "+TABLE);
        sql.append(" VALUES (1,'admin','admin')");
        return sql.toString();
    }

}
