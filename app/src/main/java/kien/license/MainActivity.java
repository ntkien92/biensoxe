package kien.license;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kien.SQLHelper.DataBaseHelper;
import kien.SQLHelper.SqliteDataController;
import kien.activities.AboutActivity;
import kien.activities.DistrictAcitivity;
import kien.activities.PoemActivity;
import kien.adapter.AdapterLicense;

import kien.objects.License;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends ActionBarActivity implements AdapterLicense.OnClickNumber {
    GridView gvLicense;
    EditText edSearch;
    List<License> listLicense, listLicenseTmp;
    AdapterLicense adapterLicense;
    public DataBaseHelper dataBaseHelper;
    public SqliteDataController sqliteDataController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gvLicense = (GridView) findViewById(R.id.gvLicense);
        edSearch = (EditText) findViewById(R.id.edSearch);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sqliteDataController = new SqliteDataController(this);
        try {
            sqliteDataController.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataBaseHelper = new DataBaseHelper(this);
        try {
            dataBaseHelper.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listLicense = new ArrayList<License>();
        listLicenseTmp = new ArrayList<License>();

        listLicense = dataBaseHelper.getListProvince("number_province");
        listLicenseTmp = listLicense;
        adapterLicense = new AdapterLicense(this, R.layout.fragment_license, listLicense, this);

        gvLicense.setAdapter(adapterLicense);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterLicense.getFilter().filter(s);
                //Log.e("KIENDU", start+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void changeToPage(int id) {
        //Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MainActivity.this, DistrictAcitivity.class);
        myIntent.putExtra("id", id);
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.menuAbout) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);

        }
        if (id == R.id.menuPoem){
            Intent myIntent = new Intent(MainActivity.this, PoemActivity.class);
            startActivity(myIntent);
        }
        if (id == R.id.menuExit) {
            //Toast.makeText(MainActivity.this, "Thoat", Toast.LENGTH_LONG).show();
            showDiaglog();
        }

        return super.onOptionsItemSelected(item);
    }
    public void showDiaglog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Thoát");
        dialog.setMessage("Bạn có muốn thoát ứng dụng không?");
        dialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Cảm ơn bạn đã sử dụng ứng dụng!!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        dialog.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showDiaglog();
    }
}
