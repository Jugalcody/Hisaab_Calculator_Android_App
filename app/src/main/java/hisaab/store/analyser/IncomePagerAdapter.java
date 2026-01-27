package hisaab.store.analyser;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class IncomePagerAdapter extends FragmentStateAdapter {

    public IncomePagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new DailyIncomeFragment();
            case 1: return new MonthlyIncomeFragment();
            case 2: return new YearlyIncomeFragment();
        }
        return new DailyIncomeFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

