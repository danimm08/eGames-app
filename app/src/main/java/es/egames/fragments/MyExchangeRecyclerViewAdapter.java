package es.egames.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import es.egames.R;
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.fragments.ExchangeFragment.OnListFragmentInteractionListener;
import es.egames.model.PersonalGame;


public class MyExchangeRecyclerViewAdapter extends RecyclerView.Adapter<MyExchangeRecyclerViewAdapter.ViewHolder> {

    private final List<DetailsOfExchangeForm> mValues;
    private Context context;
    private final OnListFragmentInteractionListener mListener;

    public MyExchangeRecyclerViewAdapter(List<DetailsOfExchangeForm> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exchange, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        String formattedDate = dateFormat.format(mValues.get(position).getCreationDate());
        holder.mDateView.setText(formattedDate);
        if (mValues.get(position).getStatus() == null) {
            holder.mStateButton.setText(R.string.pending);
            holder.mStateButton.setTextColor(context.getResources().getColor(R.color.holo_orange_dark));
        } else if (mValues.get(position).getStatus()) {
            holder.mStateButton.setText(R.string.accepted);
            holder.mStateButton.setTextColor(context.getResources().getColor(R.color.holo_green_dark));
        } else if (mValues.get(position).getStatus() == false) {
            holder.mStateButton.setText(R.string.denied);
            holder.mStateButton.setTextColor(context.getResources().getColor(R.color.holo_red_dark));
        }

        List<PersonalGame> aux = new ArrayList<>(mValues.get(position).getPersonalGameUser2());
        holder.mUsername.setText(aux.get(0).getUser().getUserAccount().getUsername());
        String theirExchanges = "";
        for (PersonalGame pg : mValues.get(position).getPersonalGameUser2()) {
            if (!theirExchanges.isEmpty()) {
                theirExchanges += ", " + pg.getGame().getTitle() + " (" + pg.getGame().getPlatform().getName() + ")";
            } else {
                theirExchanges += pg.getGame().getTitle() + " (" + pg.getGame().getPlatform().getName() + ")";
            }
        }

        String myExchanges = "";
        for (PersonalGame pg : mValues.get(position).getPersonalGameUser1()) {
            if (!myExchanges.isEmpty()) {
                myExchanges += ", " + pg.getGame().getTitle() + " (" + pg.getGame().getPlatform().getName() + ")";
            } else {
                myExchanges += pg.getGame().getTitle() + " (" + pg.getGame().getPlatform().getName() + ")";
            }
        }

        holder.mTheirExchanges.setText(theirExchanges);
        holder.mMyExchanges.setText(myExchanges);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView mDateView;
        public final Button mStateButton;
        public final TextView mUsername;
        public final TextView mTheirExchanges;
        public final TextView mMyExchanges;

        public DetailsOfExchangeForm mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.item_exchange_date);
            mStateButton = (Button) view.findViewById(R.id.exchange_state);
            mUsername = (TextView) view.findViewById(R.id.item_exchange_username);
            mTheirExchanges = (TextView) view.findViewById(R.id.item_exchange_their_games_content);
            mMyExchanges = (TextView) view.findViewById(R.id.item_exchange_my_games_content);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
