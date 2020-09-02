package com.demo.payo.view;

import android.graphics.Color;
import android.os.Bundle;

import com.demo.payo.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {


    float totalExpenses, totalIncome;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_expense);
        pieChart = findViewById(R.id.pieChart);
        Bundle extras = getIntent().getExtras();
        String valIncome = extras.getString("totalIncome");
        totalIncome = Float.parseFloat(valIncome);
        String valExpenses = extras.getString("totalExpenses");
        totalExpenses = Float.parseFloat(valExpenses);
        totalExpenses = Float.parseFloat(valExpenses);
        totalIncome = Float.parseFloat(valIncome);
        getEntries();

        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);
    }

    private void getEntries() {
        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(totalIncome, 1));
        pieEntries.add(new PieEntry(totalExpenses, 2));
      //  pieEntries.add(new PieEntry(8f, 3));
      //  pieEntries.add(new PieEntry(7f, 4));
       // pieEntries.add(new PieEntry(3f, 5));
    }
}