package hisaab.store.analyser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class YearlyIncomeFragment extends Fragment {

    Switch switchFullGraphYearlyIncome;
    TextView tvTotalIncomeValue;
    LineChart lineChart;
    Utils utils;

    LinearLayout layout;

    AdView adView;

    ImageView back;

    SharedPreferences sptheme;
    RelativeLayout tincomelayout,parentlayout;

    TextView toggletitle,title,tincometitle;
    LottieAnimationView nodata;

    String curryear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_yearly_income, container, false);
        sptheme = getContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        lineChart = view.findViewById(R.id.lineChartYearlyIncome);
        adView=view.findViewById(R.id.adViewincome_yearlyad);
        tvTotalIncomeValue = view.findViewById(R.id.tvTotalIncomeValueYearly);
        switchFullGraphYearlyIncome = view.findViewById(R.id.switchFullGraphYearlyIncome);

        tincomelayout=view.findViewById(R.id.totalIncomeCardYearly);
        parentlayout=view.findViewById(R.id.yearlyIncomeRootLayout);
        toggletitle=view.findViewById(R.id.tvFullGraphToggleLabelYearly);
        title=view.findViewById(R.id.tvYearlyTitle);
        tincometitle=view.findViewById(R.id.tvYearlyTitle_analytics);

        utils = new Utils();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        back=view.findViewById(R.id.yearlyincome_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        layout=view.findViewById(R.id.linegraphlayout_income3);
        nodata=view.findViewById(R.id.nodata_yearlyanalyticsincome);

        switchFullGraphYearlyIncome.setChecked(true);

        setupYearlyIncomeGraph();

        switchFullGraphYearlyIncome.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lineChart.fitScreen();
                lineChart.setVisibleXRangeMaximum(12f);
                lineChart.moveViewToX(0);
            } else {
                lineChart.setVisibleXRangeMaximum(6f);
                lineChart.moveViewToX(0);
            }
            lineChart.invalidate();
        });

        checkOrientation();
        changetheme();
        return view;
    }

    private void setupYearlyIncomeGraph() {
        try {
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> xLabels = new ArrayList<>();
            long total = 0;

            int startYear = 2024;
            int currentYear = Integer.parseInt(curryear);
            int yearCount = (currentYear - startYear) + 2;

            for (int i = 0; i < yearCount; i++) {
                int year = startYear + i;
                long income = 0;

                try {
                    income = Long.parseLong(utils.yearlyGet(String.valueOf(year), requireActivity()));
                } catch (Exception ignored) {
                    layout.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }



                total += income;

                entries.add(new Entry(i, income));
                xLabels.add(String.valueOf(year));
            }


            if(total==0){
                layout.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }
            else{
                layout.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
            }
            tvTotalIncomeValue.setText("₹ " + total);

            LineDataSet dataSet = new LineDataSet(entries, "Yearly Income");
            dataSet.setColor(requireContext().getColor(android.R.color.holo_green_dark));
            dataSet.setCircleColor(requireContext().getColor(android.R.color.holo_green_dark));
            dataSet.setValueTextSize(10f);
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(5f);

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);

            lineChart.getAxisRight().setEnabled(false);
            lineChart.getDescription().setEnabled(false);

            if (!switchFullGraphYearlyIncome.isChecked()) {
                lineChart.animateX(700);
            }



            YAxis leftAxis = lineChart.getAxisLeft();
            Legend legend = lineChart.getLegend();

            if (sptheme.getString("theme", "dark").equals("pink")) {
                int black = Color.BLACK;
                xAxis.setTextColor(black);
                leftAxis.setTextColor(black);
                lineChart.getAxisRight().setTextColor(black);
                xAxis.setGridColor(Color.GRAY);
                leftAxis.setGridColor(Color.GRAY);
                xAxis.setAxisLineColor(black);
                leftAxis.setAxisLineColor(black);
                legend.setTextColor(black);

                dataSet.setValueTextColor(Color.BLACK);

            } else {
                int white = Color.WHITE;
                xAxis.setTextColor(white);
                leftAxis.setTextColor(white);
                lineChart.getAxisRight().setTextColor(white);
                xAxis.setGridColor(Color.GRAY);
                leftAxis.setGridColor(Color.GRAY);
                xAxis.setAxisLineColor(white);
                leftAxis.setAxisLineColor(white);
                legend.setTextColor(white);
                dataSet.setValueTextColor(Color.WHITE);
            }


            lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            lineData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });


            lineChart.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to load yearly income"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void changetheme() {


        if (sptheme.getString("theme", "dark").equals("pink")) {
            tincomelayout.setBackground(getResources().getDrawable(R.drawable.border6));
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            toggletitle.setTextColor(getResources().getColor(R.color.primarylight));
            title.setTextColor(getResources().getColor(R.color.black));
            tincometitle.setTextColor(getResources().getColor(R.color.black));
            back.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark));
            nodata.setAnimation(R.raw.nodata);

        } else {
            tincomelayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
            toggletitle.setTextColor(getResources().getColor(R.color.white));
            title.setTextColor(getResources().getColor(R.color.white));
            tincometitle.setTextColor(getResources().getColor(R.color.white));
            back.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            nodata.setAnimation(R.raw.nograph);
        }
    }

    private void checkOrientation() {
        Configuration config= getResources().getConfiguration();
        AdjustSizeConfiguration displaysize=new AdjustSizeConfiguration(requireActivity());


        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(layout,70);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {


//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }


}
