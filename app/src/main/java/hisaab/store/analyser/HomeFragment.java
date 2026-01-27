package hisaab.store.analyser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;


public class HomeFragment extends Fragment {

    //Configuration config;
    //AdjustSizeConfiguration displaysize;


    Utils utils;


    LinearLayout additemlayout,viewhisaablayout,monthlylayout,yearlylayout,addbalance,incomeHistoryLayout,incomeAnalyticsLayout;

    LinearLayout homelayout,balanceCard;

    ImageView about;
    ShowInterstitialAd interstitialAd;

    AdView adView;

    TextView expensetitle,spendingTitle,balanceTitle,additemtitle,viewhisaabtitle,monthlytitle,yearlytitle,incomehistorytitle,incomeanalyticstitle;
    RelativeLayout profileLayout,analyticslayout;


    ImageView user_img,additemicon,viewhisaabicon,monthlyicon,yearlyicon,incomeanalyticsicon,incomehistoryicon;
    TextView username;
    ViewGroup root;
    SharedPreferences spitem,splogin,sptheme;
    ScrollView homescroll;
    View view;
    public static UserViewModel viewModel;
    String head="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home, container, false);


        try {
            root = (ViewGroup) requireActivity().getWindow().getDecorView().getRootView();
            incomeHistoryLayout=view.findViewById(R.id.home_incomehistory_layout);
            incomeAnalyticsLayout=view.findViewById(R.id.home_incomeanalytics_layout);
            username = view.findViewById(R.id.home_user_txt);
            homescroll=view.findViewById(R.id.homescrolllayout);
            adView=view.findViewById(R.id.fragmmemt_home_add);
            expensetitle=view.findViewById(R.id.home_expensetitle);
            spendingTitle=view.findViewById(R.id.home_expenseanalyticstitle);
            balanceTitle=view.findViewById(R.id.txtTotalBalance);
            about=view.findViewById(R.id.about_home);
            interstitialAd = new ShowInterstitialAd(requireActivity());
            interstitialAd.loadAd();

            additemicon=view.findViewById(R.id.additemicon_home);
            viewhisaabicon=view.findViewById(R.id.viewhisaab_icon_home);
            monthlyicon=view.findViewById(R.id.monthlyicon_home);
            yearlyicon=view.findViewById(R.id.yearlyicon_home);
            incomeanalyticsicon=view.findViewById(R.id.incomeanalytics_icon_home);
            incomehistoryicon=view.findViewById(R.id.incomehistory_icon_home);
         //   displaysize = new AdjustSizeConfiguration(getActivity());
           // config = getResources().getConfiguration();

            utils = new Utils();
            viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            homelayout = view.findViewById(R.id.homelayout);
            addbalance = view.findViewById(R.id.home_addbalance);
            profileLayout=view.findViewById(R.id.fragmenthome_profile_layout);
            user_img = view.findViewById(R.id.home_user_img);
            analyticslayout=view.findViewById(R.id.analyticsCard);
            balanceCard=view.findViewById(R.id.balanceCard);
            additemtitle=view.findViewById(R.id.additemtitle_home);
            viewhisaabtitle=view.findViewById(R.id.viewhisaab_title);
            monthlytitle=view.findViewById(R.id.monthlytitle_home);
            yearlytitle=view.findViewById(R.id.yearlytitle_home);
            incomehistorytitle=view.findViewById(R.id.incomehistory_title_home);
            incomeanalyticstitle=view.findViewById(R.id.incomeanalytics_home);

            sptheme = requireActivity().getSharedPreferences("theme", Context.MODE_PRIVATE);
            splogin = requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
            spitem = requireActivity().getSharedPreferences("item", Context.MODE_PRIVATE);


            additemlayout = view.findViewById(R.id.home_additem_layout);
            monthlylayout = view.findViewById(R.id.home_monthly_layout);
            yearlylayout = view.findViewById(R.id.home_yearly_layout);
            viewhisaablayout = view.findViewById(R.id.home_viewhisaab_layout);

            head = spitem.getString("user", "");
            username.setText("Hii "+splogin.getString("fullname", "Null").split(" ")[0]);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(),AboutPage.class);
                    startActivity(intent);
                }
            });

            randomMsg(view);
            changetheme();
            incomeAnalyticsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adcount();
                    Intent intent=new Intent(getContext(),IncomeAnalytics.class);
                    startActivity(intent);
                }
            });
            incomeHistoryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adcount();
                    Intent intent=new Intent(getContext(),display.class);
                    intent.putExtra("key","income");
                    startActivity(intent);

                }
            });
