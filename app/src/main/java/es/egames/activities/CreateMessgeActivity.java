package es.egames.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.egames.R;
import es.egames.forms.ExchangeForm;
import es.egames.forms.MessageForm;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class CreateMessgeActivity extends AppCompatActivity {

    private User user;
    private AutoCompleteTextView mText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_messge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.send_message));

        user = (User) getIntent().getSerializableExtra("user");
        mText = (AutoCompleteTextView) findViewById(R.id.message_text);
        mButton = (Button) findViewById(R.id.message_send);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForCreateMessage();
            }
        });

    }

    private void requestForCreateMessage() {
        MessageForm messageForm = new MessageForm();
        messageForm.setText(mText.getText().toString());
        messageForm.setRecipient(user);
        RequestForSendMessage requestForSendMessage = new RequestForSendMessage();
        requestForSendMessage.execute(messageForm);
    }


    public class RequestForSendMessage extends AsyncTask<MessageForm, Void, Boolean> {

        @Override
        protected Boolean doInBackground(MessageForm... params) {
            Boolean hasErrors;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity httpEntity = RestTemplateManager.authenticateRequestWithObject(getApplicationContext(), params[0]);

            String url = RestTemplateManager.getUrl(getApplicationContext(), "message/send");
            try {
                ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Void.class);
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
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("user", user);
                if (MessageActivity.instance != null) {
                    MessageActivity.instance.finish();
                }
                finish();
                startActivity(intent);

            }
        }

    }

}
