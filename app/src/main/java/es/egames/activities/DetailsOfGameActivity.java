package es.egames.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.DetailsOfGameActivityFragment;
import es.egames.fragments.MyGameDetailsFormRecyclerViewAdapter;
import es.egames.model.PersonalGame;
import es.egames.model.User;

public class DetailsOfGameActivity extends AppCompatActivity implements DetailsOfGameActivityFragment.OnListFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    public ImageView mImageView;
    public TextView mTitle;
    public TextView mPlatform;
    public TextView mGenres;
    public Spinner spinner;
    public GameDetailsForm gameDetailsForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameDetailsForm = (GameDetailsForm) getIntent().getSerializableExtra("game");
        setTitle(gameDetailsForm.getTitle());

        mImageView = (ImageView) findViewById(R.id.user_image);
        mTitle = (TextView) findViewById(R.id.user_name);
        mPlatform = (TextView) findViewById(R.id.exchange_created_on_message);
        mGenres = (TextView) findViewById(R.id.user_exchanges);
        spinner = (Spinner) findViewById(R.id.card_personalgames_orderBy);

        MyGameDetailsFormRecyclerViewAdapter.RequestForImageTask task = new MyGameDetailsFormRecyclerViewAdapter.RequestForImageTask();
        Bitmap bitmap;
        try {
            bitmap = task.execute(gameDetailsForm.getCoverUrl().replace("t_thumb", "t_cover_big")).get();
        } catch (Exception e) {
            bitmap = null;
        }

        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageResource(R.drawable.default_image);
        }
        mTitle.setText(gameDetailsForm.getTitle());
        mPlatform.setText(gameDetailsForm.getPlatform().getName());
        mGenres.setText(gameDetailsForm.getGenres().toString().replace("[", "").replace("]", ""));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orderByElements, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_personal_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreatePersonalGameActivity.class);
                intent.putExtra("gameDetailsForm", gameDetailsForm);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onListFragmentInteraction(PersonalGame personalGame) {
        Intent intent = new Intent(this, PersonalGameDetailsActivity.class);
        intent.putExtra("game", gameDetailsForm);
        intent.putExtra("personalgame", personalGame);
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String option = parent.getItemAtPosition(pos).toString().toLowerCase();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frament_to_replace, DetailsOfGameActivityFragment.newInstance(option));
        transaction.commit();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
