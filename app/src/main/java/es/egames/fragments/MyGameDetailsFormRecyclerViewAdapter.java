package es.egames.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import es.egames.R;
import es.egames.forms.GameDetailsForm;
import es.egames.fragments.GameDetailsFormList.OnListFragmentInteractionListener;


public class MyGameDetailsFormRecyclerViewAdapter extends RecyclerView.Adapter<MyGameDetailsFormRecyclerViewAdapter.ViewHolder> {

    private final List<GameDetailsForm> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyGameDetailsFormRecyclerViewAdapter(List<GameDetailsForm> gameDetailsForms, OnListFragmentInteractionListener listener) {
        mValues = gameDetailsForms;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        RequestForImageTask task = new RequestForImageTask();
        Bitmap bitmap;
        try {
            bitmap = task.execute(mValues.get(position).getCoverUrl()).get();
        } catch (Exception e) {
            bitmap = null;
        }

        if (bitmap != null) {
            holder.mImageView.setImageBitmap(bitmap);
        } else {
            holder.mImageView.setImageResource(R.drawable.default_image);
        }
        holder.mTitle.setText(mValues.get(position).getTitle());
        holder.mPlatform.setText(mValues.get(position).getPlatform().getName());
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
        public final ImageView mImageView;
        public final TextView mTitle;
        public final TextView mPlatform;
        public GameDetailsForm mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.card_game_image);
            mTitle = (TextView) view.findViewById(R.id.message_date);
            mPlatform = (TextView) view.findViewById(R.id.card_game_platform);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }

    public static class RequestForImageTask extends AsyncTask<String, Void, Bitmap> {


        public RequestForImageTask() {
            super();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp;
            try {
                URL url = new URL("http:" + params[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                bmp = null;
            } catch (Exception e) {
                bmp = null;
            }
            return bmp;
        }
    }
}
