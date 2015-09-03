package kien.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import kien.SQLHelper.DataBaseHelper;

import java.io.IOException;

/**
 * Created by KIEN on 6/16/2015.
 */
public class ViewInfoDistrictActivity extends FragmentActivity {
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kien.license.R.layout.activity_viewdistrict);
        TextView tvPopulation = (TextView) findViewById(kien.license.R.id.tvPopulation);
        TextView tvArea = (TextView) findViewById(kien.license.R.id.tvArea);
        dataBaseHelper = new DataBaseHelper(this);
        try {
            dataBaseHelper.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent myItent = getIntent();
        int id = myItent.getIntExtra("id", 0);
        int population = dataBaseHelper.getPopulationDicstrict(id);
        float area = dataBaseHelper.getAreaDistrict(id);
        tvPopulation.setText(population + "");
        tvArea.setText(area + "");
    }
}
