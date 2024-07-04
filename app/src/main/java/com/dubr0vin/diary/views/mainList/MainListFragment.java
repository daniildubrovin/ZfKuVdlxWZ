package com.dubr0vin.diary.views.mainList;

import static com.dubr0vin.diary.Utility.generateEmptyHours;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dubr0vin.diary.App;
import com.dubr0vin.diary.Logger;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.db.DayDao;
import com.dubr0vin.diary.models.Date;
import com.dubr0vin.diary.models.Day;
import com.dubr0vin.diary.views.editItem.EditItemFragment;
import com.dubr0vin.diary.views.settings.MainSettingsFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.List;

public class MainListFragment extends Fragment {
    private static Date date;
    private DayAdapter dayAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        Logger.d(this + " " + "MainList onCreateView");
        setHasOptionsMenu(true);

        progressBar = view.findViewById(R.id.main_progress_bar);
        dayAdapter = new DayAdapter(getParentFragmentManager());
        recyclerView = view.findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(dayAdapter);

        loadDay();

        return view;
    }

    public void setMainListToolbar(){
        Logger.d("setMainListToolbar");

        TextView toolbarTextView = requireActivity().findViewById(R.id.main_date_text);
        toolbarTextView.setText(dayAdapter.day.date.toString());
        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(null);
    }

    public void loadDay(){
        setProgressBar(true);
        App.runInDBThread(() -> {
            DayDao dayDao = App.database.dayDao();
            if(date == null) date = new Date(Calendar.getInstance());
            Day day = dayDao.getByDate(date);

            if(day == null){
                day = new Day(date, generateEmptyHours());
                dayDao.insert(day);
            }

            showEditFragmentFromNotification(day);

            Day final_day = day;
            App.runInUI(() -> {
                boolean isUpdated = !dayAdapter.hours.isEmpty();
                dayAdapter.day = final_day;
                dayAdapter.hours = final_day.hours;
                if(getParentFragmentManager().findFragmentByTag(EditItemFragment.class.getName()) == null) setMainListToolbar();
                Logger.d("dayAdapter notifyItemRangeInserted    " + dayAdapter.hours.size());
                setProgressBar(false);
                if(isUpdated) dayAdapter.notifyItemRangeChanged(0,dayAdapter.hours.size());
                else dayAdapter.notifyItemRangeInserted(0, dayAdapter.hours.size());
            });
        });
    }

    private void showEditFragmentFromNotification(Day day){
        Integer hourFromNotification = (Integer) requireActivity().getIntent().getSerializableExtra("hour");
        if(hourFromNotification != null) {
            requireActivity().getIntent().putExtra("hour", (Integer) null);
            getParentFragmentManager().beginTransaction()
                    .hide(this)
                    .add(R.id.main_container_layout, EditItemFragment.newInstance(day, day.hours.get(hourFromNotification)), EditItemFragment.class.getName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack("editItem")
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.main_toolbar_calendar){
            showDatePickerFragment();
            return true;
        }
        else if(item.getItemId() == R.id.main_toolbar_settings){
            showSettingsFragment();
            return true;
        }
        return false;
    }

    private void showSettingsFragment(){
        getParentFragmentManager().beginTransaction()
                .hide(this)
                .add(R.id.main_container_layout, new MainSettingsFragment(), MainSettingsFragment.class.getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("settings")
                .commit();
    }

    private void showDatePickerFragment(){
        App.runInDBThread(() -> {
            List<Day> dayList = App.database.dayDao().getAllDays();
            CalendarConstraints.Builder builder = new CalendarConstraints.Builder().setValidator(new DateValidatorSavedDays(dayList));
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(builder.build())
                    .setSelection(date.getCalendarInstance().getTimeInMillis())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar selCal = Calendar.getInstance();
                selCal.setTimeInMillis(selection);
                date = new Date(selCal.get(Calendar.YEAR), selCal.get(Calendar.MONTH) + 1, selCal.get(Calendar.DATE));
                for(Day _day: dayList) {
                    if(date.equals(_day.date)){
                        dayAdapter.day = _day;
                        dayAdapter.hours = _day.hours;
                        setMainListToolbar();
                        dayAdapter.notifyItemRangeChanged(0,_day.hours.size());
                        break;
                    }
                }

            });
            App.runInUI(() -> datePicker.show(getParentFragmentManager(), "calendar"));
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar, menu);
    }

    public void setProgressBar(boolean progress){
        progressBar.setVisibility(progress ? View.VISIBLE : View.INVISIBLE);
        recyclerView.setVisibility(progress ? View.INVISIBLE : View.VISIBLE);
    }
}
