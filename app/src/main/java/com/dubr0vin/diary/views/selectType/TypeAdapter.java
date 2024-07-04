package com.dubr0vin.diary.views.selectType;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dubr0vin.diary.R;
import com.dubr0vin.diary.models.Type;

import java.util.ArrayList;

public class TypeAdapter extends RecyclerView.Adapter<TypeViewHolder> {

    public ArrayList<Type> types;
    public Type selectType;
    private final RecyclerView recyclerView;

    public TypeAdapter(RecyclerView recyclerView) {
        this.types = new ArrayList<>();
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        Type type = types.get(position);
        holder.checkBox.setChecked(type.equals(selectType));

        holder.materialCardView.setBackgroundColor(Color.rgb(type.color.r,type.color.g,type.color.b));
        holder.textView.setText(type.name);

        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                selectType = type;
                recyclerView.post(() -> notifyItemRangeChanged(0, types.size()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
