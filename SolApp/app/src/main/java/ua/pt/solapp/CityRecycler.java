package ua.pt.solapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CityRecycler extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<String> cities;
    private CityAdapter mAdapter;
    public Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private View currentView;

    //Search view
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_show_cities, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.cities_list);

        mAdapter = new CityAdapter(getActivity(), cities, getActivity());

        final CityAdapter adapter = mAdapter;

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);

        cities = new ArrayList<String>();
        for (String city : MainActivity.markers.keySet()) cities.add(city);
        Collections.sort(cities, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                o1 = Normalizer.normalize(o1, Normalizer.Form.NFD);
                o2 = Normalizer.normalize(o2, Normalizer.Form.NFD);
                return o1.compareTo(o2);
            }
        });
        adapter.setData(cities);
        adapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.currentView = view;
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Search City");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);

        searchView = (SearchView) getActivity().findViewById(R.id.search_city);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.equals("") || s.equals(" "))
                {
                    mAdapter.setData(cities);
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    ArrayList<String> temp = new ArrayList<>();
                    for (String c : cities)
                        if (Normalizer.normalize(c, Normalizer.Form.NFD).toLowerCase().startsWith(Normalizer.normalize(s, Normalizer.Form.NFD).toLowerCase()))
                            temp.add(c);

                    mAdapter.setData(temp);
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("") || s.equals(" "))
                {
                    mAdapter.setData(cities);
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    ArrayList<String> temp = new ArrayList<>();
                    for (String c : cities)
                        if (Normalizer.normalize(c, Normalizer.Form.NFD).toLowerCase().startsWith(Normalizer.normalize(s, Normalizer.Form.NFD).toLowerCase()))
                            temp.add(c);

                    mAdapter.setData(temp);
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

    }



}
