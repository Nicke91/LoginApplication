package com.example.nicke.loginapplication.SQLDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nicke.loginapplication.models.Notes;
import com.example.nicke.loginapplication.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicke on 7/20/2017.
 */

public class SQLDatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserAcount.db";
    private static final String TABLE_USERS = "users";

    private static final String USERS_ID = "id";
    private static final String USERS_NAME = "name";
    private static final String USERS_EMAIL = "email";
    private static final String USERS_USERNAME = "user_name";
    private static final String USERS_PASSWORD = "password";

    private static final String TABLE_NOTES = "notes";
    private static final String NOTES_ID = "id1";
    private static final String NOTES_TITLE = "title";
    private static final String NOTES_DATE = "date";
    private static final String NOTES_CONTENT = "content";
    private static final String NOTES_USER = "notes_user";


    SQLiteDatabase db;


    public SQLDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String usersQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(id integer PRIMARY KEY autoincrement, name VARCHAR, email VARCHAR, user_name VARCHAR, password VARCHAR);";
        String notesQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "(id1 integer PRIMARY KEY autoincrement, title VARCHAR, date VARCHAR, content VARCHAR, notes_user VARCHAR);";

        db.execSQL(usersQuery);
        db.execSQL(notesQuery);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP_TABLE_IF_EXISTS " + TABLE_USERS);
        db.execSQL("DROP_TABLE_IF_EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    //USER
    public void addUser(User user) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_NAME, user.getName());
        contentValues.put(USERS_EMAIL, user.getEmail());
        contentValues.put(USERS_USERNAME, user.getUserName());
        contentValues.put(USERS_PASSWORD, user.getPassword());

        db.insert(TABLE_USERS, null, contentValues);

    }

    public void deleteUser(String userName) {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE TRIM(user_name)= '" + userName.trim() +"'" + ";");
        db.execSQL("DELETE FROM " + TABLE_NOTES + " WHERE TRIM(notes_user)= '" + userName.trim() + "'" + ";");
    }


    public Notes addNote(String title, String date, String content,String userName) {
        Notes returnedNote;
        int id;

        db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("content", content);
        contentValues.put("notes_user", userName);

        db.insert(TABLE_NOTES, null, contentValues);

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NOTES, null);

        int getId = cursor.getColumnIndex("id1");

        cursor.moveToFirst();

        if(cursor != null && cursor.getCount() > 0) {
            do {
                id = cursor.getInt(getId);
            } while (cursor.moveToNext());

        } else {
            id = 0;
        }

        String getTitle = contentValues.get("title").toString();
        String getDate = contentValues.get("date").toString();
        String getContent = contentValues.get("content").toString();

        returnedNote = new Notes(id, getTitle, getDate, getContent, userName);

        return returnedNote;
    }


    public void deleteNote(int id) {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES + " WHERE id1=" + id + ";");
    }


    public void changeNote(int id, String title, String date, String content) {

        db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("content", content);

        db.update(TABLE_NOTES, contentValues, "id1= "+ id, null);

    }

    public Notes getNote(int id) {
        Notes note;

        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NOTES + " WHERE id1= " + id, null);

        int getTitle = cursor.getColumnIndex("title");
        int getDate = cursor.getColumnIndex("date");
        int getContent = cursor.getColumnIndex("content");
        int getUserName = cursor.getColumnIndex("notes_user");

        cursor.moveToFirst();

        if(cursor != null && cursor.getCount() > 0) {

            do {
                String title = cursor.getString(getTitle);
                String date = cursor.getString(getDate);
                String content = cursor.getString(getContent);
                String userName = cursor.getString(getUserName);

                note = new Notes(id, title, date, content, userName);

            } while (cursor.moveToNext());

        }else {
            note = null;
        }
        return note;
    }

    public String searchUserName(String userName) {
        String checkedUserName = "";

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT user_name FROM " + TABLE_USERS, null);
        int getUserName = cursor.getColumnIndex("user_name");

        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {
                String user_name = cursor.getString(getUserName);

                checkedUserName = user_name;

                if(checkedUserName.equals(userName)) {
                    break;
                }
            }while (cursor.moveToNext());
        } else {
            checkedUserName = "not found";
        }

        return checkedUserName;
    }

    public String searchPassword(String userName, String password) {
        String checkedPassword;

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT user_name, password FROM " + TABLE_USERS, null);
        int getPassword = cursor.getColumnIndex("password");
        int getUserName = cursor.getColumnIndex("user_name");
        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {
                String passwrd = cursor.getString(getPassword);
                String user_name = cursor.getString((getUserName));

                if (user_name.equals(userName) && password.equals(passwrd)) {

                checkedPassword = passwrd;
                    break;
                } else {
                    checkedPassword = "incorrect";
                }
            }while (cursor.moveToNext());
        } else {
            checkedPassword = "not found";
        }

        return checkedPassword;
    }

    public String searchEmail(String userName, String email) {
        String checkedEmail = "";

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT user_name, email FROM " + TABLE_USERS, null);
        int getEmail = cursor.getColumnIndex("email");
        int getUserName = cursor.getColumnIndex("user_name");
        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {

                String user_name = cursor.getString(getUserName);
                String emailAddress = cursor.getString(getEmail);


                if (user_name.equals(userName) && emailAddress.equals(email)) {

                    checkedEmail = emailAddress;
                    break;
                }
            }while (cursor.moveToNext());
        } else {
            checkedEmail = "not found";
        }

        return checkedEmail;
    }

    public String returnPassword(String userName, String email) {
        String returnedPassword = "";

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT user_name, email, password FROM " + TABLE_USERS, null);
        int getEmail = cursor.getColumnIndex("email");
        int getUserName = cursor.getColumnIndex("user_name");
        int getPassword = cursor.getColumnIndex("password");
        cursor.moveToFirst();

        if(cursor != null && (cursor.getCount() > 0)) {
            do {

                String user_name = cursor.getString(getUserName);
                String emailAddress = cursor.getString(getEmail);
                String passwrd = cursor.getString(getPassword);

                if (user_name.equals(userName) && emailAddress.equals(email)) {

                    returnedPassword = passwrd;
                    break;
                }
            }while (cursor.moveToNext());
        } else {
            returnedPassword = "not found";
        }

        return returnedPassword;
    }

    public String checkIfExists(String userName, String password, boolean checkForPassword) {
        String doesExist = "";


        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT user_name, password FROM " + TABLE_USERS, null);
        int getUserName = cursor.getColumnIndex("user_name");
        int getPassword = cursor.getColumnIndex("password");

        cursor.moveToFirst();

        if(cursor != null && cursor.getCount() > 0) {
            do {
                String user_name = cursor.getString(getUserName);
                String passwrd = cursor.getString(getPassword);

                if (checkForPassword && passwrd.trim().equals(password)) {
                    return doesExist = "password";
                } else {

                    if (user_name.trim().equals(userName)) {
                        return doesExist = "userName";
                    } else if (passwrd.trim().equals(password)) {
                        return doesExist = "password";

                    }
                }

            }while (cursor.moveToNext());
        }

        return doesExist;
    }


        public String getIdAndUserName() {
            String itemText = "";

            db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT id, user_name, password FROM " + TABLE_USERS, null);
            int getUserName = cursor.getColumnIndex("user_name");
            int getId = cursor.getColumnIndex("id");
            int getPassword = cursor.getColumnIndex("password");

            cursor.moveToFirst();

            if(cursor != null && cursor.getCount() > 0) {
                do {
                    String user_name = cursor.getString(getUserName);
                    String id = cursor.getString(getId);
                    String password = cursor.getString(getPassword);

                    itemText = itemText + "ID: " + id + "\n" + "Username: " + user_name + "\n" + "Password: " + password + "\n\n";



                }while (cursor.moveToNext());
            } else {
                itemText = "No users in database";
            }

            return itemText;
        }

        public ArrayList getAll(String userNamedata) {
            ArrayList<String> dataToReturn = new ArrayList<>();

            db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE TRIM(user_name)= '" + userNamedata.trim() +"'" , null);
            int getName = cursor.getColumnIndex("name");
            int getEmail = cursor.getColumnIndex("email");
            int getUserName = cursor.getColumnIndex("user_name");
            int getPassword = cursor.getColumnIndex("password");

            cursor.moveToFirst();

            if(cursor != null && cursor.getCount() > 0) {
                String name = cursor.getString(getName);
                String email = cursor.getString(getEmail);
                String userName = cursor.getString(getUserName);
                String password = cursor.getString(getPassword);

                dataToReturn.add(name);
                dataToReturn.add(email);
                dataToReturn.add(userName);
                dataToReturn.add(password);

            }
            return dataToReturn;
        }

        public void changeStatus(String field, String newValue, String userName) {
            db = this.getWritableDatabase();

            String sql = "UPDATE "+TABLE_USERS+" SET "+field+"='"+newValue+"' WHERE TRIM(user_name) LIKE '" + userName.trim() + "'";

            db.execSQL(sql);

        }

        public List<Notes> getNotes(String userName) {
            List<Notes> notesList = new ArrayList<>();


            db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NOTES + " WHERE TRIM(notes_user)= '" + userName.trim() + "'", null);

            int getId = cursor.getColumnIndex("id1");
            int getTitle = cursor.getColumnIndex("title");
            int getDate = cursor.getColumnIndex("date");
            int getContent = cursor.getColumnIndex("content");
            int getNoteUser = cursor.getColumnIndex("notes_user");

            cursor.moveToFirst();

            if(cursor !=null && cursor.getCount() > 0) {
                do {

                    Notes note = new Notes();
                    note.setId(cursor.getInt(getId));
                    note.setTitle(cursor.getString(getTitle));
                    note.setDate(cursor.getString(getDate));
                    note.setContent(cursor.getString(getContent));
                    note.setNotes_user(cursor.getString(getNoteUser));

                    notesList.add(note);
                }while (cursor.moveToNext());

            } else {
                return null;
            }
            return notesList;
        }

}


