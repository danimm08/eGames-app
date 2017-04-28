package es.egames.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.egames.R;
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.forms.QualificationForm;
import es.egames.utils.RestTemplateManager;

public class QualifyExchangeActivity extends AppCompatActivity {

    private RatingBar mRatingBar;
    private AutoCompleteTextView mText;
    private Button mButton;
    private DetailsOfExchangeForm detailsOfExchangeForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualify_exchange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.qualify);
        detailsOfExchangeForm = (DetailsOfExchangeForm) getIntent().getSerializableExtra("detailsOfExchangeForm");

        mRatingBar = (RatingBar) findViewById(R.id.qualify_ratingbar);
        mText = (AutoCompleteTextView) findViewById(R.id.qualify_text);
        mButton = (Button) findViewById(R.id.qualify_button);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForQualify();
            }
        });

    }

    private void requestForQualify() {
        QualificationForm qualificationForm = new QualificationForm();
        qualificationForm.setText(mText.getText().toString());
        Double auxRating = new Double(mRatingBar.getRating() * 5) / 10;
        Double rating = new Double(Math.round(auxRating));
        qualificationForm.setMark(rating);
        RequestForQualify requestForQualify = new RequestForQualify();
        requestForQualify.execute(qualificationForm);
    }

    public class RequestForQualify extends AsyncTask<QualificationForm, Void, Boolean> {

        @Override
        protected Boolean doInBackground(QualificationForm... params) {
            Boolean hasErrors;
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity httpEntity = RestTemplateManager.authenticateRequestWithObject(getApplicationContext(), params[0]);
            String url = RestTemplateManager.getUrl(getApplicationContext(), "qualification/save?exchangeId=" + detailsOfExchangeForm.getId());
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
