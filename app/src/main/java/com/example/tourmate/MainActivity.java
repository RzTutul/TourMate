package com.example.tourmate;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;


import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.tourmate.pojos.UserInformationPojo;
import com.example.tourmate.viewmodels.LoginViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isExit = false;
    private boolean isBack = false;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View view;
    private NavController navController;
    public static String eventID;
    LoginViewModel loginViewModel;
    TextView navUsername, navEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view = findViewById(R.id.myview);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //For acttion Bar title Change
        Typeface font2 = Typeface.createFromAsset(getAssets(), "fonts/thinfont.otf");
        SpannableStringBuilder SS = new SpannableStringBuilder("TourMate");
        SS.setSpan(new CustomTypefaceSpan("", font2), 0, SS.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(SS);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //For Bottom Nevigation
        final BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        View headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.nav_userName);
        navEmail = headerView.findViewById(R.id.nav_email);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Event List").setIcon(R.drawable.ic_event_note_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("New Event").setIcon(R.drawable.ic_add_circle_black_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabpost = tab.getPosition();

                switch (tabpost) {
                    case 0:

                        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragmnet).navigate(R.id.eventListFragment);
                        break;

                    case 1:
                        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragmnet).navigate(R.id.add_Event);
                        break;

                    default:
                        break;

                }

                //viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        loginViewModel.stateLiveData.observe(this, new Observer<LoginViewModel.AuthenticationState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticationState authenticationState) {
                switch (authenticationState) {
                    case AUTHENTICATED:

                        loginViewModel.getUserInfo();
                        loginViewModel.userInfoLD.observe(MainActivity.this, new Observer<UserInformationPojo>() {
                            @Override
                            public void onChanged(UserInformationPojo userInformationPojo) {

                                navUsername.setText(userInformationPojo.getUserName());
                                navEmail.setText(userInformationPojo.getUserEmail());

                            }
                        });

                        break;
                    case UNAUTHENTICATED:
                        break;
                }
            }
        });


        navController = Navigation.findNavController(this, R.id.nav_host_fragmnet);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.eventListFragment:
                        isExit = true;
                        tabLayout.getTabAt(0);
                        bottomNav.setVisibility(View.GONE);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                tabLayout.setVisibility(View.VISIBLE);
                                tabLayout.getTabAt(0).select();
                            }
                        });
                        break;

                    case R.id.loginFragment:
                        isExit = true;
                        bottomNav.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        break;

                    case R.id.add_Event:
                        tabLayout.getTabAt(1).select();
                        tabLayout.setVisibility(View.VISIBLE);

                        isExit = false;
                        isBack = true;
                        break;



                    case R.id.mainDashBoard:
                        bottomNav.getMenu().findItem(R.id.botton_nav_expense).setChecked(true);
                        bottomNav.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.GONE);
                        isBack = true;
                        isExit = false;
                        break;
                    case R.id.momentGallary:
                        bottomNav.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.GONE);
                        isBack = true;
                        isExit = false;
                        break;

                    default:
                        bottomNav.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        isExit = false;
                        break;
                }
            }
        });


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("id", eventID);

                    switch (item.getItemId()) {
                        case R.id.botton_nav_expense:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragmnet).navigate(R.id.mainDashBoard, bundle);

                            break;
                        case R.id.bottom_nav_moment:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragmnet).navigate(R.id.momentGallary, bundle);
                            break;
                        case R.id.bottom_nav_event:
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragmnet).navigate(R.id.eventListFragment);
                            break;

                        default:
                            break;
                    }

                    return true;
                }
            };


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
            } else if (isBack) {
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragmnet).navigate(R.id.eventListFragment);
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
                        .navigate(R.id.tourPlaceList);
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
                loginViewModel.getLogoutUser();
                Navigation.findNavController(this, R.id.nav_host_fragmnet)
                        .navigate(R.id.loginFragment);
                break;

            case R.id.nav_exit:
                this.finish();
            default:
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
