package com.dubr0vin.diary.views.selectType;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dubr0vin.diary.R;
import com.google.android.material.card.MaterialCardView;

public class TypeViewHolder extends RecyclerView.ViewHolder {
    public CheckBox checkBox;
    public TextView textView;
    public ImageButton imageButton;
    public MaterialCardView materialCardView;

    public TypeViewHolder(@NonNull View itemView) {
        super(itemView);
        materialCardView = itemView.findViewById(R.id.item_select_card_view);
        textView = itemView.findViewById(R.id.item_select_type_text_view);
        checkBox = itemView.findViewById(R.id.item_select_type_check_box);
    }
}
