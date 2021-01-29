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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.paws4adoption.utils.NetworkStateReceiver;
import amsi.dei.estg.ipleiria.paws4adoption.views.MainFragment;
import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;

public class MenuMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NetworkStateReceiver.NetworkStateReceiverListener {

    public static String token;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private String typeAnimal;
    private NetworkStateReceiver networkStateReceiver;
    private Menu menu;
    private View hview;
    private TextView usernameDisplay;
    private MenuItem navLoginItem;

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        toggle.syncState();
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragment = new MainFragment();
        setTitle("Home");

        navigationView.setCheckedItem(R.id.navHome);

        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
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

//        MenuItem previousSelectedItem = navigationView.getCheckedItem();
//
//        if(previousSelectedItem != null )
//            previousSelectedItem.setChecked(false);
//
//        menuItem.setChecked(true);
        Bundle bundle = new Bundle();

        switch (menuItem .getItemId()) {
            case R.id.navHome:
                fragment = new MainFragment();
                bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_GENERAL_LIST);
                bundle.putString(RockChisel.ANIMAL_TYPE, RockChisel.SCENARIO_ADOPTION_ANIMAL);
                fragment.setArguments(bundle);
                setTitle("Home");
                break;

            case R.id.navSearchAdoption:
                fragment = new ListAnimalsFragment();
                bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_GENERAL_LIST);
                bundle.putString(RockChisel.ANIMAL_TYPE, RockChisel.SCENARIO_ADOPTION_ANIMAL);
                fragment.setArguments(bundle);
                setTitle(menuItem.getTitle());
                break;

            case R.id.navSearchMissing:
                fragment = new ListAnimalsFragment();
                bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_GENERAL_LIST);
                bundle.putString(RockChisel.ANIMAL_TYPE, RockChisel.SCENARIO_MISSING_ANIMAL);
                fragment.setArguments(bundle);
                setTitle(menuItem.getTitle());
                break;

            case R.id.navSearchFound:
                fragment = new ListAnimalsFragment();
                bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_GENERAL_LIST);
                bundle.putString(RockChisel.ANIMAL_TYPE, RockChisel.SCENARIO_FOUND_ANIMAL);
                fragment.setArguments(bundle);
                setTitle(menuItem.getTitle());
                break;

            case R.id.navSearchAssociations:
                fragment = new ListOrganizationsFragment();
                setTitle(menuItem.getTitle());
                break;

            case R.id.navPostWanderingAnimal:

                if(FortuneTeller.isInternetConnection(getApplicationContext())){

                    if(FortuneTeller.isLoggedUser(getApplicationContext())){
                        intent = new Intent(getApplicationContext(), PostAnimalActivity.class);
                        intent.putExtra(PostAnimalActivity.ANIMAL_TYPE, RockChisel.FOUND_ANIMAL);
                        intent.putExtra(PostAnimalActivity.ACTION, RockChisel.ACTION_CREATE);
                    } else{
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    }

                } else {
                    Toast.makeText(this, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.navPostLostAnimal:

                if(FortuneTeller.isInternetConnection(getApplicationContext())){

                    if(FortuneTeller.isLoggedUser(getApplicationContext())){
                        intent = new Intent(getApplicationContext(), PostAnimalActivity.class);
                        intent.putExtra(PostAnimalActivity.ANIMAL_TYPE, RockChisel.MISSING_ANIMAL);
                        intent.putExtra(PostAnimalActivity.ACTION, RockChisel.ACTION_CREATE);
                    } else{
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    }

                } else {
                    Toast.makeText(this, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.navMyAnimals:

                if(FortuneTeller.isInternetConnection(getApplicationContext())){

                    if(FortuneTeller.isLoggedUser(getApplicationContext())){
                        fragment = new ListAnimalsFragment();
                        bundle.putString(RockChisel.SCENARIO, RockChisel.SCENARIO_MY_LIST);
                        fragment.setArguments(bundle);
                        setTitle(menuItem.getTitle());
                    } else {
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    }

                } else {
                    Toast.makeText(this, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.navLogin:

                if(FortuneTeller.isInternetConnection(getApplicationContext())) {

                    if (FortuneTeller.isLoggedUser(getApplicationContext())) {
                        Vault.clearPreferences(getApplicationContext(), RockChisel.USER_PREFERENCES);
                    } else {
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    }
                } else {
                    Toast.makeText(this, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
                }

                break;


            default:
                //lista de ultimos animais publicados
        }

        if (fragment != null)
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        if(intent != null)
            startActivity(intent);


        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void networkAvailable() {
        Log.d("Internet Status", "---> Habemus INTERNET!");
        String loginAwareness = getString(R.string.user_prefix) + Vault.getLoggedUser(getApplicationContext());

        if(FortuneTeller.isLoggedUser(getApplicationContext())){
            usernameDisplay.setText(loginAwareness);
            navLoginItem.setTitle(R.string.logout);
        } else {
            usernameDisplay.setText(R.string.empty);
            navLoginItem.setTitle(R.string.login);
        }
    }

    @Override
    public void networkUnavailable() {
        Log.d("Internet Status", "---> Não temos net!");
        usernameDisplay.setText(R.string.internet_connection_down);
    }
}