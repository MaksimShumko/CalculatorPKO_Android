package com.maksim.calculatorpko;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class Settings extends Activity implements View.OnClickListener {
    private TextView tv_operation1, tv_operation2, tv_interval1, tv_interval2,
            tv_amount1, tv_amount2, tv_curr1, tv_curr2;
    private EditText et_operation, et_curr;

    private final String[] interval = { "Year", "Month", "Week", "Day" };
    private final String[] amount = { "Only negative", "All", "Only positive" };

    private int dialog_switch;

    private final int DIALOG_OPERATION = 0;
    private final int DIALOG_INTERVAL = 1;
    private final int DIALOG_AMOUNT = 2;
    private final int DIALOG_CURR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);

        Intent in = getIntent();

        tv_operation1 = (TextView) findViewById(R.id.textView_operation1);
        tv_operation2 = (TextView) findViewById(R.id.textView_operation2);
        tv_interval1 = (TextView) findViewById(R.id.textView_interval1);
        tv_interval2 = (TextView) findViewById(R.id.textView_interval2);
        tv_amount1 = (TextView) findViewById(R.id.textView_amount1);
        tv_amount2 = (TextView) findViewById(R.id.textView_amount2);
        tv_curr1 = (TextView) findViewById(R.id.textView_curr1);
        tv_curr2 = (TextView) findViewById(R.id.textView_curr2);

        tv_operation2.setText(in.getStringExtra("SetOperation"));
        tv_interval2.setText(interval[in.getIntExtra("SetInterval", 0)]);
        tv_amount2.setText(amount[in.getIntExtra("SetAmount", 0)]);
        tv_curr2.setText(in.getStringExtra("SetCurr"));

        tv_operation1.setOnClickListener(this);
        tv_operation2.setOnClickListener(this);
        tv_interval1.setOnClickListener(this);
        tv_interval2.setOnClickListener(this);
        tv_amount1.setOnClickListener(this);
        tv_amount2.setOnClickListener(this);
        tv_curr1.setOnClickListener(this);
        tv_curr2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_operation1:
            case R.id.textView_operation2:
                showDialog(DIALOG_OPERATION);
                break;
            case R.id.textView_interval1:
            case R.id.textView_interval2:
                showDialog(DIALOG_INTERVAL);
                break;
            case R.id.textView_amount1:
            case R.id.textView_amount2:
                showDialog(DIALOG_AMOUNT);
                break;
            case R.id.textView_curr1:
            case R.id.textView_curr2:
                showDialog(DIALOG_CURR);
                break;
            default:
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        dialog_switch = id;
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {
            case DIALOG_OPERATION:
                adb.setTitle("Type of operation");
                et_operation = new EditText(this);
                et_operation.setText(tv_operation2.getText());
                adb.setView(et_operation);
                break;
            case DIALOG_INTERVAL:
                adb.setTitle("Time interval");
                adb.setSingleChoiceItems(interval, 1, myClickListener);
                break;
            case DIALOG_AMOUNT:
                adb.setTitle("Type of amount");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.select_dialog_singlechoice, amount);
                adb.setSingleChoiceItems(adapter, 0, myClickListener);
                break;
            case DIALOG_CURR:
                adb.setTitle("Currency");
                et_curr = new EditText(this);
                et_curr.setText(tv_curr2.getText());
                adb.setView(et_curr);
                break;
            default:
                break;
        }
        adb.setPositiveButton("OK", myClickListener);
        adb.setNegativeButton("Cancel", myClickListener);
        return adb.create();
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            if (which == Dialog.BUTTON_POSITIVE) {
                Intent intent = new Intent();
                switch (dialog_switch) {
                    case DIALOG_OPERATION:
                        tv_operation2.setText(et_operation.getText().toString());
                        intent.putExtra("GetOperation", et_operation.getText().toString());
                        break;
                    case DIALOG_INTERVAL:
                        tv_interval2.setText(interval[lv.getCheckedItemPosition()]);
                        intent.putExtra("GetInterval", lv.getCheckedItemPosition());
                        break;
                    case DIALOG_AMOUNT:
                        tv_amount2.setText(amount[lv.getCheckedItemPosition()]);
                        intent.putExtra("GetAmount", lv.getCheckedItemPosition());
                        break;
                    case DIALOG_CURR:
                        tv_curr2.setText(et_curr.getText().toString());
                        intent.putExtra("GetCurr", et_curr.getText().toString());
                        break;
                    default:
                        break;
                }
                setResult(RESULT_OK, intent);
            }
        }
    };
}
