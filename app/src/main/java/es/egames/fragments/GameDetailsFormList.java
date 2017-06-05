package es.egames.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.adapters.MyGameDetailsFormRecyclerViewAdapter;
import es.egames.forms.GameDetailsForm;
import es.egames.utils.RestTemplateManager;

/**
 * A fragment representing game_default list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GameDetailsFormList extends Fragment {

    private static final String MODE = "mode";
    private String mode = "nearby";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    private List<GameDetailsForm> list;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GameDetailsFormList() {
    }

    public static GameDetailsFormList newInstance(String mode) {
        GameDetailsFormList fragment = new GameDetailsFormList();
        Bundle args = new Bundle();
        args.putString(MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mode = getArguments().getString(MODE);
        }

        RequestForGameDatilsListTask task = new RequestForGameDatilsListTask();
        AsyncTask<Void, Void, List<GameDetailsForm>> res = task.execute();
        try {
            list = res.get();
        } catch (InterruptedException e) {
            list = new ArrayList<>();
        } catch (ExecutionException e) {
            list = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamedetailsform_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            MyGameDetailsFormRecyclerViewAdapter adapter = new MyGameDetailsFormRecyclerViewAdapter(list, mListener);
            recyclerView.setAdapter(adapter);
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
     * See the Android Training lesson <game_default href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</game_default> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(GameDetailsForm gameDetailsForm);
    }


    public class RequestForGameDatilsListTask extends AsyncTask<Void, Void, List<GameDetailsForm>> {


        public RequestForGameDatilsListTask() {
            super();
        }

        @Override
        protected List<GameDetailsForm> doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getActivity());
            String url = RestTemplateManager.getUrl(getActivity(), "game/customList?type=distance");
            switch (mode) {
                case "nearby":
                    url = RestTemplateManager.getUrl(getActivity(), "game/customList?type=distance");
                    break;
                case "recommended":
                    url = RestTemplateManager.getUrl(getActivity(), "game/customList?type=recommend");
                    break;
                case "followed":
                    url = RestTemplateManager.getUrl(getActivity(), "game/customList?type=followees");
                    break;
            }

            ResponseEntity<GameDetailsForm[]> responseEntity;
            List<GameDetailsForm> res = new ArrayList<>();
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, GameDetailsForm[].class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    res = Arrays.asList(responseEntity.getBody());
                }
            } catch (Exception oops) {
                res = new ArrayList<>();
            }
            return res;
        }

    }

}
