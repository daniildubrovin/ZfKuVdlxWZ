package com.dubr0vin.diary.views.editItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dubr0vin.diary.App;
import com.dubr0vin.diary.Logger;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.models.Color;
import com.dubr0vin.diary.models.Day;
import com.dubr0vin.diary.models.Hour;
import com.dubr0vin.diary.models.Type;
import com.dubr0vin.diary.views.mainList.MainListFragment;
import com.dubr0vin.diary.views.selectType.SelectTypeFragment;

public class EditItemFragment extends Fragment {
    private static Hour hour;
    private static Type type;
    private static Day day;
    private Button button;
    private EditText editText;

    public static EditItemFragment newInstance(Day day, Hour hour){
        Logger.d("editItemFragment newInstance " + hour);
        EditItemFragment editItemFragment = new EditItemFragment();
        editItemFragment.setDay(day);
        editItemFragment.setHour(hour);
        editItemFragment.setType(hour.type);
        return editItemFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);
        Logger.d(this + " " + "EditItemFragment  onCreateView");
        setHasOptionsMenu(true);

        editText = view.findViewById(R.id.edit_text_input_record_edit_text);
        editText.setText(hour.record);

        button = view.findViewById(R.id.edit_item_select_type_button);
        updateSelectTypeButton(type);

        button.setOnClickListener(buttonView -> {
            getParentFragmentManager().beginTransaction()
                    .hide(this)
                    .add(R.id.main_container_layout, new SelectTypeFragment(), SelectTypeFragment.class.getName())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack("selectType")
                    .commit();
        });

        setEditItemToolbar();
        return view;
    }

    private boolean goBack(){
        getParentFragmentManager().popBackStack();
        MainListFragment listFragment = (MainListFragment) getParentFragmentManager().findFragmentByTag(MainListFragment.class.getName());
        listFragment.setMainListToolbar();
        return true;
    }

    private boolean saveRecord(){
        if(type.name.equals("Nothing")) {
            Toast.makeText(requireContext(),"Type not selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        App.runInDBThread(() -> {
            day.hours.get(hour.value - 1).record = editText.getText().toString();
            day.hours.get(hour.value - 1).type = type;
            App.database.dayDao().update(day);
            App.runInUI(() -> {
                getParentFragmentManager().popBackStack();
                MainListFragment listFragment = (MainListFragment) getParentFragmentManager().findFragmentByTag(MainListFragment.class.getName());
                listFragment.setMainListToolbar();
                listFragment.loadDay();
            });
        });
        return true;
    }

    public void updateSelectTypeButton(Type selectedType){
        type = selectedType;

        Color color = type.color;
        if(type.color.equals(Color.White)) color = Color.Black;

        button.setText(type.name);
        button.setTextColor(color.getAndroidColor());
    }

    public void setEditItemToolbar(){
        Logger.d("setEditItemToolbar");
        TextView toolbarTextView = requireActivity().findViewById(R.id.main_date_text);
        toolbarTextView.setText(String.valueOf(hour.value));

        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_item_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return id == android.R.id.home ? goBack() : id == R.id.edit_item_toolbar_done && saveRecord();
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
