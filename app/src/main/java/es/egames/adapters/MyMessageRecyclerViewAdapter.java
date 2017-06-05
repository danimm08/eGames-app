package es.egames.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import es.egames.R;
import es.egames.fragments.MessageFragment.OnListFragmentInteractionListener;
import es.egames.model.Message;


public class MyMessageRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageRecyclerViewAdapter.ViewHolder> {

    private final List<Message> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyMessageRecyclerViewAdapter(List<Message> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        String date = dateFormat.format(mValues.get(position).getMoment());
        String time = timeFormat.format(mValues.get(position).getMoment());
        holder.mDate.setText(date + " " + time);
        holder.mSender.setText(mValues.get(position).getSender().getUserAccount().getUsername());
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
        public final TextView mDate;
        public final TextView mSender;
        public final TextView mText;
        public Message mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDate = (TextView) view.findViewById(R.id.message_date);
            mSender = (TextView) view.findViewById(R.id.message_sender);
            mText = (TextView) view.findViewById(R.id.message_text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }

}
