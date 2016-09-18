package com.maksim.calculatorpko;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    TextView tv_operation1, tv_operation2, tv_interval1, tv_interval2,
            tv_amount1, tv_amount2, tv_curr1, tv_curr2;

    private final int MENU_YEAR = 1;
    private final int MENU_MONTH = 2;
    private final int MENU_WEEK = 3;
    private final int MENU_DAY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);

        tv_operation1 = (TextView) findViewById(R.id.textView_operation1);
        tv_operation2 = (TextView) findViewById(R.id.textView_operation2);
        tv_interval1 = (TextView) findViewById(R.id.textView_interval1);
        tv_interval2 = (TextView) findViewById(R.id.textView_interval2);

        tv_interval1.setOnClickListener(this);
        tv_interval2.setOnClickListener(this);
        tv_operation1.setOnClickListener(this);
        tv_operation2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        registerForContextMenu(view);
        openContextMenu(view);
        unregisterForContextMenu(view);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.textView_interval1:
            case R.id.textView_interval2:
                menu.add(0, MENU_YEAR, 0, "Year");
                menu.add(0, MENU_MONTH, 0, "Month");
                menu.add(0, MENU_WEEK, 0, "Week");
                menu.add(0, MENU_DAY, 0, "Day");
                break;
            case R.id.textView_operation1:
            case R.id.textView_operation2:
                Dialog dialog;
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.operation_dialog);
                dialog.setTitle("Custom Dialog");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                dialog.show();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_YEAR:
                tv_interval2.setText("Year");
                break;
            case MENU_MONTH:
                tv_interval2.setText("Month");
                break;
            case MENU_WEEK:
                tv_interval2.setText("Week");
                break;
            case MENU_DAY:
                tv_interval2.setText("Day");
                break;
        }

        return super.onContextItemSelected(item);
    }
}
