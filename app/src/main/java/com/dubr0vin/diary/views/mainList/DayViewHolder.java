package com.dubr0vin.diary.views.mainList;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dubr0vin.diary.R;

public class DayViewHolder extends RecyclerView.ViewHolder {
    public TextView hourTextView;
    public TextView recordTextView;
    public ImageButton editImageButton;

    public DayViewHolder(@NonNull View itemView) {
        super(itemView);

        hourTextView = itemView.findViewById(R.id.main_item_hour_text_view);
        recordTextView = itemView.findViewById(R.id.main_item_record_text_view);
        editImageButton = itemView.findViewById(R.id.main_item_image_button);
    }
}
