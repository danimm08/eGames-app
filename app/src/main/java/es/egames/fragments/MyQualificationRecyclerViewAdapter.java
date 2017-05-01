package es.egames.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import es.egames.R;
import es.egames.fragments.QualificationFragment.OnListFragmentInteractionListener;
import es.egames.model.Qualification;

import java.util.List;


public class MyQualificationRecyclerViewAdapter extends RecyclerView.Adapter<MyQualificationRecyclerViewAdapter.ViewHolder> {

    private final List<Qualification> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyQualificationRecyclerViewAdapter(List<Qualification> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_qualification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Float mark = new Float(mValues.get(position).getMark());
        holder.mRating.setRating(mark);
        holder.mUsername.setText(mValues.get(position).getUser().getUserAccount().getUsername());
        holder.mText.setText(mValues.get(position).getText());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
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
        public final RatingBar mRating;
        public final TextView mUsername;
        public final TextView mText;
        public Qualification mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mRating = (RatingBar) view.findViewById(R.id.qualification_rating);
            mUsername = (TextView) view.findViewById(R.id.qualification_username);
            mText = (TextView) view.findViewById(R.id.qualification_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsername.getText() + "'";
        }
    }
}
