package es.egames.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.model.Image;
import es.egames.model.PersonalGame;
import es.egames.utils.RestTemplateManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ImagePersonalGameFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    public PersonalGame personalGame;
    public List<Bitmap> images;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ImagePersonalGameFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        personalGame = (PersonalGame) getActivity().getIntent().getSerializableExtra("personalgame");

        RequestForImageTask task = new RequestForImageTask();
        Image[] auxImages = personalGame.getImages().toArray(new Image[personalGame.getImages().size()]);
        try {
            images = task.execute(auxImages).get();
        } catch (InterruptedException | ExecutionException e) {
            images = new ArrayList<>();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyImageRecyclerViewAdapter(images, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Bitmap image);
    }


    public class RequestForImageTask extends AsyncTask<Image, Void, List<Bitmap>> {


        public RequestForImageTask() {
            super();
        }

        @Override
        protected List<Bitmap> doInBackground(Image... params) {
            List<Bitmap> res = new ArrayList<>();
            for (Image i : params) {
                Bitmap bmp;
                try {
                    String auxUrl = RestTemplateManager.getUrl(getActivity(), "image/download?filename=" + URLEncoder.encode(i.getPathUrl(), "UTF-8").toString());
                    URLConnection connection = RestTemplateManager.getConnection(getActivity(), auxUrl);
                    bmp = BitmapFactory.decodeStream(connection.getInputStream());
                    res.add(bmp);
                } catch (Exception e) {
                }

            }
            return res;
        }
    }
}
