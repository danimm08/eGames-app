package es.egames.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.egames.R;
import es.egames.forms.SoughtItem;
import es.egames.fragments.SoughtItemFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MySoughtItemRecyclerViewAdapter extends RecyclerView.Adapter<MySoughtItemRecyclerViewAdapter.ViewHolder> {

    private List<SoughtItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySoughtItemRecyclerViewAdapter(List<SoughtItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_soughtitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (mValues.get(position).getImage() != null) {
            holder.mImageView.setImageBitmap(mValues.get(position).getImage());
        } else {
            holder.mImageView.setImageResource(R.drawable.default_image);
        }
        holder.mFieldOneView.setText(mValues.get(position).getFiledOne());
        holder.mFieldTwoView.setText(mValues.get(position).getFiledTwo());
        holder.mFieldThreeView.setText(mValues.get(position).getFiledThree());
        holder.mFieldFourView.setText(mValues.get(position).getFiledFour());

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

    public void setItems(List<SoughtItem> items) {
        mValues = items;
        notifyDataSetChanged();
    }

    public void addItem(SoughtItem item) {
        mValues.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(SoughtItem item) {
        int index = mValues.size() -1;
        mValues.remove(item);
        notifyItemRemoved(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mFieldOneView;
        public final TextView mFieldTwoView;
        public final TextView mFieldThreeView;
        public final TextView mFieldFourView;
        public SoughtItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.chat_image);
            mFieldOneView = (TextView) view.findViewById(R.id.chat_username);
            mFieldTwoView = (TextView) view.findViewById(R.id.fieldtwo);
            mFieldThreeView = (TextView) view.findViewById(R.id.fieldthree);
            mFieldFourView = (TextView) view.findViewById(R.id.fieldfour);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
