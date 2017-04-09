package es.egames.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import es.egames.R;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_empty);

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View inflatedLayout= inflater.inflate(R.layout.activity_empty, null, false);
        FrameLayout constraintLayout = (FrameLayout) findViewById(R.id.content_main);
//        constraintLayout.removeAllViews();
        constraintLayout.removeAllViewsInLayout();
        constraintLayout.addView(inflatedLayout);
    }
}
