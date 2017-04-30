package es.egames.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import es.egames.R;
import es.egames.fragments.MessageFragment;
import es.egames.model.Message;
import es.egames.model.User;

public class MessageActivity extends AppCompatActivity implements MessageFragment.OnListFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        User user = (User) getIntent().getSerializableExtra("user");
        setTitle(getString(R.string.chat_with) + " " + user.getUserAccount().getUsername());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Message item) {
    }
}
