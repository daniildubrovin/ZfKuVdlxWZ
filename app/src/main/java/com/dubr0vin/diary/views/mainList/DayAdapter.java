package com.dubr0vin.diary.views.mainList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.dubr0vin.diary.App;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.db.DayDao;
import com.dubr0vin.diary.models.Day;
import com.dubr0vin.diary.models.Hour;
import com.dubr0vin.diary.views.editItem.EditItemFragment;

import java.util.ArrayList;
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayViewHolder> {

    public Day day;
    public List<Hour> hours;
    public DayDao dayDao;
    public FragmentManager fragmentManager;

    public DayAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        hours = new ArrayList<>();
        dayDao = App.database.dayDao();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Hour hour = hours.get(position);

        holder.itemView.setBackgroundColor(hour.type.color.getAndroidColor());
        holder.hourTextView.setText(String.valueOf(hour.value));
        holder.recordTextView.setText(hour.record.length() > 20 ? hour.record.substring(0,20) : hour.record);
        holder.editImageButton.setOnClickListener(view -> showEditItemFragment(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    private void showEditItemFragment(int position){
        fragmentManager.beginTransaction()
                .hide(fragmentManager.findFragmentByTag(MainListFragment.class.getName()))
                .add(R.id.main_container_layout, EditItemFragment.newInstance(day, hours.get(position)), EditItemFragment.class.getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("editItem")
                .commit();
    }
}
