package es.egames.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.egames.R;
import es.egames.forms.SoughtItem;
import es.egames.model.PersonalGame;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExchangeGameFragment extends Fragment {

    private List<PersonalGame> personalGames;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExchangeGameFragment() {
    }

    public static ExchangeGameFragment newInstance(ArrayList<PersonalGame> personalGames) {
        ExchangeGameFragment fragment = new ExchangeGameFragment();
        Bundle args = new Bundle();
        args.putSerializable("personalgames", personalGames);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            personalGames = (List<PersonalGame>) getArguments().getSerializable("personalgames");
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
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            List<SoughtItem> soughtItemList = new ArrayList<>();
            MyGameDetailsFormRecyclerViewAdapter.RequestForImageTask requestForImageTask = new MyGameDetailsFormRecyclerViewAdapter.RequestForImageTask();
            for (PersonalGame pg : personalGames) {
                SoughtItem soughtItem = SoughtItem.createFromGame(pg.getGame());
                try {
                    soughtItem.setImage(requestForImageTask.execute(pg.getGame().getCoverUrl()).get());
                } catch (InterruptedException | ExecutionException e) {
                    soughtItem.setImage(null);
                }
                soughtItemList.add(soughtItem);
            }
            recyclerView.setAdapter(new MySoughtItemRecyclerViewAdapter(soughtItemList, null));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } //else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(SoughtItem item);
    }

}

