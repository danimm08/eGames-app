package es.egames.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import es.egames.R;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.DetailsOfGameActivityFragment;
import es.egames.fragments.MyGameDetailsFormRecyclerViewAdapter;
import es.egames.model.PersonalGame;

public class DetailsOfGameActivity extends AppCompatActivity implements DetailsOfGameActivityFragment.OnListFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    public ImageView mImageView;
    public TextView mTitle;
    public TextView mPlatform;
    public TextView mGenres;
    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GameDetailsForm gameDetailsForm = (GameDetailsForm) getIntent().getSerializableExtra("game");

        setTitle(gameDetailsForm.getTitle());

        mImageView = (ImageView) findViewById(R.id.card_image);
        mTitle = (TextView) findViewById(R.id.card_title);
        mPlatform = (TextView) findViewById(R.id.card_platform);
        mGenres = (TextView) findViewById(R.id.card_genres);
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
            mImageView.setImageResource(R.drawable.game_default);
        }
        mTitle.setText(gameDetailsForm.getTitle());
        mPlatform.setText(gameDetailsForm.getPlatform().getName());
        mGenres.setText(gameDetailsForm.getGenres().toString().replace("[", "").replace("]", ""));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orderByElements, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


    }

    @Override
    public void onListFragmentInteraction(PersonalGame item) {

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
