package com.dubr0vin.diary.views.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.dubr0vin.diary.App;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.Utility;
import com.dubr0vin.diary.models.Date;
import com.dubr0vin.diary.models.Settings;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class NotificationSettingsFragment extends Fragment {

    private Settings settings;

    public NotificationSettingsFragment(){
        App.runInDBThread(() -> settings = App.database.settingsDao().getSettings());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);
        setNotificationSettingsToolbar();
        setHasOptionsMenu(true);

        SwitchMaterial switchMaterial = view.findViewById(R.id.notification_settings_notify_every_hour_switch);
        switchMaterial.setChecked(settings.isNotifyEveryHour);
        switchMaterial.setOnCheckedChangeListener((compoundButton, b) -> {
            settings.isNotifyEveryHour = b;
            App.runInDBThread(() -> App.database.settingsDao().updateSettings(settings));
            Utility.createJobTask(requireContext(), settings);
        });

        Slider beginSlider = view.findViewById(R.id.beginning_sleep_slider);
        beginSlider.setValue(settings.beginSleepHour);
        TextView beginTextView = view.findViewById(R.id.notification_settings_beginning_of_sleep_value_text_view);
        beginTextView.setText(String.valueOf(settings.beginSleepHour));

        Slider endSlider = view.findViewById(R.id.end_sleep_slider);
        endSlider.setValue(settings.endSleepHour);
        TextView endTextView = view.findViewById(R.id.notification_settings_end_of_sleep_value_text_view);
        endTextView.setText(String.valueOf(settings.endSleepHour));

        TextView allTextView = view.findViewById(R.id.notification_settings_all_sleep_text_view);
        allTextView.setText(getStringForAllTextView(settings.beginSleepHour, settings.endSleepHour));

        beginSlider.addOnChangeListener((slider, value, fromUser) -> {
            settings.beginSleepHour = (int) value;
            App.runInDBThread(() -> App.database.settingsDao().updateSettings(settings));
            beginTextView.setText(String.valueOf(settings.beginSleepHour));
            allTextView.setText(getStringForAllTextView(settings.beginSleepHour,settings.endSleepHour));
            Utility.updateSleepHours(settings);
            Utility.createJobTask(requireContext(), settings);
        });

        endSlider.addOnChangeListener((slider, value, fromUser) -> {
            settings.endSleepHour = (int) value;
            App.runInDBThread(() -> App.database.settingsDao().updateSettings(settings));
            endTextView.setText(String.valueOf(settings.endSleepHour));
            allTextView.setText(getStringForAllTextView(settings.beginSleepHour, settings.endSleepHour));
            Utility.updateSleepHours(settings);
            Utility.createJobTask(requireContext(), settings);
        });


        return view;
    }

    private String getStringForAllTextView(int beginValue, int endValue){
        return getString(R.string.notification_settings_all_sleep_text_view) + " " + beginValue + " to " + endValue;
    }

    public void setNotificationSettingsToolbar(){
        TextView toolbarTextView = requireActivity().findViewById(R.id.main_date_text);
        toolbarTextView.setText("setting up notifications");

        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            MainSettingsFragment settingsFragment = (MainSettingsFragment) getParentFragmentManager().findFragmentByTag(MainSettingsFragment.class.getName());
            settingsFragment.setSettingsToolbar();
            getParentFragmentManager().popBackStack();
            return true;
        }
        return false;
    }
}
