package xyz.taouvw.mysdutools.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.taouvw.mysdutools.Fragment.Kb_Fragment;

public class KbAdapter extends FragmentStateAdapter {
    List<Kb_Fragment> lists = new ArrayList<>();

    public KbAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public KbAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public KbAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Kb_Fragment> fragmentList) {
        super(fragmentManager, lifecycle);
        this.lists = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return lists.get(position);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