/*
            try {
                if (!splogin.getString(spitem.getString("user", "") + "img", "").equals("")) {
                    user_img.setImageBitmap(Utils.decodeStringToBitmap(splogin.getString(spitem.getString("user", "") + "img", "")));
                } else {
                    user_img.setImageResource(R.drawable.baseline_person_24);
                }
            } catch (Exception e) {
                user_img.setImageResource(R.drawable.baseline_person_24);
            }*/

            //changetheme();
            checkOrientation(view);


            additemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              openDisplay(getContext(), item.class);
                }
            });


            addbalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adcount();
                  openDisplay(getContext(), money.class);
                }
            });
            viewhisaablayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adcount();
                    Intent intent=new Intent(getContext(),display.class);
                    intent.putExtra("key","hisaab");
                    startActivity(intent);

                }
            });

            monthlylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    adcount();
                    openDisplay(getContext(), monthly.class);
                }
            });

            yearlylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adcount();
                    openDisplay(getContext(),Yearly.class);
                }
            });


            ButtonEffect buttonEffect = new ButtonEffect(requireContext());
            buttonEffect.buttonEffect(addbalance);
            buttonEffect.buttonEffect(viewhisaablayout);
            buttonEffect.buttonEffect(monthlylayout);
            buttonEffect.buttonEffect(yearlylayout);
            buttonEffect.buttonEffect(additemlayout);
            buttonEffect.buttonEffect(incomeHistoryLayout);
            buttonEffect.buttonEffect(incomeAnalyticsLayout);

            viewModel.getProfileImage().observe(getViewLifecycleOwner(), newImageBase64 -> {
                if (newImageBase64 != null && !newImageBase64.isEmpty()) {
                    user_img.setImageBitmap(Utils.decodeStringToBitmap(newImageBase64));
                }
            });

            viewModel.getUserName().observe(getViewLifecycleOwner(), newName -> {
                username.setText("Hii "+splogin.getString("fullname", "Null").split(" ")[0]);
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    utils.createWindowname(requireActivity(),root,username);
                   // viewModel.setUserName(spitem.getString("name", ""));
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

        return view;
    }


    public void openDisplay(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("head", head);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }


    private void changetheme() {
        SharedPreferences sp = getContext().getSharedPreferences("theme",getContext().MODE_PRIVATE);
        if (sp.getString("theme", "dark").equals("pink")) {

            homelayout.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            expensetitle.setTextColor(getResources().getColor(R.color.black));
            balanceTitle.setTextColor(getResources().getColor(R.color.black));
            spendingTitle.setTextColor(getResources().getColor(R.color.black));
            homescroll.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            //profileLayout.setBackgroundColor(getResources().getColor(R.color.primarylight));

            profileLayout.setBackground(getResources().getDrawable(R.drawable.homeprofilecardbackground));

            viewhisaablayout.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg2));
            additemlayout.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg2));
            analyticslayout.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg2));
            balanceCard.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg2));


            additemtitle.setTextColor(getResources().getColor(R.color.black));
            viewhisaabtitle.setTextColor(getResources().getColor(R.color.black));
            viewhisaabtitle.setTextColor(getResources().getColor(R.color.black));
            monthlytitle.setTextColor(getResources().getColor(R.color.black));
            yearlytitle.setTextColor(getResources().getColor(R.color.black));
            incomehistorytitle.setTextColor(getResources().getColor(R.color.black));
            incomeanalyticstitle.setTextColor(getResources().getColor(R.color.black));

            //icons

            viewhisaabicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);
            additemicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);
            incomehistoryicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);

            monthlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

            yearlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

            incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);

            incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);


            monthlylayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarylight)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                monthlylayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                monthlylayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            yearlylayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarylight)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                yearlylayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                yearlylayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }



            incomeHistoryLayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), androidx.cardview.R.color.cardview_light_background)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                incomeHistoryLayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                incomeHistoryLayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            incomeAnalyticsLayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), androidx.cardview.R.color.cardview_light_background)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                incomeAnalyticsLayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                incomeAnalyticsLayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            addbalance.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarylight4)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                addbalance.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                addbalance.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }


        }
  else{
            additemtitle.setTextColor(getResources().getColor(R.color.white));
            viewhisaabtitle.setTextColor(getResources().getColor(R.color.white));
            viewhisaabtitle.setTextColor(getResources().getColor(R.color.white));
            monthlytitle.setTextColor(getResources().getColor(R.color.white));
            yearlytitle.setTextColor(getResources().getColor(R.color.white));
            incomehistorytitle.setTextColor(getResources().getColor(R.color.white));
            incomeanalyticstitle.setTextColor(getResources().getColor(R.color.white));


            balanceCard.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg_dark));
            analyticslayout.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg_dark));
            viewhisaablayout.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg_dark));
            additemlayout.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg_dark));
            profileLayout.setBackground(getResources().getDrawable(R.drawable.homeprofilecardbackground_dark));
            homelayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
            homescroll.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
            expensetitle.setTextColor(getResources().getColor(R.color.white));
            balanceTitle.setTextColor(getResources().getColor(R.color.white));
            spendingTitle.setTextColor(getResources().getColor(R.color.white));


           //icon

            viewhisaabicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);
            additemicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);
            incomehistoryicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

            monthlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

            yearlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

            incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

            incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);


            monthlylayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarydark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                monthlylayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                monthlylayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            yearlylayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarydark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                yearlylayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                yearlylayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }



            incomeHistoryLayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                incomeHistoryLayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                incomeHistoryLayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            incomeAnalyticsLayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                incomeAnalyticsLayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                incomeAnalyticsLayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            addbalance.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarydark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                addbalance.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                addbalance.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        changetheme();
        randomMsg(view);
        try {


            if (!splogin.getString(spitem.getString("user", "") + "img", "").equals("")) {
                user_img.setImageBitmap(Utils.decodeStringToBitmap(splogin.getString(spitem.getString("user", "") + "img", "")));
                username.setText("Hii "+splogin.getString("fullname", "Null").split(" ")[0]);
            } else {
                user_img.setImageResource(R.drawable.baseline_person_24);
            }
        } catch (Exception e) {
            user_img.setImageResource(R.drawable.baseline_person_24);
        }
    }
    public void adcount() {

        int count = spitem.getInt("adcount", 0);
        count++;

        if (count >= 20) {


            interstitialAd.showad();

            // Do NOT reset here; it will be reset when ad actually shows
        } else {
            // Save updated count

            spitem.edit().putInt("adcount", count).apply();
        }
    }


    private void checkOrientation(View view) {
        Configuration config= getResources().getConfiguration();
        LinearLayout layout = view.findViewById(R.id.innerhomelayout);
        AdjustSizeConfiguration displaysize=new AdjustSizeConfiguration(requireActivity());


        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(layout,60);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {


//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }



    public void randomMsg(View view){
        // 1. Define the array of subtitles
        String[] subtitles = {
                "Spend wisely save more",
                "Make every rupee count",
                "Analyse your expenses daily",
                "Track where your money goes",
                "Control your cash flow",
                "Save more from every income",
                "Understand your spending patterns",
                "Plan today for a better tomorrow",
                "Regularly check your expenses graph",
                "Update your expenses regularly",
                "Never miss adding record",
                "Do analyse your expenses",
                "Track to plan profit",
                "Know your profit and loss",
                "Every spend matters keep track"
        };


// 2. Generate a random index from 0 to 9
        Random random = new Random();
        int randomIndex = random.nextInt(subtitles.length); // returns 0 to 9

// 3. Set the text to a TextView
        TextView subtitleTextView = view.findViewById(R.id.home_user_subtxt);
        subtitleTextView.setText(subtitles[randomIndex]);

    }
}