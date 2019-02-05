package com.hapen.navigationdrawertest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentHome extends Fragment {
    /*** Recycler View ***/
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        /*** Spinners ***/
        // Categories spinner
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner_categories);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.spinner_categories,
                android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);

        // Organization spinner
        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner_organizations);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.spinner_organizations,
                android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);

        /*** Recycler View ***/
        ArrayList<CardItem> cardItem = new ArrayList<>();
        cardItem.add(new CardItem(
                R.drawable.org_logo_nsbe,
                "National Society of Black Engineers",
                "Careers",
                "Careers, Conversations & Culture with Facebook",
                "2 Hours | Feb 01, 5:00 PM",
                R.drawable.event_flyer_facebook));
        cardItem.add(new CardItem(
                R.drawable.org_logo_swe,
                "Society of Women Engineers",
                "Company",
                "MacAfee Tour this Friday!",
                "5 Days | Feb 10, 8:00 PM",
                R.drawable.event_flyer_macafee));
        cardItem.add(new CardItem(
                R.drawable.org_logo_pvamu,
                "Office of Financial Aid",
                "Financial Aid",
                "FAFSA Application Due on March 15!",
                "7 Days | Feb 12, 4:30 PM",
                R.drawable.event_flyer_fafsa));
        cardItem.add(new CardItem(
                R.drawable.org_logo_nsbe,
                "National Society of Black Engineers",
                "Careers",
                "Careers, Conversations & Culture with Facebook",
                "2 Hours | Feb 01, 5:00 PM",
                R.drawable.event_flyer_facebook));
        cardItem.add(new CardItem(
                R.drawable.org_logo_swe,
                "Society of Women Engineers",
                "Company",
                "MacAfee Tour this Friday!",
                "5 Days | Feb 10, 8:00 PM",
                R.drawable.event_flyer_macafee));
        cardItem.add(new CardItem(
                R.drawable.org_logo_pvamu,
                "Office of Financial Aid",
                "Financial Aid",
                "FAFSA Application Due on March 15!",
                "7 Days | Feb 12, 4:30 PM",
                R.drawable.event_flyer_fafsa));

        mRecyclerView = v.findViewById(R.id.recycler_home);
        // Set to true if you it has a fixed size
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mAdapter = new RecyclerAdapter(cardItem);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }
}
