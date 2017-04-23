package es.egames.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.ExchangeForm;
import es.egames.model.Exchange;
import es.egames.model.Note;
import es.egames.model.PersonalGame;
import es.egames.model.Type;
import es.egames.utils.RestTemplateManager;
import es.egames.widgets.MultiSelectionSpinner;

public class CreateExchangeActivity extends AppCompatActivity {

    private MultiSelectionSpinner mMyGames;
    private MultiSelectionSpinner mTheirGames;
    private Spinner mType;
    private EditText mWayExchange;
    private EditText mNote;
    private Button mMakeExchangeButton;
    private PersonalGame personalGame;
    private CreateExchangeActivity instance;
    private ExchangeForm aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        instance = this;
        personalGame = (PersonalGame) getIntent().getSerializableExtra("personalgame");

        mMyGames = (MultiSelectionSpinner) findViewById(R.id.spinner_my_games);
        mTheirGames = (MultiSelectionSpinner) findViewById(R.id.spinner_their_games);
        mType = (Spinner) findViewById(R.id.spinner_type);
        mWayExchange = (EditText) findViewById(R.id.exchange_way_exchange);
        mNote = (EditText) findViewById(R.id.exchange_note);

        loadSpinners();

        mMakeExchangeButton = (Button) findViewById(R.id.exchange_submit_exchange);
        mMakeExchangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForExchange();
            }
        });
    }

    private void loadSpinners() {
        RequestForCreateExchange createExchangeTask = new RequestForCreateExchange();
        try {
            aux = createExchangeTask.execute(personalGame).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<String> myPersonalGamesToString = new ArrayList<>();
        for (PersonalGame pg : aux.getPersonalGamesUser1()) {
            myPersonalGamesToString.add(pg.getGame().getTitle() + " " + pg.getGame().getPlatform());
        }
        List<String> theirPersonalGamesToString = new ArrayList<>();
        for (PersonalGame pg : aux.getPersonalGamesUser2()) {
            theirPersonalGamesToString.add(pg.getGame().getTitle() + " " + "(" + pg.getGame().getPlatform().getName() + ")");
        }
        if (myPersonalGamesToString.size() > 0) {
            mMyGames.setItems(myPersonalGamesToString);
            mMyGames.setSelection(0);
        }
        if (theirPersonalGamesToString.size() > 0) {
            mTheirGames.setItems(theirPersonalGamesToString);
            mTheirGames.setSelection(0);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapter);

    }

    private void requestForExchange() {

        List<PersonalGame> personalGameUser1 = new ArrayList<>();
        for (Integer i : mMyGames.getSelectedIndicies()) {
            personalGameUser1.add(aux.getPersonalGamesUser1().get(i));
        }
        List<PersonalGame> personalGameUser2 = new ArrayList<>();
        for (Integer i : mTheirGames.getSelectedIndicies()) {
            personalGameUser2.add(aux.getPersonalGamesUser2().get(i));
        }
        String auxType = (String) mType.getSelectedItem();
        Type type = auxType.equals("Fijo") ? Type.Fijo : Type.Temporal;

        String wayExchange = mWayExchange.getText().toString();
        Note note = new Note();
        note.setText(mNote.getText().toString());

        ExchangeForm exchangeForm = new ExchangeForm(personalGameUser1, personalGameUser2, type, wayExchange, note);
        RequestForExchange requestForExchange = new RequestForExchange();
        requestForExchange.execute(exchangeForm);
    }

    public class RequestForCreateExchange extends AsyncTask<PersonalGame, Void, ExchangeForm> {

        @Override
        protected ExchangeForm doInBackground(PersonalGame... params) {
            ExchangeForm res = null;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(instance);
            String url = RestTemplateManager.getUrl(instance, "exchange/create?personalGameId=" + params[0].getId());
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

            String url = RestTemplateManager.getUrl(instance, "exchange/save");
            try {
                ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
                System.out.println("Breakpoint");
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
                //TODO: Enviar a actividad de ver todos los exchange
                Toast toast = Toast.makeText(getApplicationContext(), "TODO: Enviar a actividad de ver todos los exchange", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
}
