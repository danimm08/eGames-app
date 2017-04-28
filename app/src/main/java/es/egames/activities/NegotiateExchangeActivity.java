package es.egames.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.forms.ExchangeForm;
import es.egames.model.Note;
import es.egames.model.PersonalGame;
import es.egames.model.Type;
import es.egames.utils.RestTemplateManager;
import es.egames.widgets.MultiSelectionSpinner;

public class NegotiateExchangeActivity extends AppCompatActivity {

    private MultiSelectionSpinner mMyGames;
    private MultiSelectionSpinner mTheirGames;
    private Spinner mType;
    private EditText mWayExchange;
    private EditText mNote;
    private Button mMakeExchangeButton;

    private NegotiateExchangeActivity instance;
    private ExchangeForm exchangeFormWithAvailablesPersonalGames;
    private ExchangeForm currentExchange;
    private DetailsOfExchangeForm detailsOfExchangeForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negotiate_exchange);
        instance = this;
        detailsOfExchangeForm = (DetailsOfExchangeForm) getIntent().getSerializableExtra("detailsOfExchangeForm");

        mMyGames = (MultiSelectionSpinner) findViewById(R.id.spinner_my_games);
        mTheirGames = (MultiSelectionSpinner) findViewById(R.id.spinner_their_games);
        mType = (Spinner) findViewById(R.id.spinner_type);
        mWayExchange = (EditText) findViewById(R.id.exchange_way_exchange);
        mNote = (EditText) findViewById(R.id.exchange_note);

        loadData();

        mMakeExchangeButton = (Button) findViewById(R.id.exchange_submit_exchange);
        mMakeExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForNegotiateExchange();
            }
        });
    }

    private void loadData() {
        RequestForCreateNegotiation createExchangeTask = new RequestForCreateNegotiation();
        try {
            List<PersonalGame> personalGames = new ArrayList(detailsOfExchangeForm.getPersonalGameUser2());
            PersonalGame auxPersonalGame = personalGames.get(0);
            exchangeFormWithAvailablesPersonalGames = createExchangeTask.execute(auxPersonalGame).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        RequestForGetExchangeFormInfo requestForGetExchangeFormInfo = new RequestForGetExchangeFormInfo();
        try {
            currentExchange = requestForGetExchangeFormInfo.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            currentExchange = null;
        }

        List<String> myPersonalGamesToString = new ArrayList<>();
        for (PersonalGame pg : exchangeFormWithAvailablesPersonalGames.getPersonalGamesUser1()) {
            myPersonalGamesToString.add(pg.getGame().getTitle() + " " + "(" + pg.getGame().getPlatform().getName() + ")");
        }
        List<String> theirPersonalGamesToString = new ArrayList<>();
        for (PersonalGame pg : exchangeFormWithAvailablesPersonalGames.getPersonalGamesUser2()) {
            theirPersonalGamesToString.add(pg.getGame().getTitle() + " " + "(" + pg.getGame().getPlatform().getName() + ")");
        }

        if (myPersonalGamesToString.size() > 0) {
            mMyGames.setItems(myPersonalGamesToString);
        }
        if (theirPersonalGamesToString.size() > 0) {
            mTheirGames.setItems(theirPersonalGamesToString);
        }

        List<String> selectedIndicesMyGames = new ArrayList<>();
        for (PersonalGame pg : currentExchange.getPersonalGamesUser1()) {
            if (myPersonalGamesToString.contains(pg.getGame().getTitle() + " " + "(" + pg.getGame().getPlatform().getName() + ")")) {
                selectedIndicesMyGames.add(pg.getGame().getTitle() + " " + "(" + pg.getGame().getPlatform().getName() + ")");
            }
        }
        mMyGames.setSelection(selectedIndicesMyGames);

        List<String> selectedIndicesTheirGames = new ArrayList<>();
        for (PersonalGame pg : currentExchange.getPersonalGamesUser2()) {
            if (theirPersonalGamesToString.contains(pg.getGame().getTitle() + " " + "(" + pg.getGame().getPlatform().getName() + ")")) {
                selectedIndicesTheirGames.add(pg.getGame().getTitle() + " " + "(" + pg.getGame().getPlatform().getName() + ")");
            }
        }
        mTheirGames.setSelection(selectedIndicesTheirGames);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapter);

        if (currentExchange.getType().equals(Type.Fijo)) {
            mType.setSelection(0);
        } else {
            mType.setSelection(1);
        }

        mWayExchange.setText(currentExchange.getWayExchange());

    }

    private void requestForNegotiateExchange() {

        List<PersonalGame> personalGameUser1 = new ArrayList<>();
        for (Integer i : mMyGames.getSelectedIndicies()) {
            personalGameUser1.add(exchangeFormWithAvailablesPersonalGames.getPersonalGamesUser1().get(i));
        }
        List<PersonalGame> personalGameUser2 = new ArrayList<>();
        for (Integer i : mTheirGames.getSelectedIndicies()) {
            personalGameUser2.add(exchangeFormWithAvailablesPersonalGames.getPersonalGamesUser2().get(i));
        }
        String auxType = (String) mType.getSelectedItem();
        Type type = auxType.equals("Fijo") ? Type.Fijo : Type.Temporal;

        String wayExchange = mWayExchange.getText().toString();
        Note note;
        if (mNote.getText().toString().isEmpty()) {
            note = null;
        } else {
            note = new Note();
            note.setText(mNote.getText().toString());
        }

        ExchangeForm exchangeForm = new ExchangeForm(personalGameUser1, personalGameUser2, type, wayExchange, note);

        RequestForExchange requestForExchange = new RequestForExchange();
        requestForExchange.execute(exchangeForm);


    }

    public class RequestForCreateNegotiation extends AsyncTask<PersonalGame, Void, ExchangeForm> {

        @Override
        protected ExchangeForm doInBackground(PersonalGame... params) {
            ExchangeForm res = null;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(instance);
            String url = RestTemplateManager.getUrl(instance, "exchange/createNegotiation?exchangeId=" + detailsOfExchangeForm.getId());
            try {
                ResponseEntity<ExchangeForm> responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, ExchangeForm.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    res = responseEntity.getBody();
                } else {
                    res = null;
                }
            } catch (Exception e) {
                res = null;
            }
            return res;
        }
    }

    public class RequestForGetExchangeFormInfo extends AsyncTask<PersonalGame, Void, ExchangeForm> {

        @Override
        protected ExchangeForm doInBackground(PersonalGame... params) {
            ExchangeForm res = null;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(instance);
            String url = RestTemplateManager.getUrl(instance, "exchange/getExchangeFormInfo?exchangeId=" + detailsOfExchangeForm.getId());
            try {
                ResponseEntity<ExchangeForm> responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, ExchangeForm.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    res = responseEntity.getBody();
                } else {
                    res = null;
                }
            } catch (Exception e) {
                res = null;
            }
            return res;
        }
    }

    public class RequestForExchange extends AsyncTask<ExchangeForm, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ExchangeForm... params) {
            Boolean hasErrors;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity httpEntity = RestTemplateManager.authenticateRequestWithObject(instance, params[0]);
            String url = RestTemplateManager.getUrl(instance, "exchange/negotiate?exchangeId=" + detailsOfExchangeForm.getId());
            try {
                ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    hasErrors = false;
                } else {
                    hasErrors = true;
                }
            } catch (Exception e) {
                hasErrors = true;
            }
            return hasErrors;
        }

        @Override
        protected void onPostExecute(Boolean hasErrors) {
            if (hasErrors) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_general, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        }

    }
}
