package com.example.nicke.loginapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicke.loginapplication.SQLDatabase.SQLDatabaseManager;
import com.example.nicke.loginapplication.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    EditText userName_editText, password_editText;
    Button logInBtn;
    TextView signIn_clickable_textView;
    CheckBox forgotPasswordChckBox;

    SQLDatabaseManager sqlDatabaseManager = new SQLDatabaseManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initilizeViews();
        setListeners();

    }

    private void initilizeViews() {
        userName_editText = (EditText) findViewById(R.id.userName_editText);
        password_editText = (EditText) findViewById(R.id.password_editText);

        logInBtn = (Button) findViewById(R.id.log_in_button);

        signIn_clickable_textView = (TextView) findViewById(R.id.signUp_textView);

        forgotPasswordChckBox = (CheckBox) findViewById(R.id.forgotPassword_checkBox);
    }


    private void setListeners() {

        logInBtn.setOnClickListener(this);
        signIn_clickable_textView.setOnClickListener(this);

        forgotPasswordChckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.log_in_button:

                if(forgotPasswordChckBox.isChecked()) {
                    String getUserName = userName_editText.getText().toString();
                    String getEmail = password_editText.getText().toString();

                    if(!getUserName.trim().equals("") && !getEmail.trim().equals("")) {

                        String checkUserName = sqlDatabaseManager.searchUserName(getUserName);
                        String checkEmail = sqlDatabaseManager.searchEmail(getUserName, getEmail);

                        if(getUserName.equals(checkUserName)) {

                            String emailPatern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                            if (!getEmail.trim().matches(emailPatern)) {
                                password_editText.setError(getResources().getText(R.string.invalid_email_address_toast));
                            } else {
                               if(checkEmail.equals(getEmail)) {
                                   AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                                   dialog.setTitle(getEmail);
                                   dialog.setMessage("Your password is " + sqlDatabaseManager.returnPassword(getUserName, getEmail));
                                   dialog.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           dialog.dismiss();
                                       }
                                   });
                                   dialog.show();
                               } else {
                                   password_editText.setError(getResources().getText(R.string.invalid_email_address_toast));
                               }
                            }
                        } else {
                            Toast.makeText(this, R.string.user_name_doesnt_exists_toast, Toast.LENGTH_SHORT).show();
                            userName_editText.setError(getResources().getText(R.string.user_name_doesnt_exists_toast));
                        }

                    } else {
                        Toast.makeText(this, R.string.fill_all_fields_toast, Toast.LENGTH_SHORT).show();
                    }

                } else {

                    String userName = userName_editText.getText().toString();
                    String password = password_editText.getText().toString();

                    if (!userName.trim().equals("") && !password.trim().equals("")) {

                        String checkUserName = sqlDatabaseManager.searchUserName(userName);
                        String checkPassword = sqlDatabaseManager.searchPassword(userName, password);

                        if (userName.equals(checkUserName)) {

                            if (password.equals(checkPassword)) {
                                Toast.makeText(this, R.string.login_successfull_toast, Toast.LENGTH_SHORT).show();

                                ArrayList<String> listOfData = sqlDatabaseManager.getAll(userName);
                                User user = new User();

                                user.setName(listOfData.get(0));
                                user.setEmail(listOfData.get(1));
                                user.setUserName(listOfData.get(2));
                                user.setPassword(listOfData.get(3));

                                Intent intent = new Intent(MainActivity.this, ActivityUserAccount.class);
                                intent.putExtra("name", user.getName());
                                intent.putExtra("email", user.getEmail());
                                intent.putExtra("username", user.getUserName());
                                intent.putExtra("password", user.getPassword());

                                startActivity(intent);


                            } else {
                                Toast.makeText(this, R.string.password_incorrect_toast, Toast.LENGTH_SHORT).show();
                                password_editText.setError(getResources().getText(R.string.password_incorrect_toast));
                            }
                        } else {
                            Toast.makeText(this, R.string.user_name_doesnt_exists_toast, Toast.LENGTH_SHORT).show();
                            userName_editText.setError(getResources().getText(R.string.user_name_doesnt_exists_toast));

                        }
                    } else {
                        Toast.makeText(this, R.string.fill_all_fields_toast, Toast.LENGTH_SHORT).show();

                    }

                }
                break;

            case R.id.signUp_textView:
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);

                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.all_users_item:
                Intent intent = new Intent(MainActivity.this, ActivityAllUsers.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            password_editText.setHint("Email");
            password_editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            password_editText.setTransformationMethod(null);
            logInBtn.setText("Get Password");
        } else {
            password_editText.setHint(getResources().getText(R.string.password_editText));
            password_editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password_editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            logInBtn.setText(getResources().getText(R.string.login_button));
        }
    }
}
