package com.example.personalfilmcollectionmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {
    private UserList userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userList = getUserList();

        setInfo();

        String[] users = userList.getUserNames();

        Spinner spinner = findViewById(R.id.users);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void signIn(View view) {
        if (userList.getUserNumber() != 0) {
            Spinner spinner = findViewById(R.id.users);
            String name = spinner.getSelectedItem().toString();

            User user = userList.findUser(name);

            Intent intent = new Intent(this, FilmListActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private UserList getUserList() {
        UserList userList = new UserList();

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("users.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT)");
        Cursor query = db.rawQuery("SELECT * FROM users;", null);

        if(query.moveToFirst()) {
            String name = query.getString(0);

            userList.addUser(new User(name));
        }

        query.close();
        db.close();

        return userList;
    }

    private void setInfo() {
        TextView textView = findViewById(R.id.activity_sign_in_info);

        if (userList.getUserNumber() == 0) {
            textView.setText("There are not users in list");
        } else {
            textView.setText("Select your username \n and sign in");
        }
    }
}
