package es.egames.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.egames.R;
import es.egames.activities.DetailsUserActivity;
import es.egames.fragments.ChatFragment.OnListFragmentInteractionListener;
import es.egames.fragments.dummy.DummyContent.DummyItem;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyChatRecyclerViewAdapter extends RecyclerView.Adapter<MyChatRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;

    public MyChatRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Bitmap image;
        RequestForImageTask requestForImageTask = new RequestForImageTask();
        try {
            image = requestForImageTask.execute(holder.mItem.getProfilePicture()).get();
            if (image != null) {
                holder.mImageView.setImageBitmap(image);
            } else {
                holder.mImageView.setImageResource(R.drawable.default_image);
            }

        } catch (InterruptedException | ExecutionException e) {
            holder.mImageView.setImageResource(R.drawable.default_image);
        }
        holder.mUsername.setText(mValues.get(position).getUserAccount().getUsername());

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
        public final ImageView mImageView;
        public final TextView mUsername;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.chat_image);
            mUsername = (TextView) view.findViewById(R.id.chat_username);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }

    public class RequestForImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = null;
            try {
                String auxUrl = RestTemplateManager.getUrl(context, "image/download?filename=" + URLEncoder.encode(params[0], "UTF-8").toString());
                URLConnection connection = RestTemplateManager.getConnection(context, auxUrl);
                bmp = BitmapFactory.decodeStream(connection.getInputStream());
            } catch (Exception e) {
            }
            return bmp;
        }
    }
}
