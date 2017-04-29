package es.egames.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.ChatFragment;
import es.egames.fragments.ExchangeFragment;
import es.egames.fragments.GameDetailsFormList;
import es.egames.fragments.GameTabsFragment;
import es.egames.fragments.MyChatRecyclerViewAdapter;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GameDetailsFormList.OnListFragmentInteractionListener, ExchangeFragment.OnListFragmentInteractionListener, ChatFragment.OnListFragmentInteractionListener {

    private Fragment gameTabsFragment;
    private Fragment myExchangesFragment;
    private Fragment chatsFragment;
    private SearchView searchView;
    private User principal;
    private ImageView mImageView;
    private TextView mNameView;
    private TextView mUsername;

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

//        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);


        View hView =  navigationView.getHeaderView(0);

        mImageView = (ImageView) hView.findViewById(R.id.header_image);
        mNameView = (TextView) hView.findViewById(R.id.header_name);
        mUsername = (TextView) hView.findViewById(R.id.header_username);

        modifyInfo();

        redirect();
    }

    private void modifyInfo() {
        RequestForPrincipalDetailsTask requestForPrincipalDetailsTask = new RequestForPrincipalDetailsTask();
        try {
            principal = requestForPrincipalDetailsTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            principal = null;
        }

        mNameView.setText(principal.getName() + " " + principal.getSurname());
        mUsername.setText(principal.getUserAccount().getUsername());
        RequestForImageTask requestForImageTask = new RequestForImageTask();
        try {
            Bitmap image;
            image = requestForImageTask.execute(principal.getProfilePicture()).get();
            if (image != null) {
                mImageView.setImageBitmap(image);
            } else {
                mImageView.setImageResource(R.drawable.default_image);
            }

        } catch (InterruptedException | ExecutionException e) {
            mImageView.setImageResource(R.drawable.default_image);
        }

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
            } else if (shortClassName.equals("CreatePersonalGameActivity")) {
                //TODO: Llevar a detalles personales del usuario logueado
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
        } else if (id == R.id.chats) {
            if (chatsFragment == null) {
                chatsFragment = new ChatFragment();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, chatsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.my_profile) {
            if (principal == null) {
                RequestForPrincipalDetailsTask requestForPrincipalDetailsTask = new RequestForPrincipalDetailsTask();
                try {
                    principal = requestForPrincipalDetailsTask.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    principal = null;
                }
            }
            Intent intent = new Intent(getApplicationContext(), DetailsUserActivity.class);
            intent.putExtra("user", principal);
            startActivity(intent);
        } else if (id == R.id.logout) {
            RequestForLogout requestForLogout = new RequestForLogout();
            requestForLogout.execute();
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

    @Override
    public void onListFragmentInteraction(User item) {
        //TODO: Llevar a chat
        Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_LONG).show();
    }

    public class RequestForPrincipalDetailsTask extends AsyncTask<Void, Void, User> {


        public RequestForPrincipalDetailsTask() {
            super();
        }

        @Override
        protected User doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getApplicationContext());
            String url = RestTemplateManager.getUrl(getApplicationContext(), "user/principal");
            User u = null;
            ResponseEntity<User> responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, User.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    u = responseEntity.getBody();
                }
            } catch (Exception oops) {
                u = null;
            }
            return u;
        }
    }

    public class RequestForLogout extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getApplicationContext());
            String url = RestTemplateManager.getUrl(getApplicationContext(), "oauth/logout");
            ResponseEntity<Void> responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, Void.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    RestTemplateManager.logout(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            } catch (Exception oops) {
            }
            return null;
        }

    }

    public class RequestForImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = null;
            try {
                String auxUrl = RestTemplateManager.getUrl(getApplicationContext(), "image/download?filename=" + URLEncoder.encode(params[0], "UTF-8").toString());
                URLConnection connection = RestTemplateManager.getConnection(getApplicationContext(), auxUrl);
                bmp = BitmapFactory.decodeStream(connection.getInputStream());
            } catch (Exception e) {
            }
            return bmp;
        }
    }
}
