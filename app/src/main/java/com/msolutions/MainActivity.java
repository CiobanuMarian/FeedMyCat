package com.msolutions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.msolutions.model.Configuration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button btnFeed;
    private ImageButton btnConfig;
    private boolean showErr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        btnFeed = (Button) findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configuration.getInstance().getIp() == null || Configuration.getInstance().getPort() == null) {
                    showAlert();
                } else {
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();
                }
            }
        });
        btnConfig = (ImageButton) findViewById(R.id.btnConfig);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this
                        , SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                socket = new Socket(Configuration.getInstance().getIp(), Configuration.getInstance().getPort());
                Log.i("INFO: ", "Connected!");

                // get the output stream from the socket.
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                Log.i("INFO: ", "Sending string to the ServerSocket");

                dataOutputStream.writeUTF("Up");
                dataOutputStream.flush(); // send the message
                dataOutputStream.close(); // close the output stream when we're done.

                Log.i("INFO: ", "Closing socket and terminating the program");
                socket.close();
            } catch (IOException e) {
                Log.e("Error", e.toString());
                showErr = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(showErr) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to " + Configuration.getInstance().getIp() + ":" + Configuration.getInstance().getPort(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void showAlert() {
        // show alert in case of null ip or port
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("No IP/Port configuration")
                .setTitle("Error")
                .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this
                                , SettingsActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Load from temp file", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            File config = new File(getApplicationContext().getFilesDir(), "Config");
                            FileReader fReader = new FileReader(config);
                            BufferedReader bReader = new BufferedReader(fReader);
                            Configuration.getInstance().setIp(bReader.readLine());
                            Configuration.getInstance().setPort(Integer.parseInt(bReader.readLine()));

                        } catch (Exception e){
                            Log.e("Error:", e.toString());
                            Toast toast = Toast.makeText(getApplicationContext(), "No temp file found", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                })
                .create();
        alert.show();
    }
}
