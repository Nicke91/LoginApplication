package com.example.nicke.loginapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nicke.loginapplication.adapters.NavigationDrawerAdapter;
import com.example.nicke.loginapplication.fragments.FragmentHome;
import com.example.nicke.loginapplication.fragments.FragmentMovies;
import com.example.nicke.loginapplication.fragments.FragmentSettings;
import com.example.nicke.loginapplication.models.NavigationItems;
import com.example.nicke.loginapplication.models.User;

import java.util.ArrayList;

public class ActivityUserAccount extends AppCompatActivity {
    DrawerLayout drawer;

    ArrayList<NavigationItems> navigationItems;
    ActionBarDrawerToggle actionBarToggle;
    NavigationDrawerAdapter drawerAdapter;

    RecyclerView recyclerView;

    FragmentManager fragmentManager;
    Toolbar toolbar;
    RelativeLayout left_slider;

    TextView userNameTextView,
            emailTextView;

    Bundle bundle;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        init();
        getData();

        populateListItems();

        if (savedInstanceState == null) {
            selectItem(0);
        }


    }

    private void init() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.list_slidermenu);
        toolbar = (Toolbar) findViewById(R.id.toolar);

        left_slider = (RelativeLayout) findViewById(R.id.slider);

        userNameTextView = (TextView) findViewById(R.id.profile_userName);
        emailTextView = (TextView) findViewById(R.id.profile_email);

        fragmentManager = getSupportFragmentManager();
        navigationItems = new ArrayList<>();

        bundle = new Bundle();

        actionBarToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(android.view.View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.addDrawerListener(actionBarToggle);
    }

    private void getData() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String userName = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        user = new User(name, email, userName, password);

        sendDataToFragments(name, email, userName, password);

        userNameTextView.setText(user.getUserName());
        emailTextView.setText(user.getEmail());

    }

    private void sendDataToFragments( String name, String email, String userName, String password) {

        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("username", userName);
        bundle.putString("password", password);
    }

    private void populateListItems() {
        Integer icons[] = {android.R.drawable.ic_menu_mylocation, android.R.drawable.ic_menu_send,
                android.R.drawable.ic_menu_more, android.R.drawable.ic_lock_power_off};

        String title[] = getResources().getStringArray(R.array.list_items);
        String subtitle[] = getResources().getStringArray(R.array.list_subitems);

        for (int i = 0; i <icons.length; i++) {
            navigationItems.add(new NavigationItems(title[i], subtitle[i], icons[i]));
        }

        drawerAdapter = new NavigationDrawerAdapter(this ,navigationItems);

        recyclerView.setAdapter(drawerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerAdapter.notifyDataSetChanged();


    }

    public void selectItem(int position) {
        toolbar.setTitle(navigationItems.get(position).getTitle());

        closeDrawer();
        switch (position) {
            case 0:
                replaceFragment(new FragmentHome(), "Home");
                break;
            case 1:
                replaceFragment(new FragmentMovies(), "Upcoming Movies");
                break;
            case 2:
                replaceFragment(new FragmentSettings(), "Profile Settings");
                break;
            case 3:
                Intent intent = new Intent(ActivityUserAccount.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    void replaceFragment(Fragment fragment, String tag) {
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, tag).commit();

    }

    void closeDrawer() {
        if (drawer.isDrawerOpen(left_slider)) {
            drawer.closeDrawer(left_slider);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentByTag("Home");
        if(fragment == null) {
            selectItem(0);
        } else {

            if (drawer.isDrawerOpen(left_slider)) {
                drawer.closeDrawer(left_slider);
            } else {
                moveTaskToBack(true);
            }
        }
    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync state for actionbar toggle
        actionBarToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarToggle.onConfigurationChanged(newConfig);
    }

    public void updateSettings(String changedName, String changedEmail, String changedUserName, String changedPassword) {
        user.setName(changedName);
        user.setEmail(changedEmail);
        user.setUserName(changedUserName);
        user.setPassword(changedPassword);

        sendDataToFragments(user.getName(), user.getEmail(), user.getUserName(), user.getPassword());

        emailTextView.setText(user.getEmail());
        userNameTextView.setText(user.getUserName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getSupportFragmentManager().findFragmentByTag("Home").onActivityResult(requestCode, resultCode, data);
    }
}
