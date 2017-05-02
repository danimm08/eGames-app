package es.egames.fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import es.egames.R;
import es.egames.activities.DetailsUserActivity;
import es.egames.model.PersonalGame;

/**
 * A placeholder fragment containing a simple view.
 */
public class PersonalGameDetailsActivityFragment extends Fragment {

    public TextView mUsername;
    public RatingBar mRatingbar;
    public TextView mDescription;
    public TextView mType;
    public TextView mNumber;
    public TextView mNumberDescription;
    public TextView mDistanceDescription;
    public TextView mDistanceNumber;
    public PersonalGame personalGame;

    public PersonalGameDetailsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personalGame = (PersonalGame) getActivity().getIntent().getSerializableExtra("personalgame");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_game_details, container, false);

        mUsername = (TextView) view.findViewById(R.id.message_date);
        mRatingbar = (RatingBar) view.findViewById(R.id.user_rating);
        mDescription = (TextView) view.findViewById(R.id.card_personalgame_description);
        mType = (TextView) view.findViewById(R.id.message_sender);
        mNumber = (TextView) view.findViewById(R.id.item_exchange_their_games_content);
        mNumberDescription = (TextView) view.findViewById(R.id.message_text);
        mDistanceDescription = (TextView) view.findViewById(R.id.card_personalgame_distanceDescription);
        mDistanceNumber = (TextView) view.findViewById(R.id.card_personalgame_distanceNumber);

        mUsername.setText(personalGame.getUser().getUserAccount().getUsername());
        mUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailsUserActivity.class);
                intent.putExtra("user", personalGame.getUser());
                startActivity(intent);
            }
        });
        Float rating = new Float(personalGame.getUser().getReputation());
        mRatingbar.setRating(rating);
        mDescription.setText(personalGame.getDescription());
        mType.setText(personalGame.getType().toString());
        mNumberDescription.setText(R.string.num_exchanges);
        mNumber.setText(String.valueOf(personalGame.getUser().getnExchanges()));

        if (personalGame.getDistance() != null) {
            mDistanceDescription.setText(R.string.distance);
            mDistanceNumber.setText(String.valueOf(personalGame.getDistance()) + " Km");
        }

        return view;
    }
}
