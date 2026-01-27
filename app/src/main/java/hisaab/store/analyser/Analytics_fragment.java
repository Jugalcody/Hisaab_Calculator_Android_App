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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Analytics_fragment extends Fragment {

    //TextView header;

    LineChart lineChart;
    Utils utils;
    RelativeLayout parentLayout;
    LottieAnimationView nodata;

    AdView adView;

    String[] filters = {"Daily Spent","Daily Income","Monthly Analysis", "Yearly Analysis"};
    String[] yeararr = {
            "Select Year",
            "2025","2026","2027","2028","2029","2030",
            "2031","2032","2033","2034","2035","2036",
            "2037","2038","2039","2040","2041","2042",
            "2043","2044","2045","2046","2047","2048",
            "2049","2050"
    };
    Spinner spinner, monthSpinner,yearSpinner;
    SharedPreferences sp;
    TextView header,toggle_title,graphtypetitle,datetitle;
    Switch fullgraphToggle;
    RelativeLayout tavailablelayout,treceivelayout,tspentlayout;

    String curryear= new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
    String currmonth = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());

    String selmonth=currmonth,selyear=curryear;

    TextView tspent,treceive,tremain,tspenttitle,trectitle,tavailtitle;
    LinearLayout datelayout,graphtypelayout;

    String[] montharr = {"Select Month","Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};

    int year=2024 ,month=0;

    int analyticspos=0;
    View view;
    SpinnerAdapter1 adapter,adapterMonth,adapterYear;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_analytics_fragment, container, false);
        try {
            sp = getContext().getSharedPreferences("theme", getContext().MODE_PRIVATE);
            //header=view.sp =findViewById(R.id.txtAnalyticsHeader);
            toggle_title=view.findViewById(R.id.analytics_toggle_title);
            parentLayout=view.findViewById(R.id.analytics_parentlayout);
            monthSpinner =view.findViewById(R.id.spinnerMonth);
            yearSpinner=view.findViewById(R.id.spinnerYear);
            adView=view.findViewById(R.id.adViewanalytics);

            tspenttitle=view.findViewById(R.id.analytics_tspent_title);
            tavailtitle=view.findViewById(R.id.analytics_tremain_title);
            trectitle=view.findViewById(R.id.analytics_treceive_title);
            graphtypetitle=view.findViewById(R.id.graphtypetitle_analytics);
            datetitle=view.findViewById(R.id.graphdatetitle_analytics);
            nodata=view.findViewById(R.id.nodata_analytics);
            datelayout=view.findViewById(R.id.analyitcs_datelayout);
            graphtypelayout=view.findViewById(R.id.graphtypelayout_analytics);

            fullgraphToggle=view.findViewById(R.id.switchFullGraphexpense);
            tspent = view.findViewById(R.id.analyticstotalspent);
            treceive = view.findViewById(R.id.analyticstotalreceived);
            tremain=view.findViewById(R.id.analyticstotalremain);
            header=view.findViewById(R.id.txtAnalyticsTitle);
            tavailablelayout=view.findViewById(R.id.analytics_tavailablelayout);
            treceivelayout=view.findViewById(R.id.analytics_treceivelayout);
            utils=new Utils();
            lineChart=view.findViewById(R.id.monthExpenseChart);
            spinner = view.findViewById(R.id.spinnerAnalyticsType);
            tspentlayout=view.findViewById(R.id.total_spentlayout_analytics);

            nodata.setVisibility(View.GONE);

            fullgraphToggle.setChecked(true);

            checkOrientation(view);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

             changetheme();

            fullgraphToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    lineChart.fitScreen(); // 🔥 reset any zoom
                    lineChart.setVisibleXRangeMaximum(31f);
                    lineChart.moveViewToX(0);
                } else {
                    lineChart.setVisibleXRangeMaximum(6f);
                    lineChart.moveViewToX(0);
                }

                lineChart.invalidate();
            });



            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position!=0){

                        year=2024+position;
                    }
                    else{
                        year=2024;
                        lineChart.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }


                    if(position!=0 && analyticspos==3){
                        lineChart.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                        monthSpinner.setVisibility(View.GONE);
                        loadMonthlyAnalytics(view,yeararr[position]);

                    }
                    else if(position!=0 && analyticspos==1){

                            lineChart.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.GONE);
                            setupDailyGraphSpent(requireView(),month,2024+position);


                    }
                    selyear=String.valueOf(year);


                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });



            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position!=0){

                        month=position;
                    }
                    else{

                        lineChart.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        month=0;
                    }

                    if(position!=0 && analyticspos==1){

                            lineChart.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.GONE);
                        setupDailyGraphSpent(requireView(),position,year);


                    }


                    selmonth=String.valueOf(position);
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    month=0;
                }
            });


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {

                        case 0:
                            analyticspos=1;
                            monthSpinner.setVisibility(View.VISIBLE);
                            yearSpinner.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.GONE);
                            datelayout.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.VISIBLE);


                            lineChart.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.GONE);

                            setupDailyGraphSpent(requireView(),month, year);
                            break;
                        case 1:
                            analyticspos=1;
                            monthSpinner.setVisibility(View.VISIBLE);
                            yearSpinner.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.GONE);
                            datelayout.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.VISIBLE);


                            lineChart.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.GONE);

                            setupDailyGraphReceived(requireView(),month, year);
                            break;

                        case 2:
                            analyticspos=2;
                            monthSpinner.setVisibility(View.GONE);
                            yearSpinner.setVisibility(View.VISIBLE);
                            datelayout.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.GONE);
                            nodata.setVisibility(View.VISIBLE);

                            lineChart.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.GONE);

                            loadMonthlyAnalytics(view,selyear);

                            break;
                        case 3:
                            analyticspos=3;
                            lineChart.setVisibility(View.VISIBLE);
                            monthSpinner.setVisibility(View.GONE);
                            yearSpinner.setVisibility(View.GONE);
                            datelayout.setVisibility(View.GONE);
                            nodata.setVisibility(View.GONE);
                            loadYearlyAnalytics(view);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    yearSpinner.setVisibility(View.GONE);
                    monthSpinner.setVisibility(View.GONE);
                    lineChart.setVisibility(View.GONE);
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
        return view;
    }


    private void loadMonthlyAnalytics(View view,String year) {
        //     header.setText("This Month Expense");
        setupMonthlyGraph(view,year);

    }

    private void loadYearlyAnalytics(View view) {
        // header.setText("This Year Expense");
        setupYearlyGraph(view);

    }



    private void setupDailyGraphSpent(View view, int month, int year) {
        try {
            treceivelayout.setVisibility(View.GONE);
            tspentlayout.setVisibility(View.VISIBLE);
            LineChart chart = view.findViewById(R.id.monthExpenseChart);
            SharedPreferences spitem = requireActivity().getSharedPreferences("item", Context.MODE_PRIVATE);
            String head = spitem.getString("user","");

            ArrayList<Long> dailySpend = dailySpendGraph(requireActivity(), head, month, year);

            List<Entry> spendEntries = new ArrayList<>();
            List<Entry> receiveEntries = new ArrayList<>();
            String[] days = new String[dailySpend.size()];

            for (int i = 0; i < dailySpend.size(); i++) {
                spendEntries.add(new Entry(i, dailySpend.get(i)));
                days[i] = String.valueOf(i + 1);
            }

            LineDataSet spendDataSet = new LineDataSet(spendEntries, "Daily Spend");
            spendDataSet.setColor(Color.RED);
            spendDataSet.setCircleColor(Color.RED);
            spendDataSet.setLineWidth(1.8f);
            spendDataSet.setCircleRadius(4f);


            LineData lineData = new LineData(spendDataSet);
            chart.setData(lineData);

            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            lineData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            chart.getAxisRight().setEnabled(false);
            chart.getDescription().setEnabled(false);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            chart.animateX(700);
            chart.moveViewToX(0);

            YAxis leftAxis = chart.getAxisLeft();
            Legend legend = chart.getLegend();

            if (sp.getString("theme", "dark").equals("pink")) {
                int black = Color.BLACK;
                xAxis.setTextColor(black);
                leftAxis.setTextColor(black);
                chart.getAxisRight().setTextColor(black);
                legend.setTextColor(black);
                spendDataSet.setValueTextColor(black);
            } else {
                int white = Color.WHITE;
                xAxis.setTextColor(white);
                leftAxis.setTextColor(white);
                chart.getAxisRight().setTextColor(white);
                legend.setTextColor(white);
                spendDataSet.setValueTextColor(white);
            }

            chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            chart.invalidate();

            // Update totals
            long totalSpend = 0, totalReceive = 0;
            for (long v : dailySpend) totalSpend += v;

            tspent.setText("₹" + totalSpend);
            treceive.setText("₹" + totalReceive);
            tavailablelayout.setVisibility(View.GONE);

        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    private void setupDailyGraphReceived(View view, int month, int year) {
        try {
            treceivelayout.setVisibility(View.VISIBLE);
            tspentlayout.setVisibility(View.GONE);
            LineChart chart = view.findViewById(R.id.monthExpenseChart);
            SharedPreferences spitem = requireActivity().getSharedPreferences("item", Context.MODE_PRIVATE);
            String head = spitem.getString("user","");


            ArrayList<Long> dailyReceive = dailyReceiveGraph(requireActivity(), head, month, year);

            List<Entry> receiveEntries = new ArrayList<>();
            String[] days = new String[dailyReceive.size()];

            for (int i = 0; i < dailyReceive.size(); i++) {
                receiveEntries.add(new Entry(i, dailyReceive.get(i)));
                days[i] = String.valueOf(i + 1);
            }

            LineDataSet receiveDataSet = new LineDataSet(receiveEntries, "Daily Received");
            receiveDataSet.setColor(getResources().getColor(R.color.greenincome));
            receiveDataSet.setCircleColor(getResources().getColor(R.color.greenincome));
            receiveDataSet.setLineWidth(1.8f);
            receiveDataSet.setCircleRadius(4f);

            LineData lineData = new LineData(receiveDataSet);
            chart.setData(lineData);


            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);

            chart.getAxisRight().setEnabled(false);
            chart.getDescription().setEnabled(false);
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            chart.animateX(700);
            chart.moveViewToX(0);

            YAxis leftAxis = chart.getAxisLeft();
            Legend legend = chart.getLegend();

            receiveDataSet.setColor(getResources().getColor(R.color.greenincome));
            receiveDataSet.setCircleColor(getResources().getColor(R.color.greenincome));
            if (sp.getString("theme", "dark").equals("pink")) {
                int black = Color.BLACK;
                xAxis.setTextColor(black);
                leftAxis.setTextColor(black);
                chart.getAxisRight().setTextColor(black);
                legend.setTextColor(black);
                receiveDataSet.setValueTextColor(black);
            } else {
                int white = Color.WHITE;
                xAxis.setTextColor(white);
                leftAxis.setTextColor(white);
                chart.getAxisRight().setTextColor(white);
                legend.setTextColor(white);
                receiveDataSet.setValueTextColor(white);
            }

            chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });


            chart.invalidate();

            // Update totals
            long totalSpend = 0, totalReceive = 0;
            for (long v : dailyReceive) totalReceive += v;

            tspent.setText("₹" + totalSpend);
            treceive.setText("₹" + totalReceive);
            treceivelayout.setVisibility(View.VISIBLE);
            tavailablelayout.setVisibility(View.GONE);

        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // Helper method to get daily received
    public ArrayList<Long> dailyReceiveGraph(Context context, String head, int month, int year) {
        ArrayList<Long> list = new ArrayList<>();
        File path = context.getFilesDir();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);

        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int d = 1; d <= maxDays; d++) {
            long receive = 0;

            try {
                File file = new File(path, head+montharr[month]+year+"money_history.txt");
                if (!file.exists()) {
                    list.add(0L);
                    continue;
                }

                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] arr = line.trim().split("#");
                    if (arr.length == 4) {
                        try {
                            int fileDay = Integer.parseInt(arr[2].trim().split("-")[0]);
                            if (fileDay == d) {
                                long v = Long.parseLong(arr[1].trim().substring(3));
                                receive += v;
                            }
                        } catch (Exception ignore) {}
                    }
                }
                br.close();
                fis.close();
            } catch (Exception ignore) {}
            list.add(receive);
        }

        return list;
    }


    public ArrayList<Long> dailySpendGraph(Context context, String head, int month, int year) {
        ArrayList<Long> list = new ArrayList<>();
        File path = context.getFilesDir();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);

        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int d = 1; d <= maxDays; d++) {
            long spend = 0;

            try {
                //head+"hisaab"+mon+year+"data.txt
                // File name: head + hisaab + DDMMYYYYdata.txt (same as your existing format)
                String fileName = head + "hisaab" +
                        montharr[month] +
                        year + "data.txt";

                File file = new File(path, fileName);
                if (!file.exists()) {
                    list.add(0L);
                    continue;
                }

                FileInputStream fis = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                String line;
                while ((line = br.readLine()) != null) {
                    String[] arr = line.trim().split("#");
                    if (arr.length == 4) {
                        try {
                            int fileDay = Integer.parseInt(arr[2].trim().split("-")[0]);
                            if (fileDay == d) {
                                long v = Long.parseLong(arr[1].trim().substring(3) ); // Amount index (adjust if needed)
                                spend += v;
                            }

                        } catch (Exception e) {
                            Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
                br.close();
                fis.close();

            } catch (Exception ignore) {
                spend = 0;
            }

            list.add(spend);
        }

        return list;
    }




    private void setupMonthlyGraph(View view,String year) {
        try {
            tspentlayout.setVisibility(View.VISIBLE);
            List<Entry> receivedEntries = new ArrayList<>();
            List<Entry> spentEntries = new ArrayList<>();
            List<Entry> availableEntries = new ArrayList<>();

            long available = 0;
            String[] months = generateMonthLabels();

            for (int i = 0; i < 12; i++) {
                long r = Long.parseLong(utils.monthlyGet(montharr[i+1], requireActivity(), year));
                long s = Long.parseLong(utils.monthlySpend(montharr[i+1], requireActivity(), year));
                System.out.println("soo"+s);
                available += r - s;

                receivedEntries.add(new Entry(i, r));
                spentEntries.add(new Entry(i, s));
                availableEntries.add(new Entry(i, available));
            }

            setupLineGraph(lineChart, receivedEntries, spentEntries, availableEntries,
                    months, "Monthly Received", "Monthly Spent", "Monthly Available");

            long totalR = 0, totalS = 0;

            for (int i = 0; i < 12; i++) {
                long r = Long.parseLong(utils.monthlyGet(montharr[i+1], requireActivity(), year));
                long s = Long.parseLong(utils.monthlySpend(montharr[i+1], requireActivity(), year));

                totalR += r;
                totalS += s;
            }

            tspent.setText("₹" + totalS);
            treceive.setText("₹" + totalR);
            tremain.setText("₹" + (totalR-totalS));
            treceive.setVisibility(View.VISIBLE);

            treceivelayout.setVisibility(View.VISIBLE);
            tavailablelayout.setVisibility(View.VISIBLE);



        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupYearlyGraph(View view) {
        try {
            tspentlayout.setVisibility(View.VISIBLE);
            List<Entry> receivedEntries = new ArrayList<>();
            List<Entry> spentEntries = new ArrayList<>();
            List<Entry> availableEntries = new ArrayList<>();

            long available = 0;
            for (int i = 0; i < Integer.parseInt(curryear) - 2022; i++) {
                String year = String.valueOf(2024 + i);
                long r = Long.parseLong(utils.yearlyGet(year, requireActivity()));
                long s = Long.parseLong(utils.yearlySpend(year, requireActivity()));
                available = available + r - s;

                receivedEntries.add(new Entry(i, r));
                spentEntries.add(new Entry(i, s));
                availableEntries.add(new Entry(i, available));
            }

            setupLineGraph(lineChart, receivedEntries, spentEntries, availableEntries,
                    generateYearLabels(), "Yearly Received", "Yearly Spent", "Yearly Available");

            long totalR = 0, totalS = 0;

            for (int i = 0; i < 27; i++) {
                String year = String.valueOf(2024 + i);

                long r = Long.parseLong(utils.yearlyGet(year, requireActivity()));
                long s = Long.parseLong(utils.yearlySpend(year, requireActivity()));

                totalR += r;
                totalS += s;
            }

            tspent.setText("₹" + totalS);
            treceive.setText("₹" + totalR);
            tremain.setText("₹" + (totalR - totalS));
            treceive.setVisibility(View.VISIBLE);
            treceivelayout.setVisibility(View.VISIBLE);
            tavailablelayout.setVisibility(View.VISIBLE);
        }
        catch (Exception e){

        }
    }


    private void setupLineGraph(LineChart lineChart,
                                List<Entry> rec, List<Entry> spent, List<Entry> avail,
                                String[] labels,
                                String labRec, String labSpent, String labAvail) {
        try {
            LineDataSet dsReceived = new LineDataSet(rec, labRec);
            dsReceived.setColor(getResources().getColor(R.color.greenincome));
            dsReceived.setCircleRadius(4f);
            dsReceived.setLineWidth(1.8f);
            dsReceived.setCircleRadius(4f);
            LineDataSet dsSpent = new LineDataSet(spent, labSpent);
            dsSpent.setColor(Color.RED);
            dsSpent.setCircleRadius(4f);
            dsSpent.setLineWidth(1.8f);
            dsSpent.setCircleRadius(4f);

            LineDataSet dsAvail = new LineDataSet(avail, labAvail);
            dsAvail.setColor(Color.YELLOW);
            dsAvail.setCircleRadius(4f);
            dsAvail.setLineWidth(1.8f);
            dsAvail.setCircleRadius(4f);

            LineData lineData = new LineData(dsReceived, dsSpent, dsAvail);
            lineChart.setData(lineData);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setLabelCount(15);



            lineChart.setExtraBottomOffset(13f);
            Legend legend = lineChart.getLegend();
            legend.setTextSize(11f);   // Increase legend text size
            legend.setFormSize(10f);   // Increase circle/square size
            legend.setXEntrySpace(18f); // Space between items horizontally
            legend.setYEntrySpace(11f); // Space between lines if vertical
            legend.setYOffset(10f);
            YAxis leftAxis = lineChart.getAxisLeft();


            lineChart.getAxisRight().setEnabled(false);
            lineChart.getDescription().setEnabled(false);
            if (!fullgraphToggle.isChecked()) {
                lineChart.animateX(500);

            }
            else{
                lineChart.fitScreen(); // 🔥 reset any zoom
                lineChart.setVisibleXRangeMaximum(31f);
                lineChart.moveViewToX(0);
            }

            // Enable scrolling and zooming
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);

            lineChart.moveViewToX(0); //
            xAxis.setGridColor(Color.GRAY);
            leftAxis.setGridColor(Color.GRAY);
            dsAvail.setCircleColor(Color.YELLOW);
            dsSpent.setCircleColor(Color.RED);
            dsReceived.setCircleColor(getResources().getColor(R.color.greenincome));

            if (sp.getString("theme", "dark").equals("pink")) {
                int black = Color.BLACK;
                xAxis.setTextColor(black);
                lineData.setValueTextColor(R.color.black);
                leftAxis.setTextColor(black);
                lineChart.getAxisRight().setTextColor(black);
                xAxis.setAxisLineColor(black);
                leftAxis.setAxisLineColor(black);
                legend.setTextColor(black);
                dsAvail.setValueTextColor(Color.BLACK);
                dsSpent.setValueTextColor(Color.BLACK);
                dsReceived.setValueTextColor(Color.BLACK);

            } else {
                int white = Color.WHITE;
                xAxis.setTextColor(white);
                leftAxis.setTextColor(white);
                dsAvail.setValueTextColor(Color.WHITE);
                dsSpent.setValueTextColor(Color.WHITE);
                dsReceived.setValueTextColor(Color.WHITE);
                lineChart.getAxisRight().setTextColor(white);
                xAxis.setAxisLineColor(white);
                leftAxis.setAxisLineColor(white);
                legend.setTextColor(white);
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
        }catch (Exception e){
            Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    private String[] generateDayLabels() {
        String[] days = new String[30];
        for (int i = 0; i < 30; i++) {
            days[i] = "Day " + (i + 1);
        }
        return days;
    }

    private String[] generateMonthLabels() {
        return new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    }

    private String[] generateYearLabels() {
        String[] years = new String[27];
        for(int i=0;i<27;i++){
            years[i] = String.valueOf(2024 + i);
        }
        return years;
    }

    @Override
    public void onResume() {
        super.onResume();
        changetheme();
        checkOrientation(view);
        //lineChart.setVisibility(View.GONE);
        //spinner.setSelection(0);
        int position = spinner.getSelectedItemPosition();
        if(position==0) {

            setupDailyGraphSpent(requireView(),Integer.parseInt(selmonth),Integer.parseInt(selyear));
        }
        else if(position==1) {

                loadMonthlyAnalytics(view,selyear);
        }
        else if(position==2) loadYearlyAnalytics(view);
    }



private void changetheme() {

    sp = getContext().getSharedPreferences("theme", getContext().MODE_PRIVATE);
    if (sp.getString("theme", "dark").equals("pink")) {
        adapter = new SpinnerAdapter1(requireContext(), R.layout.spinner_login_pink, filters);
        adapterMonth = new SpinnerAdapter1(getContext(), R.layout.spinner_login_pink, montharr);
        adapterYear = new SpinnerAdapter1(getContext(), R.layout.spinner_login_pink, yeararr);

    } else {
        adapterMonth = new SpinnerAdapter1(getContext(), R.layout.spinner_login, montharr);
        adapterYear = new SpinnerAdapter1(getContext(), R.layout.spinner_login, yeararr);
        adapter = new SpinnerAdapter1(requireContext(), R.layout.spinner_login,filters);
    }
    spinner.setAdapter(adapter);
    yearSpinner.setAdapter(adapterYear);
    monthSpinner.setAdapter(adapterMonth);
    yearSpinner.setSelection(Integer.parseInt(curryear)-2024);
    monthSpinner.setSelection(Integer.parseInt(currmonth));



    if (sp.getString("theme", "dark").equals("pink")) {

           //light theme
            header.setTextColor(getResources().getColor(R.color.primarylight));
            toggle_title.setTextColor(Color.BLACK);
            parentLayout.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            lineChart.setBackgroundColor(getResources().getColor(R.color.backgroundlight));
            tavailablelayout.setBackground(getResources().getDrawable(R.drawable.border6));
            treceivelayout.setBackground(getResources().getDrawable(R.drawable.border6));
            tspentlayout.setBackground(getResources().getDrawable(R.drawable.border6));

        tspenttitle.setTextColor(getResources().getColor(R.color.black));
        tavailtitle.setTextColor(getResources().getColor(R.color.black));
        trectitle.setTextColor(getResources().getColor(R.color.black));
        spinner.setPopupBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.white))
        );

        spinner.setBackground(getResources().getDrawable(R.drawable.border6));


        monthSpinner.setPopupBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.white))
        );
        monthSpinner.setBackground(getResources().getDrawable(R.drawable.border6));


        yearSpinner.setPopupBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.white))
        );
        yearSpinner.setBackground(getResources().getDrawable(R.drawable.border6));



        graphtypetitle.setTextColor(getResources().getColor(R.color.primarylight));
        datetitle.setTextColor(getResources().getColor(R.color.primarylight));
        graphtypelayout.setBackground(getResources().getDrawable(R.drawable.spinnerlayoutbg_light));
        datelayout.setBackground(getResources().getDrawable(R.drawable.spinnerlayoutbg_light));



        graphtypelayout.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), R.color.lightspinnerbg)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            graphtypelayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            graphtypelayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }
        datelayout.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), R.color.lightspinnerbg)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            datelayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            datelayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }

    } else {
                   //dark theme
            header.setTextColor(Color.WHITE);
            toggle_title.setTextColor(Color.WHITE);
            parentLayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));
            lineChart.setBackgroundColor(getResources().getColor(R.color.backgrounddark));

            tavailablelayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border6_darkgray));
            treceivelayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border6_darkgray));
            tspentlayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border6_darkgray));

            tspenttitle.setTextColor(getResources().getColor(R.color.cardtextcolorgray));
            tavailtitle.setTextColor(getResources().getColor(R.color.cardtextcolorgray));
            trectitle.setTextColor(getResources().getColor(R.color.cardtextcolorgray));

        spinner.setPopupBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.cardtextcolorgray2))
        );
        spinner.setBackground(getResources().getDrawable(R.drawable.loginbut2));


        monthSpinner.setPopupBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.cardtextcolorgray2))
        );
        monthSpinner.setBackground(getResources().getDrawable(R.drawable.loginbut2));


        yearSpinner.setPopupBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.spinnerdarkcolor))
        );
        yearSpinner.setBackground(getResources().getDrawable(R.drawable.loginbut2));


        graphtypelayout.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), R.color.bg2)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            graphtypelayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            graphtypelayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }
        datelayout.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), R.color.bg2)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            datelayout.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            datelayout.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }

        graphtypelayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));
        datelayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));

        graphtypetitle.setTextColor(getResources().getColor(R.color.cardtextcolorgray3));
        datetitle.setTextColor(getResources().getColor(R.color.cardtextcolorgray3));

    }

        }

    private void checkOrientation(View view) {
        Configuration config= getResources().getConfiguration();
        AdjustSizeConfiguration displaysize=new AdjustSizeConfiguration(requireActivity());
        LinearLayout layout=view.findViewById(R.id.analytics_innerlayout);

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(layout, 50);
            displaysize.setfixHeight(lineChart,30);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaysize.setfixWidth(layout, 95);

//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }

        }