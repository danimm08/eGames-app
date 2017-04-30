package es.egames.fragments;

import android.app.Activity;
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

import es.egames.R;
import es.egames.activities.MessageActivity;
import es.egames.fragments.dummy.DummyContent;
import es.egames.fragments.dummy.DummyContent.DummyItem;
import es.egames.model.Message;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessageFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private List<Message> items;
    private User user;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessageFragment() {
    }

    public static MessageFragment newInstance(int columnCount) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        if (getArguments() != null) {
        }
        RequestForMessages requestForMessages = new RequestForMessages();
        try {
            items = requestForMessages.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            items = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyMessageRecyclerViewAdapter(items, mListener, getContext()));
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
        void onListFragmentInteraction(Message item);
    }

    public class RequestForMessages extends AsyncTask<Void, Void, List<Message>> {

        @Override
        protected List<Message> doInBackground(Void... params) {
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getContext());
            String url = RestTemplateManager.getUrl(getContext(), "message/chat?username=" + user.getUserAccount().getUsername());
            List<Message> messages = new ArrayList<>();
            ResponseEntity<Message[]> responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, Message[].class);
                if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    messages = Arrays.asList(responseEntity.getBody());
                }
            } catch (Exception oops) {
                messages = new ArrayList<>();
            }
            return messages;
        }

    }
}
