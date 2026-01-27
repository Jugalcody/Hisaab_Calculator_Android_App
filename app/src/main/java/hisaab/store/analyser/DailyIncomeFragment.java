package hisaab.store.analyser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DailyIncomeFragment extends Fragment {
    TextView tvTotalDailyIncomeValue,fullgraphtoggle_title,title,spinnertitle,tincome_title;
    Switch fullgraphswitch;


    ImageView back;

    RelativeLayout parentlayout,incomelayout;
    LineChart lineChart;
    LinearLayout layout,spinnerlayout;

    LottieAnimationView nodata;
    SharedPreferences sp;

    String curryear= new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
    String currmonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
    String[] montharr = {"Select Month","Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    String[] yeararr = {
            "Select Year",
            "2025","2026","2027","2028","2029","2030",
            "2031","2032","2033","2034","2035","2036",
            "2037","2038","2039","2040","2041","2042",
            "2043","2044","2045","2046","2047","2048",
            "2049","2050"
    };
    String user;
    ArrayAdapter<String> adapterMonth,adapterYear;
    Spinner monthspinner, yearspinner;
    SharedPreferences sptheme;

    String year=curryear,month=currmonth;
    boolean isMonthSelected=false,isYearSelected=false;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_income, container, false);

        try {
            sp = getContext().getSharedPreferences("item", requireContext().MODE_PRIVATE);
            user = sp.getString("user", "");
            spinnerlayout=view.findViewById(R.id.dailyincome_selection_layout);
            lineChart = view.findViewById(R.id.lineChartDailyIncome);
            spinnertitle=view.findViewById(R.id.dailyincome_spinner_title);
            tincome_title=view.findViewById(R.id.tvTotalSpendDailyIncomeLabel);
            incomelayout=view.findViewById(R.id.dailyincomeanalytics_fragment_datalayout);
            title=view.findViewById(R.id.tvDailyTitle);
            fullgraphtoggle_title=view.findViewById(R.id.dailyincomeanalytics_fragment_toggle_title);
            fullgraphswitch = view.findViewById(R.id.switchfullgraphdailyincom);
            tvTotalDailyIncomeValue = view.findViewById(R.id.tvTotalSpendDailyIncomeValue);
            monthspinner = view.findViewById(R.id.dailyincomspinnermonth);
            yearspinner = view.findViewById(R.id.dailyincomspinneryear);
            parentlayout=view.findViewById(R.id.dailyincomeanalytics_fragment_parent);
            layout = view.findViewById(R.id.linegraphlayout_income1);
            nodata = view.findViewById(R.id.nodata_dailyanalyticsincome);



            sptheme = getContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
            if (sptheme.getString("theme", "dark").equals("pink")) {
                adapterMonth = new SpinnerAdapter1(getContext(), R.layout.spinner_login_pink, montharr);
                adapterYear = new SpinnerAdapter1(getContext(), R.layout.spinner_login_pink, yeararr);

            } else {
                adapterMonth = new SpinnerAdapter1(getContext(), R.layout.spinner_login, montharr);
                adapterYear = new SpinnerAdapter1(getContext(), R.layout.spinner_login, yeararr);
            }

            yearspinner.setAdapter(adapterYear);
            monthspinner.setAdapter(adapterMonth);

            yearspinner.setSelection(Integer.parseInt(curryear) - 2024);
            monthspinner.setSelection(Integer.parseInt(currmonth));


            back = view.findViewById(R.id.dailyincome_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().onBackPressed();
                }
            });

            changetheme();
            fullgraphswitch.setChecked(true);

            fullgraphswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    lineChart.fitScreen(); // 🔥 reset any zoom
                    lineChart.setVisibleXRangeMaximum(31f);
                    lineChart.moveViewToX(0); // go to first day
                } else {
                    lineChart.setVisibleXRangeMaximum(6f);
                    lineChart.moveViewToX(0);
                }

                lineChart.invalidate();
            });


            setupDailyIncomeGraph();
            yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != 0) {
                        isYearSelected = true;
                        year = String.valueOf(2024 + position);
                    } else {

                        isYearSelected = false;
                        year = curryear;
                        yearspinner.setSelection(Integer.parseInt(curryear) - 2024);
                    }

                    if (isMonthSelected) {
                        setupDailyIncomeGraph();
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    isYearSelected = false;
                    year = curryear;
                    yearspinner.setSelection(Integer.parseInt(curryear) - 2024);

                    if (isMonthSelected) {
                        setupDailyIncomeGraph();
                    }
                }
            });


            monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position != 0) {
                        isMonthSelected = true;
                        month = String.valueOf(position);

                    } else {

                        isMonthSelected = false;
                        month = currmonth;
                        monthspinner.setSelection(Integer.parseInt(currmonth));
                    }


                    if (isYearSelected) {
                        setupDailyIncomeGraph();
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    isMonthSelected = false;
                    month = currmonth;
                    monthspinner.setSelection(Integer.parseInt(currmonth));
                    setupDailyIncomeGraph();
                }
            });


        }catch (Exception e){
                        Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }

        checkOrientation(view);

        return view;
    }
    private void setupDailyIncomeGraph() {
        try {
            Utils utils=new Utils();
            File path = requireContext().getFilesDir();

           File file = new File(path, user+montharr[Integer.parseInt(month)]+year+"money_history.txt");


            if (!file.exists()) {
                tvTotalDailyIncomeValue.setText("₹0");
                layout.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.GONE);
                return;
            }

            lineChart.setVisibility(View.VISIBLE);
            String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            ArrayList<Long> dailyIncome = new ArrayList<>();
            ArrayList<String> dayLabels = new ArrayList<>();
            long total = 0;

            Calendar cal = Calendar.getInstance();
            int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            // Initialize all days with 0
            for (int i = 0; i < maxDays; i++) {
                dailyIncome.add(0L);
                dayLabels.add(String.valueOf(i + 1));
            }

            // Read file
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println("soo"+line);
                String[] arr = line.trim().split("#");

                if (arr.length == 4) {
                    try {
                        String dateStr = arr[2].trim();
                        int day = Integer.parseInt(dateStr.split("-")[0]);
                        long amount = Long.parseLong(arr[1].trim().substring(3));
                        if (day <= maxDays) {
                            dailyIncome.set(day - 1, dailyIncome.get(day - 1) + amount);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            br.close();
            fis.close();

            // Calculate total income for the month (or today)
            total = dailyIncome.stream().mapToLong(Long::longValue).sum();
            if(total==0){
                layout.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }
            else{
                layout.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
                tvTotalDailyIncomeValue.setText("₹" + total);
            }

            // Setup entries for LineChart
            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < dailyIncome.size(); i++) {
                entries.add(new Entry(i, dailyIncome.get(i)));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Daily Income ("+montharr[Integer.parseInt(month)]+" "+year+")");
            dataSet.setColor(getResources().getColor(android.R.color.holo_green_dark));
            dataSet.setCircleColor(getResources().getColor(android.R.color.holo_green_dark));
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(4f);

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);

            // X-axis
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(dayLabels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
          //  lineChart.setVisibleXRangeMaximum(6f);
            lineChart.moveViewToX(0);

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
            Toast.makeText(requireContext(), "Failed to load daily income!", Toast.LENGTH_SHORT).show();
        }
    }


    private void changetheme() {

        sptheme = getContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        if (sptheme.getString("theme", "dark").equals("pink")) {
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            fullgraphtoggle_title.setTextColor(getResources().getColor(R.color.black));
            title.setTextColor(getResources().getColor(R.color.black));

            //back image
            back.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.backgrounddark));
            incomelayout.setBackground(getResources().getDrawable(R.drawable.border6));
            tincome_title.setTextColor(getResources().getColor(R.color.black));
            spinnertitle.setTextColor(getResources().getColor(R.color.primarylight));
            spinnerlayout.setBackground(getResources().getDrawable(R.drawable.spinnerlayoutbg_light));


            monthspinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(getContext(), R.color.white))
            );
            monthspinner.setBackground(getResources().getDrawable(R.drawable.border6));

            yearspinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(getContext(), R.color.white))
            );
            yearspinner.setBackground(getResources().getDrawable(R.drawable.border6));
            nodata.setAnimation(R.raw.nodata);
        }
        else{
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
            title.setTextColor(getResources().getColor(R.color.white));
            fullgraphtoggle_title.setTextColor(getResources().getColor(R.color.white));
            tincome_title.setTextColor(getResources().getColor(R.color.white));
            spinnertitle.setTextColor(getResources().getColor(R.color.white));
            spinnerlayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));
            incomelayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));
            back.setImageTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            nodata.setAnimation(R.raw.nograph);
            monthspinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(getContext(), R.color.spinnerdarkcolor))
            );
            monthspinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));

            yearspinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(getContext(), R.color.spinnerdarkcolor))
            );
            yearspinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));

        }
    }

    private void checkOrientation(View view) {
        Configuration config= getResources().getConfiguration();
        LinearLayout spinnerlayout = view.findViewById(R.id.dailyincome_selection_layout);
        AdjustSizeConfiguration displaysize=new AdjustSizeConfiguration(requireActivity());


        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(layout,70);
            displaysize.setfixWidth(spinnerlayout,70);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {


//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }


}

