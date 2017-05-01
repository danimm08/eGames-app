package es.egames.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.GameDetailsForm;
import es.egames.forms.SoughtItem;
import es.egames.fragments.SoughtItemFragment;
import es.egames.model.Game;
import es.egames.model.PersonalGame;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class DetailsUserActivity extends AppCompatActivity implements SoughtItemFragment.OnListFragmentInteractionListener {

    private User principal;
    private User auxUser;
    public User user;
    public static DetailsUserActivity instance;
    private ImageView userImage;
    private TextView userName;
    private TextView userUserName;
    private RatingBar userRating;
    private TextView userExchanges;
    private Button userAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        RequestForPrincipalDetailsTask task1 = new RequestForPrincipalDetailsTask();
        try {
            principal = task1.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            principal = null;
        }

        auxUser = (User) getIntent().getSerializableExtra("user");
        RequestForUserDetailsTask task = new RequestForUserDetailsTask();
        try {
            user = task.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            user = null;
        }

        setContentView(R.layout.activity_user_details);
        setTitle(user.getUserAccount().getUsername());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userImage = (ImageView) findViewById(R.id.user_image);
        userName = (TextView) findViewById(R.id.user_name);
        userUserName = (TextView) findViewById(R.id.exchange_created_on_message);
        userRating = (RatingBar) findViewById(R.id.user_rating);
        userExchanges = (TextView) findViewById(R.id.user_exchanges);
        userAction = (Button) findViewById(R.id.user_action_button);

        Bitmap image;
        RequestForImageTask requestForImageTask = new RequestForImageTask();
        try {
            image = requestForImageTask.execute(user.getProfilePicture()).get();
            if (image != null) {
                userImage.setImageBitmap(image);
            } else {
                userImage.setImageResource(R.drawable.default_image);
            }

        } catch (InterruptedException | ExecutionException e) {
            userImage.setImageResource(R.drawable.default_image);
        }

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        userName.setText(user.getName() + " " + user.getSurname());
        userUserName.setText(user.getUserAccount().getUsername());
        Double auxRating = (user.getReputation() * 5) / 10;
        Long rating = Math.round(auxRating);
        userRating.setRating(rating);
        userExchanges.setText(user.getnExchanges() + " " + getString(R.string.exchanges));

        View.OnClickListener followOrUnfollow = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestForFollow requestForFollow = new RequestForFollow();
                Boolean hasErrors;
                try {
                    hasErrors = requestForFollow.execute(user).get();
                } catch (InterruptedException | ExecutionException e) {
                    hasErrors = true;
                }
                if (hasErrors) {
                    Toast.makeText(getApplicationContext(), R.string.error_follow, Toast.LENGTH_LONG);
                } else {
                    if (userAction.getText().toString().equals(getString(R.string.follow))) {
                        userAction.setText(R.string.unfollow);
                    } else {
                        userAction.setText(R.string.follow);
                    }
                }
            }
        };

        if (principal.getId() == user.getId()) {
            userAction.setText(R.string.edit_info);
            userAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ModifyPersonalData.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });

        } else if (principal.getFollowees().contains(user)) {
            userAction.setText(R.string.unfollow);
            userAction.setOnClickListener(followOrUnfollow);
        } else if (!principal.getFollowees().contains(user)) {
            userAction.setText(R.string.follow);
            userAction.setOnClickListener(followOrUnfollow);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (principal.getId() == user.getId()) {
            fab.setVisibility(View.GONE);
        } else {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CreateMessgeActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onListFragmentInteraction(SoughtItem item) {
        if (item.getObject() instanceof PersonalGame) {
            GameDetailsForm gameDetailsForm;
            PersonalGame pg = (PersonalGame) item.getObject();
            Game g = pg.getGame();
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
            Intent intent = new Intent(this, DetailsUserActivity.class);
            intent.putExtra("user", (User) item.getObject());
            startActivity(intent);
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

                    String url = RestTemplateManager.getUrl(getApplicationContext(), "user/profile_picture");
                    Ion.with(getApplicationContext())
                            .load(url)
                            .setHeader("Authorization", "Bearer " + RestTemplateManager.getToken(getApplicationContext()))
                            .setMultipartFile("image", file).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (result == null) {
                                Intent intent = new Intent(getApplicationContext(), DetailsUserActivity.class);
                                intent.putExtra("user", user);
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

    public File convertToFile(Bitmap bitmapToConvert) throws IOException {
        File f = new File(getApplicationContext().getCacheDir(), user.getUserAccount().getUsername() + ".png");
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

    public class RequestForUserDetailsTask extends AsyncTask<Void, Void, User> {


        public RequestForUserDetailsTask() {
            super();
        }

        @Override
        protected User doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(instance);
            String url = RestTemplateManager.getUrl(instance, "user/details?userId=" + auxUser.getId());
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

    public class RequestForPrincipalDetailsTask extends AsyncTask<Void, Void, User> {


        public RequestForPrincipalDetailsTask() {
            super();
        }

        @Override
        protected User doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(instance);
            String url = RestTemplateManager.getUrl(instance, "user/principal");
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

    public class RequestForImageTask extends AsyncTask<String, Void, Bitmap> {

        public RequestForImageTask() {
            super();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = null;
            try {
                String auxUrl = RestTemplateManager.getUrl(instance, "image/download?filename=" + URLEncoder.encode(params[0], "UTF-8").toString());
                URLConnection connection = RestTemplateManager.getConnection(instance, auxUrl);
                bmp = BitmapFactory.decodeStream(connection.getInputStream());
            } catch (Exception e) {
            }
            return bmp;
        }
    }

    public class RequestForFollow extends AsyncTask<User, Void, Boolean> {


        public RequestForFollow() {
            super();
        }

        @Override
        protected Boolean doInBackground(User... params) {
            Boolean hasErrors = true;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(instance);
            String url = RestTemplateManager.getUrl(instance, "user/follow?userId=" + params[0].getId());
            User u = null;
            ResponseEntity responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, Object.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    hasErrors = false;
                }
            } catch (Exception oops) {
                hasErrors = true;
            }
            return hasErrors;
        }
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
