package com.dubr0vin.diary.views.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dubr0vin.diary.Logger;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.Utility;
import com.dubr0vin.diary.views.mainList.MainListFragment;

public class MainSettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setSettingsToolbar();
        setHasOptionsMenu(true);

        view.findViewById(R.id.settings_notifications_button).setOnClickListener(notifications_button -> {
            showSettingsFragment(new NotificationSettingsFragment());
        });

        view.findViewById(R.id.settings_language_button).setOnClickListener(language_button -> {

        });

        view.findViewById(R.id.settings_about_button).setOnClickListener(about_button -> {

        });

        return view;
    }

    private void showSettingsFragment(Fragment fragment){
        getParentFragmentManager().beginTransaction()
                .hide(this)
                .add(R.id.main_container_layout, fragment, Utility.getFragmentName(fragment))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("settings")
                .commit();
    }

    public void setSettingsToolbar(){
        TextView toolbarTextView = requireActivity().findViewById(R.id.main_date_text);
        toolbarTextView.setText("SETTINGS");

        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Logger.d("exit from settings framgnet");
            getParentFragmentManager().popBackStack();
            MainListFragment listFragment = (MainListFragment) getParentFragmentManager().findFragmentByTag(MainListFragment.class.getName());
            listFragment.setMainListToolbar();
            listFragment.loadDay();
            return true;
        }
        return false;
    }
}
