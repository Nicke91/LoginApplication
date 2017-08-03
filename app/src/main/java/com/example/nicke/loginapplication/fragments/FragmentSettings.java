package com.example.nicke.loginapplication.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicke.loginapplication.ActivityUserAccount;
import com.example.nicke.loginapplication.MainActivity;
import com.example.nicke.loginapplication.R;
import com.example.nicke.loginapplication.SQLDatabase.SQLDatabaseManager;
import com.example.nicke.loginapplication.models.User;

import java.util.ArrayList;

/**
 * Created by Nicke on 7/24/2017.
 */

public class FragmentSettings extends Fragment implements View.OnClickListener {

    User user;

    ActivityUserAccount parentActivity;


    TextView name_holder_textView,
             email_holder_textView,
             userName_holder_textView,
             password_holder_textView;

    SlidingDrawer slidingDrawer;

    Button changeName_button,
           changeEmail_button,
           changeUserName_button,
           changePassword_button,
           deleteProfile_button;

    Button slideButton;

    Bundle bundle;
    SQLDatabaseManager sqlDatabaseManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        init(view);
        getData();
        setListeners();

        return view;
    }

    private void init(View view) {
        name_holder_textView = (TextView) view.findViewById(R.id.name_holder);
        email_holder_textView = (TextView) view.findViewById(R.id.email_holder);
        userName_holder_textView =(TextView) view.findViewById(R.id.userName_holder);
        password_holder_textView = (TextView) view.findViewById(R.id.password_holder);

        slidingDrawer = (SlidingDrawer) view.findViewById(R.id.settingsDrawer);
        slideButton = (Button) view.findViewById(R.id.slideButton);

        changeName_button = (Button) view.findViewById(R.id.changeName_button);
        changeEmail_button = (Button) view.findViewById(R.id.changeEmail_button);
        changeUserName_button = (Button) view.findViewById(R.id.changeUserName_button);
        changePassword_button = (Button) view.findViewById(R.id.changePassword_button);

        deleteProfile_button = (Button) view.findViewById(R.id.deleteProfile_button);

        bundle = new Bundle();
        sqlDatabaseManager = new SQLDatabaseManager(getContext());

        parentActivity = (ActivityUserAccount)getActivity();


        setUpDrawer();

    }



    private void getData() {
        bundle = getArguments();

        setTexts(bundle);


    }


    private void setListeners() {
        changeName_button.setOnClickListener(this);
        changeEmail_button.setOnClickListener(this);
        changeUserName_button.setOnClickListener(this);
        changePassword_button.setOnClickListener(this);
        deleteProfile_button.setOnClickListener(this);
    }

    private void setUpDrawer () {
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slideButton.setText(getResources().getString(R.string.change_settings_button));
            }
        });

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                slideButton.setText(getResources().getString(R.string.close_settings_button));
            }
        });
    }

    private void setTexts(Bundle bundle) {

        try {

           String name = bundle.getString("name");
            String email = bundle.getString("email");
            String userName = bundle.getString("username");
            String password = bundle.getString("password");

            user = new User(name, email, userName, password);

            name_holder_textView.setText(user.getName());
            email_holder_textView.setText(user.getEmail());
            userName_holder_textView.setText(user.getUserName());
            password_holder_textView.setText(user.getPassword());



        }catch (NullPointerException e) {
            Log.e("NullPointerException", "Probably null");
        }
    }


    @Override
    public void onClick(View v) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogLayout = inflater.inflate(R.layout.change_status_dialog, null);

        dialog.setView(dialogLayout);

        final EditText inputField = (EditText) dialogLayout.findViewById(R.id.statusDialog_edit_field);
        TextView fieldHolder = (TextView) dialogLayout.findViewById(R.id.statusDialog_field);

        String ok = (String) getResources().getText(R.string.ok_button);
        String cancel = (String) getResources().getText(R.string.cancel_button);

        final String fieldTitle;


        switch (v.getId()) {


            case R.id.changeName_button:

                fieldTitle = (String) getResources().getText(R.string.name_textView);
                fieldHolder.setText(fieldTitle);
                inputField.setText(user.getName());

                dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String changedName = inputField.getText().toString();

                        if(!changedName.trim().equals("")) {

                            changeSettings(changedName, "name", 0, name_holder_textView, user.getUserName());

                        } else {
                            Toast.makeText(getActivity(), R.string.fill_the_field_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                break;

            case R.id.changeEmail_button:

                fieldTitle = (String) getResources().getText(R.string.email_editText);
                fieldHolder.setText(fieldTitle);
                inputField.setText(user.getEmail());

                dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String changeEmail = inputField.getText().toString();
                        String emailPatern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                        if(!changeEmail.trim().equals("") && changeEmail.trim().matches(emailPatern)){

                            changeSettings(changeEmail, "email", 1, email_holder_textView, user.getUserName());

                        }else {
                            Toast.makeText(getActivity(), R.string.invalid_email_address_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                break;

            case R.id.changeUserName_button:

                fieldTitle = (String) getResources().getText(R.string.user_name_editText);
                fieldHolder.setText(fieldTitle);
                inputField.setText(user.getUserName());

                dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String changeUserName = inputField.getText().toString();

                        if(!changeUserName.trim().equals("")) {
                            String doesExists = sqlDatabaseManager.checkIfExists(changeUserName, user.getPassword(), false);

                            if (doesExists.equals("userName")) {
                                Toast.makeText(getContext(), R.string.user_name_exists_toast, Toast.LENGTH_SHORT).show();
                            } else {
                                sqlDatabaseManager.changeStatus("user_name", changeUserName, user.getUserName());

                                ArrayList<String> newData = sqlDatabaseManager.getAll(changeUserName);

                                user.setUserName(newData.get(2));

                                userName_holder_textView.setText(user.getUserName());

                                parentActivity.updateSettings(user.getName(), user.getEmail(), user.getUserName(), user.getPassword());
                                Toast.makeText(getContext(), R.string.profile_changed_toast, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), R.string.fill_the_field_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                break;

            case R.id.changePassword_button:

                fieldTitle = (String) getResources().getText(R.string.password_editText);
                fieldHolder.setText(fieldTitle);

                dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String changePassword = inputField.getText().toString();

                        if (!changePassword.trim().equals("")) {
                            String doesExists = sqlDatabaseManager.checkIfExists("", changePassword, true);

                            if(doesExists.equals("password")) {
                                Toast.makeText(getContext(), R.string.password_exists_toast, Toast.LENGTH_SHORT).show();
                            } else {

                                changeSettings(changePassword, "password", 3, password_holder_textView, user.getUserName());

                            }
                        } else {
                            Toast.makeText(getContext(), R.string.fill_the_field_toast, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                break;

            case R.id.deleteProfile_button:


                AlertDialog.Builder deleteProfile_dialog = new AlertDialog.Builder(getContext());
                deleteProfile_dialog.setTitle(R.string.delete_user_dialog);
                deleteProfile_dialog.setMessage(R.string.check_to_delete_dialog);

                deleteProfile_dialog.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface deleteProfile_dialog, int which) {

                        String userName = bundle.getString("username");
                        sqlDatabaseManager.deleteUser(userName);

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                    }
                });

                deleteProfile_dialog.setNegativeButton(R.string.no_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface deleteProfile_dialog, int which) {

                        deleteProfile_dialog.cancel();

                    }
                });
                deleteProfile_dialog.show();
                break;

        }


    }
    private void changeSettings(String changedField, String SQLName, int position, TextView viewToChange, String userName) {
        sqlDatabaseManager.changeStatus(SQLName, changedField, userName);

        ArrayList<String> newData = sqlDatabaseManager.getAll(userName);

        switch (SQLName) {
            case "name":
                user.setName(newData.get(position));
                break;
            case "email":
                user.setEmail(newData.get(position));
                break;
            case "user_name":
                user.setUserName(newData.get(position));
                break;
            case "password":
                user.setPassword(newData.get(position));
        }

        viewToChange.setText(changedField);

        parentActivity.updateSettings(user.getName(), user.getEmail(), user.getUserName(), user.getPassword());
        Toast.makeText(getContext(), R.string.profile_changed_toast, Toast.LENGTH_SHORT).show();



    }


}
