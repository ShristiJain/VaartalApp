package com.example.vartalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapterClass extends FragmentPagerAdapter {
    public FragmentAdapterClass(@NonNull FragmentManager fm,int behaviour) {
        super(fm,behaviour);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ChatsFragment chatsFragment=new ChatsFragment();
                return chatsFragment;
            case 1:
                BusinessFragment businessFragment=new BusinessFragment();
                return businessFragment;
            case 2:
                ClassroomFragment classroomFragment=new ClassroomFragment();
                return classroomFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "MyChats";
            case 1:
                return "Business";
            case 2:
                return "Classroom";
            default:return null;
        }
    }
}
