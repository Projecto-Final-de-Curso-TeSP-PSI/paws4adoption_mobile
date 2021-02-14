package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.paws4adoption.utils.NetworkStateReceiver;
import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;

public class MenuMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NetworkStateReceiver.NetworkStateReceiverListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private NetworkStateReceiver networkStateReceiver;
    private Menu menu;
    private View hview;
    private TextView usernameDisplay;
    private MenuItem navLoginItem;
    private MenuItem navPostWanderingAnimal;
    private MenuItem navPostLostAnimal;
    private MenuItem navMyAnimals;

    /**
     * On create state of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Fragment fragment = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        menu = navigationView.getMenu();
        hview = navigationView.getHeaderView(0);
        usernameDisplay = hview.findViewById(R.id.tvUsername);
        navLoginItem = menu.findItem(R.id.navLogin);
        navPostWanderingAnimal = menu.findItem(R.id.navPostWanderingAnimal);
        navPostLostAnimal = menu.findItem(R.id.navPostLostAnimal);
        navMyAnimals = menu.findItem(R.id.navMyAnimals);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if(FortuneTeller.isInternetConnection(getApplicationContext()))
                    if(FortuneTeller.isLoggedUser(getApplicationContext())){
                        String loginAwareness = getString(R.string.user_prefix) + Vault.getLoggedUsername(getApplicationContext());
                        usernameDisplay.setText(loginAwareness);

                        usernameDisplay.setVisibility(View.VISIBLE);

                        navLoginItem.setTitle(R.string.logout);
                    } else {
                        usernameDisplay.setVisibility(View.GONE);

                        navLoginItem.setTitle(R.string.login);
                    }

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);

            }
        };

        toggle.syncState();
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragment = new MainFragment();
        setTitle(getString(R.string.title_main_fragment));

        navigationView.setCheckedItem(R.id.navHome);

        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment, "MainFragment").commit();
    }

    public void onDestroy(){
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    /**
     * Gets the selected item on the drawer, and sets the events to each one
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        Fragment fragment = null;
        Intent intent = null;
        Bundle bundle = new Bundle();
        String tag = "";

        navigationView.setCheckedItem(menuItem.getItemId());

        switch (menuItem .getItemId()) {
            case R.id.navHome:
                tag = "MainFragment";
                fragment = new MainFragment();
                setTitle(R.string.title_main_fragment);
                break;

            case R.id.navSearchAdoption:
                fragment = new ListAnimalsFragment();
                bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_GENERAL_LIST);
                bundle.putString(RockChisel.ANIMAL_TYPE, RockChisel.ADOPTION_ANIMAL);
                fragment.setArguments(bundle);
                setTitle(menuItem.getTitle());
                break;

            case R.id.navSearchMissing:
                fragment = new ListAnimalsFragment();
                bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_GENERAL_LIST);
                bundle.putString(RockChisel.ANIMAL_TYPE, RockChisel.MISSING_ANIMAL);
                fragment.setArguments(bundle);
                setTitle(menuItem.getTitle());
                break;

            case R.id.navSearchFound:
                fragment = new ListAnimalsFragment();
                bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_GENERAL_LIST);
                bundle.putString(RockChisel.ANIMAL_TYPE, RockChisel.FOUND_ANIMAL);
                fragment.setArguments(bundle);
                setTitle(menuItem.getTitle());
                break;

            case R.id.navSearchAssociations:
                fragment = new ListOrganizationsFragment();
                setTitle(menuItem.getTitle());
                break;

            case R.id.navPostWanderingAnimal:

                if(FortuneTeller.isLoggedUser(getApplicationContext())){
                    intent = new Intent(getApplicationContext(), PostAnimalActivity.class);
                    intent.putExtra(PostAnimalActivity.ANIMAL_TYPE, RockChisel.FOUND_ANIMAL);
                    intent.putExtra(PostAnimalActivity.ACTION, RockChisel.ACTION_CREATE);
                } else{
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                break;

            case R.id.navPostLostAnimal:

                if(FortuneTeller.isLoggedUser(getApplicationContext())){
                    intent = new Intent(getApplicationContext(), PostAnimalActivity.class);
                    intent.putExtra(PostAnimalActivity.ANIMAL_TYPE, RockChisel.MISSING_ANIMAL);
                    intent.putExtra(PostAnimalActivity.ACTION, RockChisel.ACTION_CREATE);
                } else{
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                break;

            case R.id.navMyAnimals:

                if(FortuneTeller.isLoggedUser(getApplicationContext())){
                    fragment = new ListAnimalsFragment();
                    bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_MY_LIST);
                    fragment.setArguments(bundle);
                    setTitle(menuItem.getTitle());
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                break;

            case R.id.navLogin:

                if (FortuneTeller.isLoggedUser(getApplicationContext())) {
                    Vault.clearUserPreferences(getApplicationContext());
                    usernameDisplay.setVisibility(View.GONE);
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }
                break;
        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment, tag).commit();

        if(intent != null)
            startActivity(intent);


        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void networkAvailable() {
        Log.d("Internet Status", "---> Habemus INTERNET!");

        String loginAwareness = getString(R.string.user_prefix) + Vault.getLoggedUsername(getApplicationContext());

        navLoginItem.setEnabled(true);
        navMyAnimals.setEnabled(true);
        navPostLostAnimal.setEnabled(true);
        navPostWanderingAnimal.setEnabled(true);


        if (FortuneTeller.isLoggedUser(getApplicationContext())) {
            usernameDisplay.setText(loginAwareness);
            usernameDisplay.setVisibility(View.VISIBLE);


            navLoginItem.setTitle(R.string.logout);
        } else {
            usernameDisplay.setText(R.string.empty);
            usernameDisplay.setVisibility(View.GONE);

            navLoginItem.setTitle(R.string.login);
        }

        MainFragment mainFragmentDemo = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
        if(mainFragmentDemo != null){
            mainFragmentDemo.netAvailable();
        }
    }

    @Override
    public void networkUnavailable() {
        Log.d("Internet Status", "---> Não temos net!");

        Vault.clearUserPreferences(getApplicationContext());

        navLoginItem.setEnabled(false);
        navLoginItem.setTitle(R.string.login);

        navMyAnimals.setEnabled(false);
        navPostLostAnimal.setEnabled(false);
        navPostWanderingAnimal.setEnabled(false);

        usernameDisplay.setText(R.string.internet_connection_down);
        usernameDisplay.setVisibility(View.VISIBLE);

        MainFragment mainFragmentDemo = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
        if(mainFragmentDemo != null){
            mainFragmentDemo.netUnavailable();
        }
    }

}