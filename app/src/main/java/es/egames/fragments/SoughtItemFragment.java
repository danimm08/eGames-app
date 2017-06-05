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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import es.egames.R;
import es.egames.adapters.MySoughtItemRecyclerViewAdapter;
import es.egames.forms.SoughtItem;
import es.egames.model.Game;
import es.egames.model.PersonalGame;
import es.egames.model.User;
import es.egames.utils.RestTemplateManager;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SoughtItemFragment extends Fragment {

    public static final String MODE = "mode";
    private static final String AUXOBJECT = "auxObject";
    private int mColumnCount = 1;
    private String mode;
    private Object auxObject;
    private List<SoughtItem> items;
    private OnListFragmentInteractionListener mListener;
    public MySoughtItemRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SoughtItemFragment() {
    }

    public static SoughtItemFragment newInstance(String mode, Object auxObject) {
        SoughtItemFragment fragment = new SoughtItemFragment();
        Bundle args = new Bundle();
        args.putString(MODE, mode);
        if (auxObject instanceof String)
            args.putString(AUXOBJECT, (String) auxObject);
        if (auxObject instanceof User)
            args.putSerializable(AUXOBJECT, (User) auxObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mode = getArguments().getString(MODE);

            auxObject = getArguments().getSerializable(AUXOBJECT);

        }

        RequestForSearchTask task = new RequestForSearchTask();
        try {
            items = task.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soughtitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MySoughtItemRecyclerViewAdapter(items, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mode != null && mode == "personalgames") {
            RequestForSearchTask task = new RequestForSearchTask();
            try {
                items = task.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            adapter.setItems(items);
        }
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
        void onListFragmentInteraction(SoughtItem item);
    }

    public class RequestForSearchTask extends AsyncTask<Void, Void, List<SoughtItem>> {


        public RequestForSearchTask() {
            super();
        }

        @Override
        protected List<SoughtItem> doInBackground(Void... params) {
            List<SoughtItem> soughtItemList = new ArrayList<>();
            RestTemplate restTemplate = RestTemplateManager.create();
            HttpEntity headers = RestTemplateManager.authenticateRequest(getActivity());
            String url;
            String toSearch = auxObject instanceof String ? (String) auxObject : null;
            User user = auxObject instanceof User ? (User) auxObject : null;
            switch (mode) {
                case "user":
                    url = RestTemplateManager.getUrl(getActivity(), "user/search?toSearch=" + toSearch);
                    ResponseEntity<User[]> responseEntity;
                    responseEntity = restTemplate.exchange(url, HttpMethod.GET, headers, User[].class);
                    if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                        List<User> aux = Arrays.asList(responseEntity.getBody());
                        for (User u : aux) {
                            SoughtItem i = SoughtItem.createFromUser(u);
                            Bitmap bmp;
                            try {
                                String auxUrl = RestTemplateManager.getUrl(getActivity(), "image/download?filename=" + URLEncoder.encode(u.getProfilePicture(), "UTF-8").toString());
                                URLConnection connection = RestTemplateManager.getConnection(getActivity(), auxUrl);
                                bmp = BitmapFactory.decodeStream(connection.getInputStream());
                                i.setImage(bmp);
                            } catch (Exception e) {
                                bmp = null;
                                i.setImage(bmp);
                            }
                            soughtItemList.add(i);
                        }

                    }
                    break;
                case "personalgame":
                    url = RestTemplateManager.getUrl(getActivity(), "personalgame/search?toSearch=" + toSearch);
                    ResponseEntity<PersonalGame[]> responseEntity2;
                    responseEntity2 = restTemplate.exchange(url, HttpMethod.GET, headers, PersonalGame[].class);
                    if (responseEntity2.getStatusCode().equals(HttpStatus.OK)) {
                        List<PersonalGame> aux = Arrays.asList(responseEntity2.getBody());
                        for (PersonalGame pg : aux) {
                            SoughtItem i = SoughtItem.createFromPersonalGame(pg);
                            Bitmap bmp;
                            try {
                                String auxUrl = RestTemplateManager.getUrl(getActivity(), "image/download?filename=" + URLEncoder.encode(pg.getImages().get(0).getPathUrl(), "UTF-8").toString());
                                URLConnection connection = RestTemplateManager.getConnection(getActivity(), auxUrl);
                                bmp = BitmapFactory.decodeStream(connection.getInputStream());
                                i.setImage(bmp);
                            } catch (Exception e) {
                                bmp = null;
                                i.setImage(bmp);
                            }
                            soughtItemList.add(i);
                        }

                    }
                    break;
                case "game":
                    url = RestTemplateManager.getUrl(getActivity(), "game/search?toSearch=" + toSearch);
                    ResponseEntity<Game[]> responseEntity3;
                    responseEntity3 = restTemplate.exchange(url, HttpMethod.GET, headers, Game[].class);
                    if (responseEntity3.getStatusCode().equals(HttpStatus.OK)) {
                        List<Game> aux = Arrays.asList(responseEntity3.getBody());
                        for (Game g : aux) {
                            SoughtItem i = SoughtItem.createFromGame(g);
                            Bitmap bmp;
                            try {
                                URL auxUrl = new URL("http:" + g.getCoverUrl());
                                bmp = BitmapFactory.decodeStream(auxUrl.openConnection().getInputStream());
                                i.setImage(bmp);
                            } catch (Exception e) {
                                bmp = null;
                                i.setImage(bmp);
                            }
                            soughtItemList.add(i);
                        }
                    }
                    break;
                case "personalgames":
                    url = RestTemplateManager.getUrl(getActivity(), "personalgame/list?userId=" + user.getId());
                    ResponseEntity<PersonalGame[]> responseEntity4;
                    responseEntity4 = restTemplate.exchange(url, HttpMethod.GET, headers, PersonalGame[].class);
                    if (responseEntity4.getStatusCode().equals(HttpStatus.OK)) {
                        List<PersonalGame> aux = Arrays.asList(responseEntity4.getBody());
                        for (PersonalGame pg : aux) {
                            SoughtItem i = SoughtItem.createFromGame(pg.getGame());
                            i.setObject(pg);
                            Bitmap bmp;
                            try {
                                URL auxUrl = new URL("http:" + pg.getGame().getCoverUrl());
                                bmp = BitmapFactory.decodeStream(auxUrl.openConnection().getInputStream());
                                i.setImage(bmp);
                            } catch (Exception e) {
                                bmp = null;
                                i.setImage(bmp);
                            }
                            soughtItemList.add(i);
                        }
                    }
                    break;
                case "followers":
                    List<User> aux = new ArrayList<>(user.getFollowers());
                    for (User u : aux) {
                        SoughtItem i = SoughtItem.createFromUser(u);
                        Bitmap bmp;
                        try {
                            String auxUrl = RestTemplateManager.getUrl(getActivity(), "image/download?filename=" + URLEncoder.encode(u.getProfilePicture(), "UTF-8").toString());
                            URLConnection connection = RestTemplateManager.getConnection(getActivity(), auxUrl);
                            bmp = BitmapFactory.decodeStream(connection.getInputStream());
                            i.setImage(bmp);
                        } catch (Exception e) {
                            bmp = null;
                            i.setImage(bmp);
                        }
                        soughtItemList.add(i);
                    }
                    break;
                case "followees":
                    List<User> aux2 = new ArrayList<>(user.getFollowees());
                    for (User u : aux2) {
                        SoughtItem i = SoughtItem.createFromUser(u);
                        Bitmap bmp;
                        try {
                            String auxUrl = RestTemplateManager.getUrl(getActivity(), "image/download?filename=" + URLEncoder.encode(u.getProfilePicture(), "UTF-8").toString());
                            URLConnection connection = RestTemplateManager.getConnection(getActivity(), auxUrl);
                            bmp = BitmapFactory.decodeStream(connection.getInputStream());
                            i.setImage(bmp);
                        } catch (Exception e) {
                            bmp = null;
                            i.setImage(bmp);
                        }
                        soughtItemList.add(i);
                    }
                    break;
            }
            return soughtItemList;
        }


    }
}

