package es.egames.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.egames.R;
import es.egames.forms.UserProfileForm;
import es.egames.forms.UserUserAccountForm;
import es.egames.model.Address;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

public class ModifyPersonalData extends AppCompatActivity {

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
    private EditText oldPasswordET;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_data);
        user = (User) getIntent().getSerializableExtra("user");

        Button register_button = (Button) findViewById(R.id.modify_info);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestForModifyInfo();
            }
        });
    }

    private void requestForModifyInfo() {
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
        oldPasswordET = (EditText) findViewById(R.id.register_user_account_old_password);

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
        String oldPassword = oldPasswordET.getText().toString();

        Address address = new Address(street, city, state, country, zip);
        UserProfileForm userProfileForm = new UserProfileForm();
        userProfileForm.setAddress(address);
        userProfileForm.setName(name);
        userProfileForm.setSurname(surname);

        UserUserAccountForm userUserAccountForm = new UserUserAccountForm();
        userUserAccountForm.setEmail(email);
        userUserAccountForm.setPassword(password);
        userUserAccountForm.setOldPassword(oldPassword);
        userUserAccountForm.setUsername(userName);

        RequestForModifyInfo requestForModifyInfo = new RequestForModifyInfo();
        requestForModifyInfo.execute(userProfileForm, userUserAccountForm);

    }

    public class RequestForModifyInfo extends AsyncTask<Object, Void, Map<String, Object>> {

        @Override
        protected Map<String, Object> doInBackground(Object... params) {
            Set<Integer> errors = new HashSet<>();
            UserProfileForm userProfileForm;
            UserUserAccountForm userUserAccountForm;
            Map<String, Object> info = new HashMap<>();
            Boolean flag = false;

            if (params[0] instanceof UserProfileForm) {
                userProfileForm = (UserProfileForm) params[0];
                RestTemplate restTemplate = RestTemplateManager.create();
                HttpEntity httpEntity = RestTemplateManager.authenticateRequestWithObject(getApplicationContext(), userProfileForm);
                String url = RestTemplateManager.getUrl(getApplicationContext(), "user/profile/edit");
                try {
                    ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);

                    if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                        errors.add(R.string.error_personaldata);
                    }
                } catch (Exception e) {
                    errors.add(R.string.error_personaldata);
                }
            }
            if (params[1] instanceof UserUserAccountForm) {
                userUserAccountForm = (UserUserAccountForm) params[1];
                RestTemplate restTemplate = RestTemplateManager.create();
                HttpEntity httpEntity = RestTemplateManager.authenticateRequestWithObject(getApplicationContext(), userUserAccountForm);
                String url = RestTemplateManager.getUrl(getApplicationContext(), "userAccount/edit");
                try {
                    ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);

                    if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                        errors.add(R.string.error_userAccount);
                        errors.add(R.string.error_userAccount_remmind);
                    } else {
                        Map<String, Boolean> res = (Map<String, Boolean>) responseEntity.getBody();
                        flag = res.get("result");
                        if (res.get("result")) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            RestTemplateManager.logout(getApplicationContext());
                            finish();
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    errors.add(R.string.error_userAccount);
                    errors.add(R.string.error_userAccount_remmind);
                }
            }
            info.put("errors", errors);
            info.put("flag", flag);
            return info;
        }

        @Override
        protected void onPostExecute(Map<String, Object> info) {
            List<Integer> errors = new ArrayList<>((Set<Integer>) info.get("errors"));
            Boolean flag = (Boolean) info.get("flag");
            if (!errors.isEmpty()) {
                String errorsAsString = "";
                for (Integer i : errors) {
                    String aux = getString(i);
                    if (errors.indexOf(i) == 0) {
                        errorsAsString += aux;
                    } else {
                        errorsAsString += ", " + aux;
                    }
                }
                Toast.makeText(getApplicationContext(), errorsAsString, Toast.LENGTH_LONG).show();
            } else {
                if (flag == false) {
                    Intent intent = new Intent(getApplicationContext(), DetailsUserActivity.class);
                    DetailsUserActivity.instance.finish();
                    intent.putExtra("user", user);
                    finish();
                    startActivity(intent);
                }
            }
        }
    }


}
