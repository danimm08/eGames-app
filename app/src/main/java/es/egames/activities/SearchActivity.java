package es.egames.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.adapters.TabsAdapter;
import es.egames.forms.GameDetailsForm;
import es.egames.forms.SoughtItem;
import es.egames.fragments.GameDetailsFormList;
import es.egames.fragments.SoughtItemFragment;
import es.egames.model.Game;
import es.egames.model.PersonalGame;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class SearchActivity extends AppCompatActivity implements SoughtItemFragment.OnListFragmentInteractionListener {

    public TabsAdapter adapter;
    private String toSearch;
    private Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_search);
        toSearch = getIntent().getStringExtra("toSearch");
        setTitle(getString(R.string.ressults_of_search) + " " + toSearch);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new TabsAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(SoughtItemFragment.newInstance("game", toSearch), getString(R.string.games));
        adapter.addFragment(SoughtItemFragment.newInstance("personalgame", toSearch), getString(R.string.personal_games));
        adapter.addFragment(SoughtItemFragment.newInstance("user", toSearch), getString(R.string.users));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onListFragmentInteraction(SoughtItem item) {
        if (item.getObject() instanceof Game) {
            GameDetailsForm res;
            Game g = (Game) item.getObject();
            RequestForGameDetailsTask task = new RequestForGameDetailsTask();
            try {
                res = task.execute(g.getId()).get();
                Intent intent = new Intent(instance, DetailsOfGameActivity.class);
                intent.putExtra("game", res);
                startActivity(intent);
            } catch (InterruptedException | ExecutionException e) {
                Toast.makeText(getApplicationContext(), R.string.error_general, Toast.LENGTH_LONG).show();
            }
        } else if (item.getObject() instanceof PersonalGame) {
            GameDetailsForm gameDetailsForm;
            PersonalGame pg = (PersonalGame) item.getObject();
            Game g =  pg.getGame();
            RequestForGameDetailsTask task = new RequestForGameDetailsTask();
            try {
                gameDetailsForm = task.execute(g.getId()).get();
                Intent intent = new Intent(instance, PersonalGameDetailsActivity.class);
                intent.putExtra("game", gameDetailsForm);
                intent.putExtra("personalgame", pg);
                startActivity(intent);
            } catch (InterruptedException | ExecutionException e) {
                Toast.makeText(getApplicationContext(), R.string.error_general, Toast.LENGTH_LONG).show();
            }
        } else if (item.getObject() instanceof User) {

        }
        Toast.makeText(getApplicationContext(), "Clicado", Toast.LENGTH_LONG).show();
    }

    public class RequestForGameDetailsTask extends AsyncTask<Integer, Void, GameDetailsForm> {

        public RequestForGameDetailsTask() {
            super();
        }

        @Override
        protected GameDetailsForm doInBackground(Integer... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(instance);
            String url = RestTemplateManager.getUrl(instance, "game/details?gameId=" + params[0]);

            ResponseEntity<GameDetailsForm> responseEntity;
            GameDetailsForm res = null;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, GameDetailsForm.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    res = responseEntity.getBody();
                }
            } catch (Exception oops) {
                res = null;
            }
            return res;
        }

    }
}
