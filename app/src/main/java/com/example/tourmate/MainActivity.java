package com.example.tourmate;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.viewmodels.LoginViewModel;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean isExit = false;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.myview);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_userName);
        navUsername.setText("Rz Tutul");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragmnet);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.eventListFragment:
                        isExit = true;
                        break;

                        case R.id.loginFragment:
                        isExit = true;
                        break;


                    default:
                        isExit = false;
                        break;
                }
            }
        });


    }

    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isExit) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                super.onBackPressed();
            }


        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Navigation.findNavController(this, R.id.nav_host_fragmnet)
                        .navigate(R.id.eventListFragment);
                break;

            case R.id.nav_event:
                Navigation.findNavController(this, R.id.nav_host_fragmnet)
                        .navigate(R.id.eventListFragment);
                break;

            case R.id.nav_weather:
                Navigation.findNavController(this, R.id.nav_host_fragmnet)
                        .navigate(R.id.weatherFragment);
                break;

            case R.id.nav_nearby:
                Navigation.findNavController(this, R.id.nav_host_fragmnet)
                        .navigate(R.id.nearByFragment);
                break;
            case R.id.nav_compass:
                Navigation.findNavController(this, R.id.nav_host_fragmnet)
                        .navigate(R.id.compassFragment);
                break;
            case R.id.nav_logout:
                Navigation.findNavController(this, R.id.nav_host_fragmnet)
                        .navigate(R.id.loginFragment);
                break;



        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

    /*@Override
    public void onBackPressed() {

    *//* count = getSupportFragmentManager().getBackStackEntryCount();
        if (count ==0) {
            super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            getSupportFragmentManager().popBackStack();
        }

        *//*

    }

}
*/