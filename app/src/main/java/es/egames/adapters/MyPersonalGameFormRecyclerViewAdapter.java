package es.egames.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import es.egames.R;
import es.egames.fragments.DetailsOfGameActivityFragment.OnListFragmentInteractionListener;
import es.egames.model.PersonalGame;

import java.util.List;

public class MyPersonalGameFormRecyclerViewAdapter extends RecyclerView.Adapter<MyPersonalGameFormRecyclerViewAdapter.ViewHolder> {

    private final List<PersonalGame> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final boolean isDistance;

    public MyPersonalGameFormRecyclerViewAdapter(List<PersonalGame> items, OnListFragmentInteractionListener listener, boolean isDistance) {
        mValues = items;
        mListener = listener;
        this.isDistance = isDistance;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_personalgame_form, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mPersonalGame = mValues.get(position);
        holder.mUsername.setText(mValues.get(position).getUser().getUserAccount().getUsername());
        Float rating = new Float(mValues.get(position).getUser().getReputation());
        holder.mRatingbar.setRating(rating);
        holder.mType.setText(mValues.get(position).getType().toString());
        if(!isDistance){
            holder.mNumberDescription.setText(R.string.num_exchanges);
            holder.mNumber.setText(String.valueOf(mValues.get(position).getUser().getnExchanges()));
        }else{
            holder.mNumberDescription.setText(R.string.distance);
            holder.mNumber.setText(String.valueOf(mValues.get(position).getDistance()) + " Km");
        }



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mPersonalGame);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUsername;
        public final RatingBar mRatingbar;
        public final TextView mType;
        public final TextView mNumber;
        public final TextView mNumberDescription;
        public PersonalGame mPersonalGame;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsername = (TextView) view.findViewById(R.id.message_date);
            mRatingbar = (RatingBar) view.findViewById(R.id.user_rating);
            mType = (TextView) view.findViewById(R.id.message_sender);
            mNumber = (TextView) view.findViewById(R.id.item_exchange_their_games_content);
            mNumberDescription = (TextView) view.findViewById(R.id.message_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsername.getText() + "'";
        }
    }
}
