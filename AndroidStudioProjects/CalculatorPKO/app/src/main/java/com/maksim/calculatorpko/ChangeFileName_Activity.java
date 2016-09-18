package com.maksim.calculatorpko;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeFileName_Activity extends AppCompatActivity {
    Button bt;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_change_file_name);

        bt = (Button) findViewById(R.id.button);
        et = (EditText) findViewById(R.id.editText);

        Intent data = getIntent();
        et.setText(data.getStringExtra("SetFileName"));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("GetFileName",et.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
