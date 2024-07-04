package com.dubr0vin.diary.views.selectType;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dubr0vin.diary.App;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.Logger;
import com.dubr0vin.diary.models.Type;
import com.dubr0vin.diary.views.addType.AddTypeFragment;
import com.dubr0vin.diary.views.editItem.EditItemFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class SelectTypeFragment extends Fragment {

    private TypeAdapter typeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_type, container, false);
        setHasOptionsMenu(true);

        RecyclerView recyclerView = view.findViewById(R.id.select_type_recycler_view);
        typeAdapter = new TypeAdapter(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(typeAdapter);

        MaterialButton addTypeButton = view.findViewById(R.id.select_type_add_type_button);
        addTypeButton.setOnClickListener(button -> showAddTypeFragment(typeAdapter));

        loadTypes();

        setSelectTypeToolbar();

        return view;
    }

    public boolean goBack(){
        Logger.d("SelectType goBack");
        getParentFragmentManager().popBackStack();
        EditItemFragment itemFragment = (EditItemFragment) getParentFragmentManager().findFragmentByTag(EditItemFragment.class.getName());
        itemFragment.setEditItemToolbar();
        return true;
    }

    public boolean saveTypeSelect(){
        if(typeAdapter.selectType == null) {
            Toast.makeText(requireContext(),"type not selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        getParentFragmentManager().popBackStack();
        EditItemFragment itemFragment = (EditItemFragment) getParentFragmentManager().findFragmentByTag(EditItemFragment.class.getName());
        itemFragment.setEditItemToolbar();
        itemFragment.updateSelectTypeButton(typeAdapter.selectType);
        return true;
    }

    public void setSelectTypeToolbar(){
        Logger.d("setSelectTypeToolbar");
        TextView toolbarTextView = requireActivity().findViewById(R.id.main_date_text);
        toolbarTextView.setText("select type");
    }

    public void loadTypes(){
        App.runInDBThread(() -> {
            List<Type> types = App.database.typeDao().getAllTypes();
            App.runInUI(() -> {
                boolean isChanged = !typeAdapter.types.isEmpty();
                typeAdapter.types = new ArrayList<>(types);
                if(isChanged) typeAdapter.notifyItemRangeChanged(0,types.size());
                else typeAdapter.notifyItemRangeInserted(0,types.size());
            });
        });
    }

    private void showAddTypeFragment(TypeAdapter adapter){
        getParentFragmentManager().beginTransaction()
                .hide(this)
                .add(R.id.main_container_layout, AddTypeFragment.newInstance(typeAdapter), AddTypeFragment.class.getName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("addType")
                .commit();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.select_type_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return id == android.R.id.home ? goBack() : id == R.id.select_type_toolbar_done && saveTypeSelect();
    }
}
