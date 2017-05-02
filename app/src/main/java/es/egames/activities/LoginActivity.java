package es.egames.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import es.egames.R;
import es.egames.utils.RestTemplateManager;

/**
 * A login screen that offers login via userName/password.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mRegisterSection;
    private Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.userName);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mRegisterSection = findViewById(R.id.register_section);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showProgress(false);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for game_default valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for game_default valid username.
        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isUserNameValid(username)) {
            mUserNameView.setError(getString(R.string.error_invalid_username));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show game_default progress spinner, and kick off game_default background task to
            // perform the auxUser login attempt.
            showProgress(true);
            mAuthTask = new LoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUserNameValid(String username) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return true;
    }

    private void goToRegister() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        mAuthTask = null;
        startActivity(intent);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterSection.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mRegisterSection.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterSection.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterSection.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the auxUser.
     */
    public class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mPassword;

        LoginTask(String userName, String password) {
            mUserName = userName;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean hasErrors = false;
            final String baseUrl = getString(R.string.ip_server) + "oauth/token";
            String pass = "eGamesApp:eGamesAppSecret";
            String encoded = new String(Base64.encode(pass.getBytes(), Base64.DEFAULT));
            encoded = encoded.replace("\n", "");


            RestTemplate restTemplate = RestTemplateManager.create();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + encoded);
            HttpEntity httpEntity = new HttpEntity(headers);

            //TODO: Do this with game_default post request
//            Map<String, String> uriVariables = new HashMap<>();
//            uriVariables.put("username", mUserName);
//            uriVariables.put("password", mPassword);
//            uriVariables.put("grant_type", "password");

            String url = baseUrl + "?username=" + mUserName + "&password=" + mPassword + "&grant_type=password";
            String access_token;
            try {
                ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Map.class);

                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    Map<String, ?> entity = responseEntity.getBody();
                    access_token = (String) entity.get("access_token");

                    SharedPreferences sharedPref = getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("access_token", access_token);
                    editor.commit();
                }
            } catch (Exception oops) {
                hasErrors = true;
            }

            return hasErrors;
        }

        @Override
        protected void onPostExecute(final Boolean hasErrors) {
            mAuthTask = null;
            showProgress(false);
            SharedPreferences sharedPref = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String access_token = sharedPref.getString("access_token", null);
            if (!hasErrors) {
                if (access_token != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.error_invalid_credentials, Toast.LENGTH_LONG);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_general, Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

