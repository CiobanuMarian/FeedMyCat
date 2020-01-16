package com.msolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.msolutions.model.Configuration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class SettingsActivity extends AppCompatActivity {

    private Button btnSave;
    private TextView txtIp;
    private TextView txtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSave = (Button) findViewById(R.id.btnSave);
        txtIp = (TextView) findViewById(R.id.txtIP);
        txtPort = (TextView) findViewById(R.id.txtPort);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtIp.getText().toString().isEmpty()) {
                    txtIp.setError("All fields must be completed!");
                    if (txtPort.getText().toString().isEmpty()) {
                        txtPort.setError("All fields must be completed!");
                    }
                } else {
                    try {
                        Configuration.getInstance().setIp(txtIp.getText().toString());
                        Configuration.getInstance().setPort(Integer.parseInt(txtPort.getText().toString()));
                        File config = new File(getApplicationContext().getFilesDir(), "Config");
                        BufferedWriter bw = new BufferedWriter(new FileWriter(config));
                        bw.write(txtIp.getText().toString());
                        bw.newLine();
                        bw.write(txtPort.getText().toString());
                        bw.close();
                        Intent intent = new Intent(SettingsActivity.this
                                , MainActivity.class);
                        startActivity(intent);
                    } catch (Exception e){
                        Toast toast = Toast.makeText(getApplicationContext(), "Wrong user input, try again", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

            }
    });
}
}
