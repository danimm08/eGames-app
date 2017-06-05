package es.egames.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import es.egames.R;
import es.egames.fragments.ImagePersonalGameFragment.OnListFragmentInteractionListener;

public class MyImageRecyclerViewAdapter extends RecyclerView.Adapter<MyImageRecyclerViewAdapter.ViewHolder> {

    private final List<Bitmap> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyImageRecyclerViewAdapter(List<Bitmap> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mImage = mValues.get(position);

        holder.mImageView.setImageBitmap(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mImage);
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
        public Bitmap mImage;
        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.personal_game_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}