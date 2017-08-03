package com.example.nicke.loginapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicke.loginapplication.SQLDatabase.SQLDatabaseManager;
import com.example.nicke.loginapplication.models.User;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name_editText,
            email_editText,
            userName_editText,
            password_editText,
            confirmPassword_editText;

    Button registerBtn;

    TextView logIn_clickable_textView;

    SQLDatabaseManager sqlDatabaseManager = new SQLDatabaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initilizeView();
        setListeners();
    }

    private void initilizeView() {
        name_editText = (EditText) findViewById(R.id.name_editText);
        email_editText = (EditText) findViewById(R.id.email_editText);
        userName_editText = (EditText) findViewById(R.id.register_userName_editText);
        password_editText = (EditText) findViewById(R.id.register_password_editText);
        confirmPassword_editText = (EditText) findViewById(R.id.register_password_confirm_editText);

        registerBtn = (Button) findViewById(R.id.register_button);

        logIn_clickable_textView = (TextView) findViewById(R.id.register_log_in_button);
    }

    private void setListeners() {

        registerBtn.setOnClickListener(this);
        logIn_clickable_textView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                String name = name_editText.getText().toString();
                String email = email_editText.getText().toString();
                String userName = userName_editText.getText().toString();
                String password = password_editText.getText().toString();
                String confirmPassword = confirmPassword_editText.getText().toString();

                String[] strings = {name, email, userName, password, confirmPassword};
                boolean areThereEmptyFields = false;
                for (String string : strings) {
                    if (string.trim().equals("")) {
                        areThereEmptyFields = true;
                    }
                }

                if(areThereEmptyFields) {
                    Toast.makeText(this, R.string.fill_all_fields_toast, Toast.LENGTH_SHORT).show();
                    showEmptyFields();
                    break;
                }

                String emailPatern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(!email.trim().matches(emailPatern)) {
                    email_editText.setError(getResources().getText(R.string.invalid_email_address_toast));
                    break;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(this, R.string.no_matching_passwords_toast, Toast.LENGTH_SHORT).show();
                    password_editText.setError(getResources().getText(R.string.no_matching_passwords_toast));
                    confirmPassword_editText.setError(getResources().getText(R.string.no_matching_passwords_toast));
                } else {
                    User user = new User();

                    user.setName(name);
                    user.setEmail(email);
                    user.setUserName(userName);
                    user.setPassword(password);

                    String doesExist = sqlDatabaseManager.checkIfExists(userName, password, false);
                    if (doesExist.equals("userName")) {
                        Toast.makeText(this, R.string.user_name_exists_toast, Toast.LENGTH_SHORT).show();
                        userName_editText.setError(getResources().getText(R.string.user_name_exists_toast));
                    } else if (doesExist.equals("password")) {
                        Toast.makeText(this, R.string.password_exists_toast, Toast.LENGTH_SHORT).show();
                        password_editText.setError(getResources().getText(R.string.password_exists_toast));
                    } else {
                        Toast.makeText(this, userName + " " + getString(R.string.registred_successfuly_toast), Toast.LENGTH_SHORT).show();
                        sqlDatabaseManager.addUser(user);
                        Intent intent = new Intent(RegisterActivity.this, ActivityUserAccount.class);
                        intent.putExtra("id", user.getId());
                        intent.putExtra("name", user.getName());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("username", user.getUserName());
                        intent.putExtra("password", user.getPassword());

                        startActivity(intent);
                        finish();
                    }

                }

                break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
    }

    private void showEmptyFields() {
        EditText[] fields = {name_editText, email_editText, userName_editText, password_editText, confirmPassword_editText};

        for (int i = 0; i <fields.length ; i++) {
            if(fields[i].getText().toString().trim().equals("")) {
                fields[i].setError(getString(R.string.this_field_is_empty_error));
            }
        }

    }
}
