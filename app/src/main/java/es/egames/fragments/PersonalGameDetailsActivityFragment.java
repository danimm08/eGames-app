package es.egames.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import es.egames.R;
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

        mUsername = (TextView) view.findViewById(R.id.card_personalgame_username);
        mRatingbar = (RatingBar) view.findViewById(R.id.card_personalgame_ratingbar);
        mDescription = (TextView) view.findViewById(R.id.card_personalgame_description);
        mType = (TextView) view.findViewById(R.id.card_personalgame_type);
        mNumber = (TextView) view.findViewById(R.id.card_personalgame_number);
        mNumberDescription = (TextView) view.findViewById(R.id.card_personalgame_numberDescription);
        mDistanceDescription = (TextView) view.findViewById(R.id.card_personalgame_distanceDescription);
        mDistanceNumber = (TextView) view.findViewById(R.id.card_personalgame_distanceNumber);

        mUsername.setText(personalGame.getUser().getUserAccount().getUsername());
        Double auxRating = (personalGame.getUser().getReputation() * 5) / 10;
        Long rating = Math.round(auxRating);
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
