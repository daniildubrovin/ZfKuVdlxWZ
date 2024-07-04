package com.dubr0vin.diary.views.addType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dubr0vin.diary.App;
import com.dubr0vin.diary.Logger;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.Utility;
import com.dubr0vin.diary.models.Color;
import com.dubr0vin.diary.models.Type;
import com.dubr0vin.diary.views.selectType.SelectTypeFragment;
import com.dubr0vin.diary.views.selectType.TypeAdapter;
import com.google.android.material.button.MaterialButton;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;

public class AddTypeFragment extends Fragment {
    private TypeAdapter typeAdapter;
    public static Color color;
    private EditText editText;

    public static AddTypeFragment newInstance(TypeAdapter typeAdapter) {
        AddTypeFragment typeFragment = new AddTypeFragment();
        typeFragment.typeAdapter = typeAdapter;
        AddTypeFragment.color = Color.White;

        App.runInDBThread(() -> {
            AddTypeFragment.color = Utility.getRandomUniqueColor(getColorsFromTypes(App.database.typeDao().getAllTypes()));
        });

        return typeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_type, container, false);
        setHasOptionsMenu(true);
        setAddTypeToolbar();

        editText = view.findViewById(R.id.add_new_type_edit_text);
        MaterialButton chooseColorButton = view.findViewById(R.id.add_new_type_select_color_button);
        updateColorButton(chooseColorButton);
        chooseColorButton.setOnClickListener(button -> {
            new ColorPickerDialog.Builder(requireContext())
                    .attachAlphaSlideBar(false)
                    .attachBrightnessSlideBar(false)
                    .setTitle("select type color")
                    .setPositiveButton("ok", (ColorEnvelopeListener) (envelope, fromUser) -> {
                        color = new Color(
                                envelope.getArgb()[1],
                                envelope.getArgb()[2],
                                envelope.getArgb()[3]);
                        updateColorButton(chooseColorButton);
                    })
                    .setNegativeButton("cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        });


        return view;
    }

    private void updateColorButton(MaterialButton chooseColorButton){
        chooseColorButton.setText("color: " + color.toString());
        Color buttonColor = color;
        if(buttonColor.r < 50 && buttonColor.g < 50 && buttonColor.b < 50) buttonColor = Color.White;
        chooseColorButton.setBackgroundColor(buttonColor.getAndroidColor());
    }

    public boolean goBack(){
        Logger.d("SelectType goBack");
        getParentFragmentManager().popBackStack();
        SelectTypeFragment selectTypeFragment = (SelectTypeFragment) getParentFragmentManager().findFragmentByTag(SelectTypeFragment.class.getName());
        selectTypeFragment.setSelectTypeToolbar();
        return true;
    }

    public boolean saveNewType(){
        if(editText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "the text is not entered", Toast.LENGTH_SHORT).show();
            return false;
        }

        App.runInDBThread(() -> {
            Type newType = new Type(editText.getText().toString(), color);
            App.database.typeDao().insert(newType);
            App.runInUI(() -> {
                getParentFragmentManager().popBackStack();
                SelectTypeFragment selectTypeFragment = (SelectTypeFragment) getParentFragmentManager().findFragmentByTag(SelectTypeFragment.class.getName());
                selectTypeFragment.setSelectTypeToolbar();
                selectTypeFragment.loadTypes();
            });
        });

        return true;
    }

    public void setAddTypeToolbar(){
        Logger.d("setSelectTypeToolbar");
        TextView toolbarTextView = requireActivity().findViewById(R.id.main_date_text);
        toolbarTextView.setText("adding a new type");
    }

    private static ArrayList<Color> getColorsFromTypes(List<Type> types){
        ArrayList<Color> colors = new ArrayList<>();
        for (Type type: types) colors.add(type.color);
        return colors;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_new_type_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return id == android.R.id.home ? goBack() : id == R.id.add_new_type_done && saveNewType();
    }
}
