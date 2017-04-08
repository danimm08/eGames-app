package es.egames.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.egames.R;
import es.egames.forms.RegistrationForm;
import es.egames.model.Address;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText surnameET;
    private EditText streetET;
    private EditText cityET;
    private EditText stateET;
    private EditText countryET;
    private EditText zipET;
    private EditText emailET;
    private EditText userNameET;
    private EditText passwordET;
    private LinkedHashMap<String, List> errors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register_button = (Button) findViewById(R.id.register_form_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestForRegister();
            }
        });
    }

    private void requestForRegister() {
        nameET = (EditText) findViewById(R.id.register_name);
        surnameET = (EditText) findViewById(R.id.register_surname);
        streetET = (EditText) findViewById(R.id.register_address_street);
        cityET = (EditText) findViewById(R.id.register_address_city);
        stateET = (EditText) findViewById(R.id.register_address_state);
        countryET = (EditText) findViewById(R.id.register_address_country);
        zipET = (EditText) findViewById(R.id.register_address_zip);
        emailET = (EditText) findViewById(R.id.register_user_account_email);
        userNameET = (EditText) findViewById(R.id.register_user_account_user_name);
        passwordET = (EditText) findViewById(R.id.register_user_account_password);

        String name = nameET.getText().toString();
        String surname = surnameET.getText().toString();
        String street = streetET.getText().toString();
        String city = cityET.getText().toString();
        String state = stateET.getText().toString();
        String country = countryET.getText().toString();
        String zip = zipET.getText().toString();
        String email = emailET.getText().toString();
        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();

        RequestForRegister requestForRegister = new RequestForRegister(name, surname, street, city, state, country, zip, email, userName, password);
        Activity[] params = {this};
        requestForRegister.execute(params);
    }

    private void showErrors() {
        Map<String, EditText> mapErrors = new HashMap<>();
        mapErrors.put("name", nameET);
        mapErrors.put("surname", surnameET);
        mapErrors.put("address.street", streetET);
        mapErrors.put("address.city", cityET);
        mapErrors.put("address.state", stateET);
        mapErrors.put("address.country", countryET);
        mapErrors.put("address.zip", zipET);
        mapErrors.put("email", emailET);
        mapErrors.put("username", userNameET);
        mapErrors.put("password", passwordET);

        if (errors != null && errors.keySet().size() > 0) {
            for (String error : errors.keySet()) {
                mapErrors.get(error).setError(errors.get(error).toString());
            }
        }
    }


    public class RequestForRegister extends AsyncTask<Activity, Void, Boolean> {

        private String name;
        private String surname;
        private String street;
        private String city;
        private String country;
        private String state;
        private String zip;
        private String email;
        private String userName;
        private String password;

        public RequestForRegister(String name, String surname, String street, String city, String country, String state, String zip, String email, String userName, String password) {
            this.name = name;
            this.surname = surname;
            this.street = street;
            this.city = city;
            this.country = country;
            this.state = state;
            this.zip = zip;
            this.email = email;
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Activity... params) {
            boolean hasErrors = false;

            Address address = new Address(street, city, state, country, zip);
            RegistrationForm registrationForm = new RegistrationForm(name, surname, address, userName, password, email);

            RestTemplate restTemplate = RestTemplateManager.create();
            String url = RestTemplateManager.getUrl(params[0], "registration");

            try {
                ResponseEntity<LinkedHashMap> responseEntity = restTemplate.postForEntity(url, registrationForm, LinkedHashMap.class);

                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    errors = null;
                    startActivity(intent);
                } else {
                    errors = (LinkedHashMap<String, List>) responseEntity.getBody();
                }
            } catch (Exception e) {
                hasErrors = true;
            }
            return hasErrors;
        }

        @Override
        protected void onPostExecute(Boolean hasErrors) {
            if (!hasErrors) {
                showErrors();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),R.string.error_general, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }


}
