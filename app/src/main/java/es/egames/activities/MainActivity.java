package es.egames.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.egames.R;
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.ExchangeFragment;
import es.egames.fragments.GameDetailsFormList;
import es.egames.fragments.GameTabsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GameDetailsFormList.OnListFragmentInteractionListener, ExchangeFragment.OnListFragmentInteractionListener {

    private Fragment gameTabsFragment;
    private Fragment myExchangesFragment;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIsLoggedIn();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        gameTabsFragment = new GameTabsFragment();
        fragmentManager.beginTransaction().replace(R.id.content_main, gameTabsFragment).commit();

        redirect();
    }

    private void redirect() {
        ComponentName callingActivity = getCallingActivity();
        if (callingActivity != null) {
            String shortClassName = callingActivity.getShortClassName().replace(".activities.", "");
            if (shortClassName.equals("CreateExchangeActivity") || shortClassName.equals("QualifyExchangeActivity") || shortClassName.equals("NegotiateExchangeActivity")) {
                if (myExchangesFragment == null) {
                    myExchangesFragment = new ExchangeFragment();
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main, myExchangesFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        searchView.setIconified(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView) menu.findItem(R.id.lens).getActionView();
//        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("toSearch", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify game_default parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_games) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, gameTabsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.my_exchanges) {
            if (myExchangesFragment == null) {
                myExchangesFragment = new ExchangeFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, myExchangesFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new Fragment()).commit();
        } else if (id == R.id.nav_manage) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new Fragment()).commit();
        } else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new Fragment()).commit();
        } else if (id == R.id.nav_send) {
            fragmentManager.beginTransaction().replace(R.id.content_main, new Fragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkIsLoggedIn() {
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String access_token = sharedPref.getString("access_token", null);
        if (access_token == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onListFragmentInteraction(GameDetailsForm gameDetailsForm) {
        Intent intent = new Intent(this, DetailsOfGameActivity.class);
        intent.putExtra("game", gameDetailsForm);
        startActivity(intent);

    }

    @Override
    public void onListFragmentInteraction(DetailsOfExchangeForm item) {
        Intent intent = new Intent(this, DetailsExchangeActivity.class);
        intent.putExtra("detailsOfExchangeForm", item);
        startActivity(intent);
    }
}
