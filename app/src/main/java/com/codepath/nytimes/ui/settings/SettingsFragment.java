package com.codepath.nytimes.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.codepath.nytimes.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    Switch darkModeSwitch;
    FrameLayout flContainer;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        darkModeSwitch = (Switch) view.findViewById(R.id.sDarkMode);
        flContainer = (FrameLayout) view.findViewById(R.id.flContainer);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        darkModeSwitch.setChecked(sharedPreferences.getBoolean("isChecked", false));
        flContainer.setBackgroundColor(Color.parseColor(sharedPreferences.getString("BackgroundColor", "#FFFFFF")));
        darkModeSwitch.setTextColor(Color.parseColor(sharedPreferences.getString("TextColor", "#121212")));
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked", b);
                if (b) {

                    editor.putString("BackgroundColor", "#121212");
                    editor.putString("TextColor", "#FFFFFF");
                    editor.putString("BorderColor", "#1C2833");

                    editor.apply();
                    flContainer.setBackgroundColor(Color.parseColor("#121212"));
                    darkModeSwitch.setTextColor(Color.parseColor("#FFFFFF"));

                } else {
                    editor.putString("BackgroundColor", "#FFFFFF");
                    editor.putString("TextColor", "#121212");
                    editor.putString("BorderColor", "#dddddd");
                    editor.apply();
                    flContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    darkModeSwitch.setTextColor(Color.parseColor("#121212"));
                }
            }
        });
        getActivity().setTitle(getString(R.string.action_bar_settings));
        // Inflate the layout for this fragment
        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}