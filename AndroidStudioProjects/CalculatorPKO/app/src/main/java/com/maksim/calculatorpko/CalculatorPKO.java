package com.maksim.calculatorpko;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maksim.calculatorpko.fileexplorer.FileChooser;

public class CalculatorPKO extends AppCompatActivity {
    TextView tv_FileName;
    Button bt_open;
    Button bt_read;
    ListView lv_Result;

    FloatingActionButton fab;

    String fileName;

    private static final int REQUEST_PATH = 1;
    private static final int REQUEST_CHANGED_PATH = 2;
    private static final int REQUEST_SETTINGS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_pko);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                registerForContextMenu(view);
                openContextMenu(view);
                unregisterForContextMenu(view);*/

                Intent intent = new Intent(CalculatorPKO.this, ChangeFileName_Activity.class);
                intent.putExtra("SetFileName", fileName);
                startActivityForResult(intent, REQUEST_CHANGED_PATH);
            }
        });

        tv_FileName = (TextView) findViewById(R.id.textView_FileName);
        bt_open = (Button) findViewById(R.id.button_OpenXMLFile);
        bt_read = (Button) findViewById(R.id.button_read);
        lv_Result = (ListView) findViewById(R.id.list);

        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(CalculatorPKO.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        });

        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String typeName;
                    typeName = "Płatność kartą";

                    int timeInterval;
                    timeInterval = 1;

                    int checkBox = 0;
                    /*if(jRadioButton_Negative.isSelected())
                        checkBox = 0;
                    else if(jRadioButton_All.isSelected())
                        checkBox = 1;
                    else if(jRadioButton_Positive.isSelected())
                        checkBox = 2;
                    else throw new Exception("checkBox not working!!!");*/

                    String curr;
                    curr = "PLN";

                    if(fileName == null || fileName.isEmpty()) {
                        Snackbar.make(view, "Error: Open XML file!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        throw new Exception("fileName not exist!!!");
                    }
                    ReadXMLFile readXMLFile = new ReadXMLFile(fileName, typeName, timeInterval, checkBox, curr);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CalculatorPKO.this,
                            android.R.layout.simple_list_item_1, readXMLFile.getResults());
                    lv_Result.setAdapter(adapter);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator_pko, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(CalculatorPKO.this, Settings.class);
            intent.putExtra("SetFileName", fileName);
            startActivityForResult(intent, REQUEST_SETTINGS);
        } /*else if (id == R.id.action_operation) {
            Toast.makeText(CalculatorPKO.this, getString(R.string.action_operation), Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_time) {
            Toast.makeText(CalculatorPKO.this, getString(R.string.action_time), Toast.LENGTH_LONG).show();
        } else if (id == R.id.action_curr) {
            Toast.makeText(CalculatorPKO.this, getString(R.string.action_curr), Toast.LENGTH_LONG).show();
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(this, FileChooser.class);
                    startActivityForResult(intent, REQUEST_PATH);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(CalculatorPKO.this,
                            "Permission denied to read your External storage",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // Listen for results.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        if (requestCode == REQUEST_PATH) {
            if (resultCode == RESULT_OK) {
                fileName = data.getStringExtra("GetPath")+ "/" + data.getStringExtra("GetFileName");
                tv_FileName.setText(data.getStringExtra("GetFileName"));
            }
        } else if (requestCode == REQUEST_CHANGED_PATH) {
            if (resultCode == RESULT_OK) {
                fileName = data.getStringExtra("GetFileName");
                tv_FileName.setText(fileName);
            }
        }
    }
}
