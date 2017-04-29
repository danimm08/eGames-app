package es.egames.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.ImagePersonalGameFragment;
import es.egames.fragments.MyGameDetailsFormRecyclerViewAdapter;
import es.egames.model.PersonalGame;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

import static java.security.AccessController.getContext;

public class PersonalGameDetailsActivity extends AppCompatActivity implements ImagePersonalGameFragment.OnListFragmentInteractionListener {

    private User principal;
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
        RequestForPrincipalDetailsTask requestForPrincipalDetailsTask = new RequestForPrincipalDetailsTask();
        try {
            principal = requestForPrincipalDetailsTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            principal = null;
        }
        personalGame = (PersonalGame) getIntent().getSerializableExtra("personalgame");
        gameDetailsForm = (GameDetailsForm) getIntent().getSerializableExtra("game");

        setTitle(gameDetailsForm.getTitle());

        mImageView = (ImageView) findViewById(R.id.user_image);
        mTitle = (TextView) findViewById(R.id.user_name);
        mPlatform = (TextView) findViewById(R.id.exchange_created_on_message);
        mGenres = (TextView) findViewById(R.id.user_exchanges);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.make_exchange_button);
        if (principal.equals(personalGame.getUser())) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }
            });
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CreateExchangeActivity.class);
                    intent.putExtra("personalgame", personalGame);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {

                if (data.getData() != null) {

                    Uri uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    File file = convertToFile(bitmap);

                    String url = RestTemplateManager.getUrl(getApplicationContext(), "personalgame/upload?personalGameId=" + personalGame.getId());
                    Ion.with(getApplicationContext())
                            .load(url)
                            .setHeader("Authorization", "Bearer " + RestTemplateManager.getToken(getApplicationContext()))
                            .setMultipartFile("image", file).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result == null) {
                                Intent intent = new Intent(getApplicationContext(), PersonalGameDetailsActivity.class);
                                intent.putExtra("game", gameDetailsForm);
                                RequestForPersonalGameDetails requestForPersonalGameDetails = new RequestForPersonalGameDetails();
                                PersonalGame newPersonalGame;
                                try {
                                    newPersonalGame = requestForPersonalGameDetails.execute().get();
                                } catch (InterruptedException | ExecutionException e1) {
                                    newPersonalGame = personalGame;
                                }
                                intent.putExtra("personalgame", newPersonalGame);
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_upload_image), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        } catch (Exception oops) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_upload_image), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onListFragmentInteraction(Bitmap image) {
    }

    public File convertToFile(Bitmap bitmapToConvert) throws IOException {
        File f = new File(getApplicationContext().getCacheDir(), gameDetailsForm.getTitle() + ".png");
        f.createNewFile();

        Bitmap bitmap = bitmapToConvert;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;
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

    public class RequestForPersonalGameDetails extends AsyncTask<Void, Void, PersonalGame> {

        @Override
        protected PersonalGame doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getApplicationContext());
            String url = RestTemplateManager.getUrl(getApplicationContext(), "personalgame/details?personalGameId=" + personalGame.getId());

            ResponseEntity<PersonalGame> responseEntity;
            PersonalGame res = null;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, PersonalGame.class);
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
