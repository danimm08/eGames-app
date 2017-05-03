package es.egames.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.fragments.ExchangeGameFragment;
import es.egames.fragments.NoteFragment;
import es.egames.model.PersonalGame;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class DetailsExchangeActivity extends AppCompatActivity {

    private DetailsOfExchangeForm detailsOfExchangeForm;
    public User principal;
    private TextView mDate;
    private TextView mUpdate;
    private Button mState;
    private TextView mEvent;
    private TextView mUsername;
    private TextView mNum;
    private TextView mType;
    private TextView mWayExchange;
    private Button mAccept;
    private Button mDenny;
    private Button mNegotiate;
    private Button mQualify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_details);
        setTitle(R.string.details_exchange);
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
        mUsername = (TextView) findViewById(R.id.exchange_username);
        mNum = (TextView) findViewById(R.id.exchange_num);
        mType = (TextView) findViewById(R.id.exchange_type);
        mWayExchange = (TextView) findViewById(R.id.exchange_way_exchange);

        mAccept = (Button) findViewById(R.id.exchange_accept);
        mDenny = (Button) findViewById(R.id.exchange_denny);
        mNegotiate = (Button) findViewById(R.id.exchange_negotiate);
        mQualify = (Button) findViewById(R.id.exchange_qualify);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        mDate.setText(dateFormat.format(detailsOfExchangeForm.getCreationDate()));
        mUpdate.setText(dateFormat.format(detailsOfExchangeForm.getLastUpdateDate()));


        if (detailsOfExchangeForm.getStatus() == null) {
            mState.setText(R.string.pending);
            mState.setTextColor(getResources().getColor(R.color.holo_orange_dark));
            mQualify.setVisibility(View.GONE);
        } else if (detailsOfExchangeForm.getStatus()) {
            mState.setText(R.string.accepted);
            mState.setTextColor(getResources().getColor(R.color.holo_green_dark));
            mAccept.setVisibility(View.GONE);
            mNegotiate.setVisibility(View.GONE);
            mDenny.setVisibility(View.GONE);
            RequesForCheckIsAllowedToQualify requesForCheckIsAllowedToQualify = new RequesForCheckIsAllowedToQualify();
            requesForCheckIsAllowedToQualify.execute();
        } else if (detailsOfExchangeForm.getStatus() == false) {
            mState.setText(R.string.denied);
            mState.setTextColor(getResources().getColor(R.color.holo_red_dark));
            mAccept.setVisibility(View.GONE);
            mNegotiate.setVisibility(View.GONE);
            mDenny.setVisibility(View.GONE);
            mQualify.setVisibility(View.GONE);
        }

        if (detailsOfExchangeForm.getEventDate() != null) {
            mEvent.setText(getString(R.string.on) + " " + dateFormat.format(detailsOfExchangeForm.getEventDate()));
        }

        List<PersonalGame> aux = new ArrayList<>(detailsOfExchangeForm.getPersonalGameUser2());
        mUsername.setText(aux.get(0).getUser().getUserAccount().getUsername());
        mNum.setText(detailsOfExchangeForm.getNumberOfAttemps().toString());
        mType.setText(detailsOfExchangeForm.getType().toString());
        if (detailsOfExchangeForm.getWayExchange() == null || detailsOfExchangeForm.getWayExchange().isEmpty()) {
            mWayExchange.setVisibility(View.GONE);
        } else {
            mWayExchange.setText(detailsOfExchangeForm.getWayExchange());
            mWayExchange.setVisibility(View.VISIBLE);
        }


        if (detailsOfExchangeForm.getLastModifier().equals(principal)) {
            mAccept.setVisibility(View.GONE);
        }

        final RequestForAcceptOrDenny requestForAcceptOrDenny = new RequestForAcceptOrDenny();
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForAcceptOrDenny.execute(true);
            }
        });

        mDenny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForAcceptOrDenny.execute(false);
            }
        });

        mNegotiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NegotiateExchangeActivity.class);
                intent.putExtra("detailsOfExchangeForm", detailsOfExchangeForm);
                startActivity(intent);
            }
        });

        mQualify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QualifyExchangeActivity.class);
                intent.putExtra("detailsOfExchangeForm", detailsOfExchangeForm);
                startActivity(intent);
            }
        });


        NoteFragment fragment = NoteFragment.newInstance(new ArrayList<>(detailsOfExchangeForm.getNotes()));
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

    public class RequestForAcceptOrDenny extends AsyncTask<Boolean, Void, Boolean> {

        public RequestForAcceptOrDenny() {
            super();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getApplicationContext());
            String url;
            if (params[0]) {
                url = RestTemplateManager.getUrl(getApplicationContext(), "exchange/accept?exchangeId=" + detailsOfExchangeForm.getId());
            } else {
                url = RestTemplateManager.getUrl(getApplicationContext(), "exchange/decline?exchangeId=" + detailsOfExchangeForm.getId());
            }
            Boolean isAccept = null;
            ResponseEntity<Boolean> responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, Boolean.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    if (params[0]) {
                        isAccept = true;
                    } else {
                        isAccept = false;
                    }

                }
            } catch (Exception oops) {
                isAccept = null;
            }
            return isAccept;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == null) {
                Toast.makeText(getApplicationContext(), R.string.error_general, Toast.LENGTH_LONG).show();
            } else if (aBoolean) {
                super.onPostExecute(aBoolean);
                mState.setText(R.string.accepted);
                mState.setTextColor(getResources().getColor(R.color.holo_green_dark));
                mAccept.setVisibility(View.GONE);
                mNegotiate.setVisibility(View.GONE);
                mDenny.setVisibility(View.GONE);
                mQualify.setVisibility(View.VISIBLE);
            } else if (!aBoolean) {
                mState.setText(R.string.denied);
                mState.setTextColor(getResources().getColor(R.color.holo_red_dark));
                mAccept.setVisibility(View.GONE);
                mNegotiate.setVisibility(View.GONE);
                mDenny.setVisibility(View.GONE);
            }
        }
    }

    public class RequesForCheckIsAllowedToQualify extends AsyncTask<Boolean, Void, Boolean> {

        public RequesForCheckIsAllowedToQualify() {
            super();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getApplicationContext());
            String url;
            url = RestTemplateManager.getUrl(getApplicationContext(), "qualification/checkIsAllowedToQualify?exchangeId=" + detailsOfExchangeForm.getId());

            Boolean res = false;
            ResponseEntity<Boolean> responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, Boolean.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    res = responseEntity.getBody();
                }
            } catch (Exception oops) {
                res = false;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                mQualify.setVisibility(View.VISIBLE);
            } else {
                mQualify.setVisibility(View.GONE);
            }
        }
    }

}
