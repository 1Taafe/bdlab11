package by.bstu.bdlab11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper helper;
    ListView resultList;
    DatePicker fromDatePicker;
    DatePicker toDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DatabaseHelper(getBaseContext());
        helper.Initialize(getBaseContext());
        resultList = findViewById(R.id.resultList);
        fromDatePicker = findViewById(R.id.firstDatePicker);
        toDatePicker = findViewById(R.id.secondDatePicker);
    }

    public void BestStudents(View view) {
        ArrayList<String> result = helper.getBest(getFromDate(), getToDate());
        Toast.makeText(this, getFromDate() + "\n" + getToDate(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        resultList.setAdapter(adapter);
    }

    public String getFromDate(){
        int mYear = fromDatePicker.getYear();
        int mMonth = fromDatePicker.getMonth() + 1;
        int mDay = fromDatePicker.getDayOfMonth();
        String month = "";
        String day = "";
        if(mMonth < 10){
            month = "0" + mMonth;
        }
        else{
            month = String.valueOf(mMonth);
        }
        if(mDay < 10){
            day = "0" + mDay;
        }
        else{
            day = String.valueOf(mDay);
        }
        String selectedDate = mYear + "-" + month + "-" + day;
        return selectedDate;
    }

    public String getToDate(){
        int mYear = toDatePicker.getYear();
        int mMonth = toDatePicker.getMonth() + 1;
        int mDay = toDatePicker.getDayOfMonth();
        String month = "";
        String day = "";
        if(mMonth < 10){
            month = "0" + mMonth;
        }
        else{
            month = String.valueOf(mMonth);
        }
        if(mDay < 10){
            day = "0" + mDay;
        }
        else{
            day = String.valueOf(mDay);
        }
        String selectedDate = mYear + "-" + month + "-" + day;
        return selectedDate;
    }

    public void WorstStudents(View view) {
        ArrayList<String> result = helper.getWorst(getFromDate(), getToDate());
        Toast.makeText(this, getFromDate() + "\n" + getToDate(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        resultList.setAdapter(adapter);
    }

    public void AvgStudents(View view) {
        ArrayList<String> result = helper.getAverage(getFromDate(), getToDate());
        Toast.makeText(this, getFromDate() + "\n" + getToDate(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        resultList.setAdapter(adapter);
    }

    public void AnalysisStudents(View view) {
        ArrayList<String> result = helper.getAnalysis(getFromDate(), getToDate());
        Toast.makeText(this, getFromDate() + "\n" + getToDate(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        resultList.setAdapter(adapter);
    }

    public void AnalysisFaculties(View view) {
        ArrayList<String> result = helper.getFacultyAnalysis(getFromDate(), getToDate());
        Toast.makeText(this, getFromDate() + "\n" + getToDate(), Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        resultList.setAdapter(adapter);
    }
}