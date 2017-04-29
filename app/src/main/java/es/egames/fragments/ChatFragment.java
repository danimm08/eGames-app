package es.egames.fragments;

import android.content.Context;
import android.content.Intent;
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

import es.egames.R;
import es.egames.activities.LoginActivity;
import es.egames.fragments.dummy.DummyContent;
import es.egames.fragments.dummy.DummyContent.DummyItem;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ChatFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private List<User> chats;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            RequestForChats requestForChats = new RequestForChats();
            try {
                chats = requestForChats.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                chats = new ArrayList<>();
            }
            recyclerView.setAdapter(new MyChatRecyclerViewAdapter(chats, mListener, getContext()));
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
        void onListFragmentInteraction(User item);
    }

    public class RequestForChats extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getContext());
            String url = RestTemplateManager.getUrl(getContext(), "message/chats");
            List<User> res = new ArrayList<>();
            ResponseEntity<User[]> responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, User[].class);
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
