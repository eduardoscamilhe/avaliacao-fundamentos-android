package com.example.administrador.myapplication.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrador.myapplication.util.AppUtil;

/**
 * Created by Administrador on 28/05/2015.
 */
public final class UserRepository {

    private static class Singleton {
        public static final UserRepository INSTANCE = new UserRepository();
    }

    private UserRepository() {
        super();
    }
    public static UserRepository getInstance() {
        return Singleton.INSTANCE;
    }
    public boolean getLogin(String login, String pass) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getReadableDatabase();
        String where = UserContract.LOGIN + " = ? AND "+UserContract.PASSWORD +" = ?";
        String[] args = {login,pass};
        Cursor cursor = db.query(UserContract.TABLE, UserContract.COLUMNS, where, args, null, null, null);
        Integer teste = cursor.getCount();
        db.close();
        helper.close();
        boolean retorno = teste > 0;
        return retorno ;
    }
}
