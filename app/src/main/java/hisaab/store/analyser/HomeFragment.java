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

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

    LinearLayout balanceCard,analyticslayout;
    CoordinatorLayout homelayout;
    ImageView about;
    ShowInterstitialAd interstitialAd;
    TextView additemsubtitle,viewsubtitle,monthlysubtitle,yearlysubtitle,historysubtitle,analysticssubtitle;
    View vr1,vr2;

    AdView adView;

    TextView expensetitle,spendingTitle,balanceTitle,additemtitle,viewhisaabtitle,monthlytitle,yearlytitle,incomehistorytitle,incomeanalyticstitle;
    RelativeLayout profileLayout;
    FrameLayout monthlyframe,historyframe,analyticsframe,yearlyframe,additemframe,viewframe;


    ImageView user_img,additemicon,viewhisaabicon,monthlyicon,yearlyicon,incomeanalyticsicon,incomehistoryicon;
    TextView username;
    ViewGroup root;
    SharedPreferences spitem,splogin,sptheme;
    NestedScrollView homescroll;
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
            yearlyframe= view.findViewById(R.id.home_yearly_layout_frame);
            monthlyframe= view.findViewById(R.id.home_monthly_layout_frame);
            viewframe= view.findViewById(R.id.viewframe);
            additemframe= view.findViewById(R.id.additemframe);
            historyframe= view.findViewById(R.id.home_incomehistory_layout_frame);
            analyticsframe= view.findViewById(R.id.home_incomeanalytics_layout_frame);
            incomeAnalyticsLayout=view.findViewById(R.id.home_incomeanalytics_layout);
            username = view.findViewById(R.id.home_user_txt);
            homescroll=view.findViewById(R.id.homescrolllayout);
            adView=view.findViewById(R.id.fragmmemt_home_add);
            vr1=view.findViewById(R.id.vr1);
            vr2=view.findViewById(R.id.vr2);
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

            //subtitle ids
            additemsubtitle=view.findViewById(R.id.additem_subtitle);
            viewsubtitle=view.findViewById(R.id.viewhisaab_subtitle);
            yearlysubtitle=view.findViewById(R.id.yearly_subtitle);
            monthlysubtitle=view.findViewById(R.id.monthly_subtitle);
            historysubtitle=view.findViewById(R.id.incomehistory_subtitle);
            analysticssubtitle=view.findViewById(R.id.incomeanalytics_subtitle);

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
/*
            ButtonEffect buttonEffect = new ButtonEffect(requireContext());
            buttonEffect.buttonEffect(addbalance);
            buttonEffect.buttonEffect(viewhisaablayout);
            */

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

           // viewhisaabicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);
          //  additemicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);
           // incomehistoryicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);

          //  monthlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

          //  yearlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

          //  incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);

          //  incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.primarylight), PorterDuff.Mode.SRC_IN);

            additemsubtitle.setTextColor(getResources().getColor(R.color.subtitlecolorlightbg));
            viewsubtitle.setTextColor(getResources().getColor(R.color.subtitlecolorlightbg));
            yearlysubtitle.setTextColor(getResources().getColor(R.color.subtitlecolorlightbg));
            monthlysubtitle.setTextColor(getResources().getColor(R.color.subtitlecolorlightbg));
            historysubtitle.setTextColor(getResources().getColor(R.color.subtitlecolorlightbg));
            analysticssubtitle.setTextColor(getResources().getColor(R.color.subtitlecolorlightbg));

            monthlyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.framegreen)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                monthlyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                monthlyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            yearlyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.frameblue)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                yearlyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                yearlyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }


            additemframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.framegreen)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                additemframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                additemframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            viewframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.frameblue)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                viewframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                viewframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }


            historyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.framegreen)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                historyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                historyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            analyticsframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.frameorange)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                analyticsframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                analyticsframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            addbalance.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarylight4)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                addbalance.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                addbalance.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            vr1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.subtitlecolorlightbg));
            vr2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.subtitlecolorlightbg));
        }
  else{
            vr1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.subtitlecolordarkbg));
            vr2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.subtitlecolordarkbg));
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
            homelayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark33));
            homescroll.setBackgroundColor(getResources().getColor(R.color.backgrounddark33));
            expensetitle.setTextColor(getResources().getColor(R.color.white));
            balanceTitle.setTextColor(getResources().getColor(R.color.white));
            spendingTitle.setTextColor(getResources().getColor(R.color.white));

            additemsubtitle.setTextColor(getResources().getColor(R.color.subtitlecolordarkbg));
            viewsubtitle.setTextColor(getResources().getColor(R.color.subtitlecolordarkbg));
            yearlysubtitle.setTextColor(getResources().getColor(R.color.subtitlecolordarkbg));
            monthlysubtitle.setTextColor(getResources().getColor(R.color.subtitlecolordarkbg));
            historysubtitle.setTextColor(getResources().getColor(R.color.subtitlecolordarkbg));
            analysticssubtitle.setTextColor(getResources().getColor(R.color.subtitlecolordarkbg));
           //icon

          //  viewhisaabicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);
        //    additemicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);
            //incomehistoryicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

        //    monthlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

          //  yearlyicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

          //  incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);

          //  incomeanalyticsicon.setColorFilter(ContextCompat.getColor(getContext(),R.color.white), PorterDuff.Mode.SRC_IN);


            monthlyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark33)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                monthlyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                monthlyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            yearlyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark33)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                yearlyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                yearlyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }



            historyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark33)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                historyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                historyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            analyticsframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark33)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                analyticsframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                analyticsframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            addbalance.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.primarydark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                addbalance.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                addbalance.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }


            additemframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.framegreendark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                additemframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                additemframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            viewframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.framebluedark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                viewframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                viewframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }


            monthlyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.framegreendark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                monthlyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                monthlyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            yearlyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.framebluedark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                yearlyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                yearlyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }



            historyframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.frametealdark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                historyframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                historyframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }

            analyticsframe.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.frameorangedark)
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                analyticsframe.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            } else {
                analyticsframe.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
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
                "Accurate analysis",
                "Track expenses",
                "Manage your expenses"
        };

// 2. Generate a random index from 0 to 9
        Random random = new Random();
        int randomIndex = random.nextInt(subtitles.length); // returns 0 to 9

// 3. Set the text to a TextView
        TextView subtitleTextView = view.findViewById(R.id.home_user_subtxt);
        subtitleTextView.setText(subtitles[randomIndex]);

    }
}