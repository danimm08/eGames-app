package es.egames.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import es.egames.R;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.DetailsOfGameActivityFragment;
import es.egames.fragments.MyGameDetailsFormRecyclerViewAdapter;
import es.egames.fragments.dummy.DummyContent;
import es.egames.model.PersonalGame;

public class DetailsOfGameActivity extends AppCompatActivity implements DetailsOfGameActivityFragment.OnListFragmentInteractionListener {

    public ImageView mImageView;
    public TextView mTitle;
    public TextView mPlatform;
    public TextView mGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        GameDetailsForm gameDetailsForm = (GameDetailsForm) getIntent().getSerializableExtra("game");

        mImageView = (ImageView) findViewById(R.id.card_image);
        mTitle = (TextView) findViewById(R.id.card_title);
        mPlatform = (TextView) findViewById(R.id.card_platform);
        mGenres = (TextView) findViewById(R.id.card_genres);

        MyGameDetailsFormRecyclerViewAdapter.RequestForImageTask task = new MyGameDetailsFormRecyclerViewAdapter.RequestForImageTask();
        Bitmap bitmap;
        try {
            bitmap = task.execute(gameDetailsForm.getCoverUrl()).get();
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

    }

    @Override
    public void onListFragmentInteraction(PersonalGame item) {

    }
}
