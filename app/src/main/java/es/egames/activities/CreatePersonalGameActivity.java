package es.egames.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.egames.R;
import es.egames.forms.ExchangeForm;
import es.egames.forms.GameDetailsForm;
import es.egames.forms.PersonalGameForm;
import es.egames.model.Game;
import es.egames.model.Type;
import es.egames.utils.RestTemplateManager;

public class CreatePersonalGameActivity extends AppCompatActivity {

    private Spinner mType;
    private AutoCompleteTextView mDescrition;
    private Button mButton;
    private GameDetailsForm gameDetailsForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.create_personal_game);
        setContentView(R.layout.activity_create_personal_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gameDetailsForm = (GameDetailsForm) getIntent().getSerializableExtra("gameDetailsForm");

        mType = (Spinner) findViewById(R.id.create_personal_game_spinner);
        mDescrition = (AutoCompleteTextView) findViewById(R.id.create_personal_game_description);
        mButton = (Button) findViewById(R.id.create_personal_game_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requesForCreatePersonalGame();
            }
        });

    }

    private void requesForCreatePersonalGame() {
        PersonalGameForm personalGameForm = new PersonalGameForm();
        personalGameForm.setDescription(mDescrition.getText().toString());
        String auxType = (String) mType.getSelectedItem();
        Type type = auxType.equals("Fijo") ? Type.Fijo : Type.Temporal;
        personalGameForm.setType(type);
        Game auxGame = new Game();
        auxGame.setId(gameDetailsForm.getId());
        personalGameForm.setGame(auxGame);

        RequestForCreatePersonalGame requestForCreatePersonalGame = new RequestForCreatePersonalGame();
        requestForCreatePersonalGame.execute(personalGameForm);
    }

    public class RequestForCreatePersonalGame extends AsyncTask<PersonalGameForm, Void, Boolean> {

        @Override
        protected Boolean doInBackground(PersonalGameForm... params) {
            Boolean hasErrors;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity httpEntity = RestTemplateManager.authenticateRequestWithObject(getApplicationContext(), params[0]);

            String url = RestTemplateManager.getUrl(getApplicationContext(), "personalgame/save");
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
