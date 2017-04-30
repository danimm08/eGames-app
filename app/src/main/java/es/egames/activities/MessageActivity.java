package es.egames.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.Serializable;

import es.egames.R;
import es.egames.fragments.MessageFragment;
import es.egames.model.Message;
import es.egames.model.User;

public class MessageActivity extends AppCompatActivity implements MessageFragment.OnListFragmentInteractionListener{

    private User user;
    public static MessageActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instance = this;
        user = (User) getIntent().getSerializableExtra("user");
        setTitle(getString(R.string.chat_with) + " " + user.getUserAccount().getUsername());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateMessgeActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Message item) {
    }
}
