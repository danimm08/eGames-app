package es.egames.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import es.egames.forms.DetailsOfExchangeForm;
import es.egames.utils.RestTemplateManager;

//TODO: OnBackToFragment, update data
public class ExchangeFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExchangeFragment() {
    }

    public static ExchangeFragment newInstance(int columnCount) {
        ExchangeFragment fragment = new ExchangeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RequestForDetailsOfExchangeFormTask requestForDetailsOfExchangeFormTask = new RequestForDetailsOfExchangeFormTask();
            List<DetailsOfExchangeForm> detailsOfExchangeForms;
            try {
                detailsOfExchangeForms = requestForDetailsOfExchangeFormTask.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                detailsOfExchangeForms = new ArrayList<>();
            }
            recyclerView.setAdapter(new MyExchangeRecyclerViewAdapter(detailsOfExchangeForms, mListener, getContext()));
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DetailsOfExchangeForm item);
    }

    public class RequestForDetailsOfExchangeFormTask extends AsyncTask<Void, Void, List<DetailsOfExchangeForm>> {


        public RequestForDetailsOfExchangeFormTask() {
            super();
        }

        @Override
        protected List<DetailsOfExchangeForm> doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getActivity());
            String url = RestTemplateManager.getUrl(getActivity(), "exchange/getMyExchanges");

            ResponseEntity<DetailsOfExchangeForm[]> responseEntity;
            List<DetailsOfExchangeForm> res = new ArrayList<>();
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, DetailsOfExchangeForm[].class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    DetailsOfExchangeForm[] detailsOfExchangeForms = responseEntity.getBody();
                    res = Arrays.asList(detailsOfExchangeForms);
                }
            } catch (Exception oops) {
                res = new ArrayList<>();
            }
            return res;
        }

    }
}
