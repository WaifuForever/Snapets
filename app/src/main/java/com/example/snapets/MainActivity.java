package com.example.snapets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.snapets.controller.PageController;
import com.example.snapets.controller.UserController;
import com.example.snapets.view.ManageData;
import com.example.snapets.view.login.FirstActivity;
import com.example.snapets.view.menu.feed.FeedFragment;
import com.example.snapets.view.menu.newPost.newPostFragment;
import com.example.snapets.view.menu.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements ManageData {

    private AppBarConfiguration mAppBarConfiguration;
    private int altNumber = 1;

    protected DrawerLayout drawer = findViewById(R.id.drawer_layout);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);

        drawer.addView(contentView, 0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PageController.getInstance().changePage(getSupportFragmentManager(), new newPostFragment());

            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        navView.setNavigationItemSelectedListener(navigatonItemSelected);


        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                new FeedFragment()).commit();

        Log.d("Fragments", getSupportFragmentManager().getFragments().toString());

        //PageController.getInstance().changePage(getSupportFragmentManager(), new FeedFragment());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        TextView username = findViewById(R.id.header_username);
        TextView email = findViewById(R.id.header_email);
        ImageView profile = findViewById(R.id.header_profile);


        UserController.getInstance().getUserData(this, UserController.getInstance().getAuth().getCurrentUser().getUid(),"username", username);
        UserController.getInstance().getUserData(this, UserController.getInstance().getAuth().getCurrentUser().getUid(),"email", email);
        UserController.getInstance().getUserData(this, UserController.getInstance().getAuth().getCurrentUser().getUid(),"image", profile);

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void manageData(Object data, Object view) {
        if (view instanceof ImageView){
            Glide.with(this.getBaseContext())
                    .load(data)
                    .into((ImageView)view);

            //holder.postImage.setContentDescription("Teste");
            if(data != null)
                Log.d("change imageView", "MainActivity " + data.toString());
            else
                Log.d("change imageView", "MainActivity null");
        }
        else if ( view instanceof TextView){
            ((TextView) view).setText((String) data);
            Log.d("change TextView", "MainActivity " + (String) data);
        }

        else{
            Log.d("failed change", "MainActivity " + view + " cannot be found");
        }
    }


    private NavigationView.OnNavigationItemSelectedListener navigatonItemSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId()){
                case R.id.nav_home:
                    if(altNumber == 0){

                        altNumber = 1;
                        PageController.getInstance().changePage(getSupportFragmentManager(), new FeedFragment());
                        menuItem.setTitle("Perfil");
                        menuItem.setIcon(R.drawable.ic_menu_profile);


                        Log.d("CurrentFragment", PageController.getInstance().getCurrentFragment(getSupportFragmentManager()).toString());
                    }

                    else{

                        PageController.getInstance().changePage(getSupportFragmentManager(),
                                new ProfileFragment(UserController.getInstance().getAuth().getCurrentUser().getUid()));

                        altNumber = 0;

                        menuItem.setTitle("Feed");
                        menuItem.setIcon(R.drawable.ic_feed);
                        Log.d("CurrentFragment", PageController.getInstance().getCurrentFragment(getSupportFragmentManager()).toString());
                    }

                    break;

                case R.id.nav_logout:

                    UserController.getInstance().getAuth().signOut();
                    Intent a = new Intent(getApplicationContext(), FirstActivity.class);
                    startActivity(a);
                    finish();
                    break;

                case R.id.nav_tools:
                    Toast.makeText(getApplicationContext(), "Função não implementada", Toast.LENGTH_LONG).show();

                    break;

                case R.id.nav_gallery:
                    Toast.makeText(getApplicationContext(), "Função não implementada", Toast.LENGTH_LONG).show();
                    break;

                case R.id.nav_slideshow:
                    Toast.makeText(getApplicationContext(), "Função não implementada", Toast.LENGTH_LONG).show();
                    break;

                case R.id.nav_send:
                    Toast.makeText(getApplicationContext(), "Função não implementada", Toast.LENGTH_LONG).show();
                    break;

                case R.id.nav_share:
                    Toast.makeText(getApplicationContext(), "Função não implementada", Toast.LENGTH_LONG).show();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
            }

            return true;
        }
    };
}
