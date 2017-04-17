package es.egames.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import es.egames.R;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.ImagePersonalGameFragment;
import es.egames.fragments.MyGameDetailsFormRecyclerViewAdapter;
import es.egames.model.Image;
import es.egames.model.PersonalGame;

public class PersonalGameDetailsActivity extends AppCompatActivity implements ImagePersonalGameFragment.OnListFragmentInteractionListener {

    public PersonalGame personalGame;
    public GameDetailsForm gameDetailsForm;
    public ImageView mImageView;
    public TextView mTitle;
    public TextView mPlatform;
    public TextView mGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_game_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        personalGame = (PersonalGame) getIntent().getSerializableExtra("personalgame");
        gameDetailsForm = (GameDetailsForm) getIntent().getSerializableExtra("game");

        setTitle(gameDetailsForm.getTitle());

        mImageView = (ImageView) findViewById(R.id.card_image);
        mTitle = (TextView) findViewById(R.id.card_title);
        mPlatform = (TextView) findViewById(R.id.card_platform);
        mGenres = (TextView) findViewById(R.id.card_genres);

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

    }

    @Override
    public void onListFragmentInteraction(Bitmap image) {

    }
}
