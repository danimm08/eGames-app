package es.egames.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.adapters.MyPersonalGameFormRecyclerViewAdapter;
import es.egames.forms.GameDetailsForm;
import es.egames.model.PersonalGame;
import es.egames.utils.RestTemplateManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DetailsOfGameActivityFragment extends Fragment {

    private static final String ORDERBY = "orderBy";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<PersonalGame> personalGameList;
    private String orderBy;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailsOfGameActivityFragment() {
    }

    public static DetailsOfGameActivityFragment newInstance(String orderBy) {
        DetailsOfGameActivityFragment fragment = new DetailsOfGameActivityFragment();
        Bundle args = new Bundle();
        args.putString(ORDERBY, orderBy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            orderBy = getArguments().getString(ORDERBY);
        }

        GameDetailsForm gameDetailsForm = (GameDetailsForm) getActivity().getIntent().getSerializableExtra("game");
        RequestForGameDetailsListTask requestForGameDetailsListTask = new RequestForGameDetailsListTask();
        try {
            personalGameList = requestForGameDetailsListTask.execute(String.valueOf(gameDetailsForm.getId())).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            personalGameList = new ArrayList<>();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamedetails_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            boolean isDistance = orderBy.equals("distance");
            recyclerView.setAdapter(new MyPersonalGameFormRecyclerViewAdapter(personalGameList, mListener, isDistance));
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
        void onListFragmentInteraction(PersonalGame item);
    }

    public class RequestForGameDetailsListTask extends AsyncTask<String, Void, List<PersonalGame>> {


        public RequestForGameDetailsListTask() {
            super();
        }

        @Override
        protected List<PersonalGame> doInBackground(String... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getActivity());
            String url = RestTemplateManager.getUrl(getActivity(), "game/details?gameId=" + params[0] + "&orderBy=" + "reputation");
            if (orderBy == null) {
                orderBy = "reputation";
            }
            switch (orderBy) {
                case "reputation":
                    url = RestTemplateManager.getUrl(getActivity(), "game/details?gameId=" + params[0] + "&orderBy=" + "reputation");
                    break;
                case "type":
                    url = RestTemplateManager.getUrl(getActivity(), "game/details?gameId=" + params[0] + "&orderBy=" + "type");
                    break;
                case "followees":
                    url = RestTemplateManager.getUrl(getActivity(), "game/details?gameId=" + params[0] + "&orderBy=" + "followees");
                    break;
                case "distance":
                    url = RestTemplateManager.getUrl(getActivity(), "game/details?gameId=" + params[0] + "&orderBy=" + "distance");
                    break;
            }

            ResponseEntity<GameDetailsForm> responseEntity;
            List<PersonalGame> res = new ArrayList<>();
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, GameDetailsForm.class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    GameDetailsForm gameDetailsForm = responseEntity.getBody();
                    if (gameDetailsForm != null) {
                        res = new ArrayList<>(gameDetailsForm.getPersonalGames());
                    }
                }
            } catch (Exception oops) {
                oops.printStackTrace();
                res = new ArrayList<>();
            }
            return res;
        }

    }

}
