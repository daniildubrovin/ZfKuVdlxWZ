package com.dubr0vin.diary.views;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityManagerCompat;

import com.dubr0vin.diary.Logger;
import com.dubr0vin.diary.R;
import com.dubr0vin.diary.views.addType.AddTypeFragment;
import com.dubr0vin.diary.views.editItem.EditItemFragment;
import com.dubr0vin.diary.views.mainList.MainListFragment;
import com.dubr0vin.diary.views.selectType.SelectTypeFragment;
import com.dubr0vin.diary.views.settings.MainSettingsFragment;
import com.dubr0vin.diary.views.settings.NotificationSettingsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.main_toolbar));

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.main_container_layout, new MainListFragment(), MainListFragment.class.getName())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainListFragment listFragment = (MainListFragment) getSupportFragmentManager().findFragmentByTag(MainListFragment.class.getName());
        EditItemFragment itemFragment = (EditItemFragment) getSupportFragmentManager().findFragmentByTag(EditItemFragment.class.getName());
        SelectTypeFragment typeFragment = (SelectTypeFragment) getSupportFragmentManager().findFragmentByTag(SelectTypeFragment.class.getName());
        AddTypeFragment addTypeFragment = (AddTypeFragment) getSupportFragmentManager().findFragmentByTag(AddTypeFragment.class.getName());
        MainSettingsFragment settingsFragment = (MainSettingsFragment) getSupportFragmentManager().findFragmentByTag(MainSettingsFragment.class.getName());
        NotificationSettingsFragment notificationSettingsFragment = (NotificationSettingsFragment) getSupportFragmentManager().findFragmentByTag(NotificationSettingsFragment.class.getName());

        //if I switch from editItemFragment to MainListFragment
        if(itemFragment == null && listFragment != null) listFragment.setMainListToolbar();
        //if I switch from SelectTypeFragment to EditItemFragment
        else if(typeFragment == null && itemFragment != null) itemFragment.setEditItemToolbar();
        else if(addTypeFragment == null && typeFragment != null) typeFragment.setSelectTypeToolbar();
        else if(settingsFragment == null && listFragment != null) listFragment.setMainListToolbar();
        else if(notificationSettingsFragment == null && settingsFragment != null) settingsFragment.setSettingsToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}