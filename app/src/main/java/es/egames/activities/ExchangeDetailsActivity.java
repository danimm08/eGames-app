package es.egames.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.fragments.ExchangeGameFragment;
import es.egames.fragments.NoteFragment;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class ExchangeDetailsActivity extends AppCompatActivity {

    private DetailsOfExchangeForm detailsOfExchangeForm;
    public User principal;
    private TextView mDate;
    private TextView mUpdate;
    private Button mState;
    private TextView mEvent;
    private TextView mNum;
    private TextView mType;
    private TextView mWayExchange;
    private Button mAccept;
    private Button mDenny;
    private Button mNegotiate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        detailsOfExchangeForm = (DetailsOfExchangeForm) getIntent().getSerializableExtra("detailsOfExchangeForm");
        RequestForPrincipalDetailsTask requestForPrincipalDetailsTask = new RequestForPrincipalDetailsTask();
        try {
            principal = requestForPrincipalDetailsTask.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            principal = null;
        }

        mDate = (TextView) findViewById(R.id.exchange_date);
        mUpdate = (TextView) findViewById(R.id.exchange_update);
        mState = (Button) findViewById(R.id.exchange_state);
        mEvent = (TextView) findViewById(R.id.exchange_event);
        mNum = (TextView) findViewById(R.id.exchange_num);
        mType = (TextView) findViewById(R.id.exchange_type);
        mWayExchange = (TextView) findViewById(R.id.exchange_way_exchange);

        mAccept = (Button) findViewById(R.id.exchange_accept);
        mDenny = (Button) findViewById(R.id.exchange_denny);
        mNegotiate = (Button) findViewById(R.id.exchange_negotiate);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        mDate.setText(dateFormat.format(detailsOfExchangeForm.getCreationDate()));
        mUpdate.setText(dateFormat.format(detailsOfExchangeForm.getLastUpdateDate()));

        if (detailsOfExchangeForm.getStatus() == null) {
            mState.setText(R.string.pending);
            mState.setTextColor(getResources().getColor(R.color.holo_orange_dark));
        } else if (detailsOfExchangeForm.getStatus()) {
            mState.setText(R.string.accepted);
            mState.setTextColor(getResources().getColor(R.color.holo_green_dark));
            mAccept.setVisibility(View.INVISIBLE);
            mNegotiate.setVisibility(View.INVISIBLE);
            mDenny.setVisibility(View.INVISIBLE);
        } else if (detailsOfExchangeForm.getStatus() == false) {
            mState.setText(R.string.denied);
            mState.setTextColor(getResources().getColor(R.color.holo_red_dark));
            mAccept.setVisibility(View.INVISIBLE);
            mNegotiate.setVisibility(View.INVISIBLE);
            mDenny.setVisibility(View.INVISIBLE);
        }

        if (detailsOfExchangeForm.getEventDate() != null) {
            mEvent.setText(getString(R.string.on) + " " + dateFormat.format(detailsOfExchangeForm.getEventDate()));
        }

        mNum.setText(detailsOfExchangeForm.getNumberOfAttemps().toString());
        mType.setText(detailsOfExchangeForm.getType().toString());
        mWayExchange.setText(detailsOfExchangeForm.getWayExchange());

        if (detailsOfExchangeForm.getLastModifier().equals(principal)) {
            mAccept.setVisibility(View.INVISIBLE);
        }

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lanzar petición aceptar
            }
        });

        mDenny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lanzar petición aceptar
            }
        });

        mNegotiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Lanzar actividad negociar
            }
        });

        NoteFragment fragment = new NoteFragment();
        ExchangeGameFragment fragment1 = ExchangeGameFragment.newInstance(new ArrayList(detailsOfExchangeForm.getPersonalGameUser1()));
        ExchangeGameFragment fragment2 = ExchangeGameFragment.newInstance(new ArrayList(detailsOfExchangeForm.getPersonalGameUser2()));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_exchange_details_notes, fragment)
                .replace(R.id.content_exchange_details_my_games, fragment1)
                .replace(R.id.content_exchange_details_their_games, fragment2).commit();

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

}
