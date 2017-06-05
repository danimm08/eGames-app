package es.egames.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import es.egames.R;
import es.egames.fragments.NoteFragment.OnListFragmentInteractionListener;
import es.egames.model.Note;

import java.text.DateFormat;
import java.util.List;

public class MyNoteRecyclerViewAdapter extends RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder> {

    private final List<Note> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyNoteRecyclerViewAdapter(List<Note> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        holder.mDateView.setText(dateFormat.format(mValues.get(position).getDate()));
        holder.mUsernameView.setText(mValues.get(position).getUser().getUserAccount().getUsername());
        holder.mTextView.setText(mValues.get(position).getText());

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
        public final TextView mUsernameView;
        public final TextView mTextView;
        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.message_sender);
            mUsernameView = (TextView) view.findViewById(R.id.message_date);
            mTextView = (TextView) view.findViewById(R.id.note_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsernameView.getText() + "'";
        }
    }
}
