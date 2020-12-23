package amsi.dei.estg.ipleiria.paws4adoption.views;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.paws4adoption.MainFragment;
import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.utils.FortuneTeller;
import amsi.dei.estg.ipleiria.paws4adoption.utils.RockChisel;
import amsi.dei.estg.ipleiria.paws4adoption.utils.Vault;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;

    /**
     * On create state of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Fragment fragment = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                app.sendScreenView("Menu");
                setScenario();

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

        //setScenario();
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

        MenuItem previousSelectedItem = navigationView.getCheckedItem();

        if(previousSelectedItem != null )
            previousSelectedItem.setChecked(false);

        menuItem.setChecked(true);

        switch (menuItem .getItemId()) {
            case R.id.navHome:
                fragment = new MainFragment();
                setTitle("Home");
                break;
            case R.id.navSearchAnimals:
                fragment = new ListAdoptionAnimalsFragment();
                setTitle(menuItem.getTitle());
                break;
            case R.id.navSearchAssociations:
                fragment = new ListOrganizationsFragment();
                setTitle(menuItem.getTitle());
                break;
            case R.id.navMyAnimals:
                break;
            case R.id.navPostWanderingAnimal:
                intent = new Intent(getApplicationContext(), PostAnimalActivity.class);
                break;
            case R.id.navPostLostAnimal:
                break;
            case R.id.navLogin:
                if(FortuneTeller.isThereLoggedUser(getApplicationContext())){
                    Vault.clearPreferences(getApplicationContext(), RockChisel.USER_PREFERENCES);
                } else{
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
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

    /**
     * Method that implements changes to the current activity according the internet state (availabe/or not) and user (guest/logged)
     */
    public void setScenario(){

        Menu menu = navigationView.getMenu();
        View hview = navigationView.getHeaderView(0);

        TextView usernameDisplay = hview.findViewById(R.id.tvUsername);
        MenuItem navLoginItem = menu.findItem(R.id.navLogin);

        if(FortuneTeller.isThereInternetConnection(getApplicationContext())){

            if(FortuneTeller.isThereLoggedUser(getApplicationContext())){

                String loginAwareness = getString(R.string.user_prefix) + Vault.getLoggedUser(getApplicationContext());

                usernameDisplay.setText(loginAwareness);
                navLoginItem.setTitle(R.string.logout);
            } else {
                usernameDisplay.setText(R.string.empty);
                navLoginItem.setTitle(R.string.login);
            }
        }
        else{
            usernameDisplay.setText(R.string.internet_connection_down);
            navLoginItem.setVisible(false);
        }

    }

}