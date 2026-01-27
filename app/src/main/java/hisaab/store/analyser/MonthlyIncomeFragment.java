package hisaab.store.analyser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthlyIncomeFragment extends Fragment {

    Spinner yearlySpinner;
    TextView tvTotalMonthlyIncomeValue;
    Switch fullgraphswitch;
    String curryear= new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

    LinearLayout layout;
    LottieAnimationView nodata;

    AdView adView;
    SharedPreferences sptheme;
    LineChart lineChart;
    String year=curryear;
    Utils utils;

    String[] yeararr = {
            "Select Year",
            "2025","2026","2027","2028","2029","2030",
            "2031","2032","2033","2034","2035","2036",
            "2037","2038","2039","2040","2041","2042",
            "2043","2044","2045","2046","2047","2048",
            "2049","2050"
    };


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monthly_income, container, false);
        sptheme = requireContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        lineChart = view.findViewById(R.id.lineChartMonthlyIncome);
        tvTotalMonthlyIncomeValue = view.findViewById(R.id.tvTotalMonthlyIncomeValue);
        yearlySpinner = view.findViewById(R.id.monthlyIncomeSpinnerYear);
        adView=view.findViewById(R.id.adViewincome_monthlyad);
        fullgraphswitch=view.findViewById(R.id.switchFullGraphMonthlyIncome);


        ImageView back=view.findViewById(R.id.monthlyincome_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        layout=view.findViewById(R.id.linegraphlayout_income2);
        nodata=view.findViewById(R.id.nodata_monthlyanalyticsincome);
        fullgraphswitch.setChecked(true);
        utils = new Utils();
       setupYearSpinner();
        yearlySpinner.setSelection(Integer.parseInt(curryear)-2024);
        setupMonthlyIncomeGraph(Integer.parseInt(year));

        fullgraphswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lineChart.fitScreen(); // 🔥 reset any zoom
                lineChart.setVisibleXRangeMaximum(12f);
                lineChart.moveViewToX(0); // go to first day
            } else {
                lineChart.setVisibleXRangeMaximum(6f);
                lineChart.moveViewToX(0);
            }

            lineChart.invalidate();
        });

        checkOrientation(view);
        changetheme(view);



        return view;
    }

    private void setupYearSpinner() {


        SharedPreferences sptheme=getContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        SpinnerAdapter1 adapterYear;
        if (sptheme.getString("theme", "dark").equals("pink")) {

             adapterYear = new SpinnerAdapter1(getContext(), R.layout.spinner_login_pink, yeararr);

        } else {

            adapterYear = new SpinnerAdapter1(getContext(), R.layout.spinner_login, yeararr);
        }

        yearlySpinner.setAdapter(adapterYear);
        yearlySpinner.setSelection(Integer.parseInt(curryear)-2024);



        yearlySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0){
                    year=String.valueOf(2024+position);
                }
                else{

                    year=curryear;
                    yearlySpinner.setSelection(Integer.parseInt(curryear)-2024);
                }


                    setupMonthlyIncomeGraph(Integer.parseInt(year));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        }

    private void setupMonthlyIncomeGraph(int year) {
        try {
            ArrayList<Entry> entries = new ArrayList<>();
            String[] monthLabels = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            long total = 0;

            for (int i = 0; i < 12; i++) {
                long income = 0;
                try {
                    income = Long.parseLong(utils.monthlyGet(monthLabels[i], requireActivity(), String.valueOf(year)));
                } catch (Exception ignored) {
                    layout.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }



                total += income;
                entries.add(new Entry(i, income));
            }

            if(total==0){
                layout.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }
            else{
                layout.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
                tvTotalMonthlyIncomeValue.setText("₹ " + total);
            }




            LineDataSet dataSet = new LineDataSet(entries, "Monthly Income (" + year + ")");
            dataSet.setColor(requireContext().getColor(android.R.color.holo_green_dark));
            dataSet.setCircleColor(requireContext().getColor(android.R.color.holo_green_dark));
            dataSet.setValueTextSize(10f);
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(5f);

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);

            // X-axis setup


            lineChart.setData(lineData);

            // X-axis
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            //lineChart.setVisibleXRangeMaximum(6f);
            lineChart.moveViewToX(0);
            //xAxis.setLabelCount(monthLabels.length, true);
            //xAxis.setAvoidFirstLastClipping(false);

            lineChart.getAxisRight().setEnabled(false);
            lineChart.getDescription().setEnabled(false);
            if (!fullgraphswitch.isChecked()) {
                lineChart.animateX(500);
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
            Toast.makeText(requireContext(), "Failed to load monthly income", Toast.LENGTH_SHORT).show();
        }
    }

    private void changetheme(View view) {

        String theme = sptheme.getString("theme", "dark");

        // Root layout
        RelativeLayout parentlayout = view.findViewById(R.id.monthlyIncomeRootLayout);

        // Header
        ImageView back = view.findViewById(R.id.monthlyincome_back);
        TextView title = view.findViewById(R.id.tvMonthlyTitle);

        // Spinner section
        LinearLayout spinnerlayout = view.findViewById(R.id.display_selection_layout);
        TextView spinnertitle = view.findViewById(R.id.tvSelectDateLabel);
        Spinner yearspinner = view.findViewById(R.id.monthlyIncomeSpinnerYear);

        // Full graph toggle
        LinearLayout fullGraphLayout = view.findViewById(R.id.fullGraphToggleLayout);
        TextView fullgraphtoggle_title = view.findViewById(R.id.tvFullGraphToggleLabel);
        Switch switchFullGraph = view.findViewById(R.id.switchFullGraphMonthlyIncome);

        // Total income card
        RelativeLayout incomelayout = view.findViewById(R.id.totalIncomeCard);
        TextView tincome_title = view.findViewById(R.id.tvTotalMonthlyLabel);
        TextView tincome_value = view.findViewById(R.id.tvTotalMonthlyIncomeValue);

        if (theme.equals("pink")) {
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            spinnerlayout.setBackground(getResources().getDrawable(R.drawable.spinnerlayoutbg_light));
            fullGraphLayout.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            incomelayout.setBackground(getResources().getDrawable(R.drawable.border6));

            title.setTextColor(getResources().getColor(R.color.black));
            fullgraphtoggle_title.setTextColor(getResources().getColor(R.color.black));
            spinnertitle.setTextColor(getResources().getColor(R.color.primarylight));
            tincome_title.setTextColor(getResources().getColor(R.color.black));

            back.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark));
            yearspinner.setPopupBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.white)));
            yearspinner.setBackground(getResources().getDrawable(R.drawable.border6));
            nodata.setAnimation(R.raw.nodata);
        } else {
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
            spinnerlayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));
            fullGraphLayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
            incomelayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));

            title.setTextColor(getResources().getColor(R.color.white));
            fullgraphtoggle_title.setTextColor(getResources().getColor(R.color.white));
            spinnertitle.setTextColor(getResources().getColor(R.color.white));
            tincome_title.setTextColor(getResources().getColor(R.color.white));
            nodata.setAnimation(R.raw.nograph);
            back.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            yearspinner.setPopupBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.spinnerdarkcolor)));
            yearspinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));
        }
    }


    private void checkOrientation(View view) {
        Configuration config= getResources().getConfiguration();
        LinearLayout spinnerlayout = view.findViewById(R.id.display_selection_layout);
        AdjustSizeConfiguration displaysize=new AdjustSizeConfiguration(requireActivity());


        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(layout,70);
            displaysize.setfixWidth(spinnerlayout,70);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {


//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }


}
