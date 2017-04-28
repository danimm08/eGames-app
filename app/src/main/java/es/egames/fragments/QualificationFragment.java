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

import es.egames.R;
import es.egames.model.Qualification;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class QualificationFragment extends Fragment {

    private User user;
    private List<Qualification> qualifications;
    private OnListFragmentInteractionListener mListener;

    public QualificationFragment() {
    }


    public static QualificationFragment newInstance(User u) {
        QualificationFragment fragment = new QualificationFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", u);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }

        RequestForQualifications requestForQualifications = new RequestForQualifications();
        try {
            qualifications = requestForQualifications.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            qualifications = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qualification_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyQualificationRecyclerViewAdapter(qualifications, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Qualification item);
    }

    public class RequestForQualifications extends AsyncTask<Void, Void, List<Qualification>> {

        @Override
        protected List<Qualification> doInBackground(Void... params) {
            List<Qualification> qualifications = new ArrayList<>();
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity httpEntity = RestTemplateManager.authenticateRequest(getContext());
            String url = RestTemplateManager.getUrl(getContext(), "qualification/list?userId=" + user.getId());
            try {
                ResponseEntity<Qualification[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Qualification[].class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    qualifications = Arrays.asList(responseEntity.getBody());
                }
            } catch (Exception e) {
                qualifications = new ArrayList<>();
            }
            return qualifications;
        }
    }
}
