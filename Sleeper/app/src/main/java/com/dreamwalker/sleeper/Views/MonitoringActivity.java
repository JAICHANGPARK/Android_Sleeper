package com.dreamwalker.sleeper.Views;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamwalker.sleeper.BleLib.BlunoLibrary;
import com.dreamwalker.sleeper.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MonitoringActivity extends BlunoLibrary {

    private static final String TAG = "MonitoringActivity";

    private Button buttonScan;
    private Button buttonSerialSend, buttonSave, buttonGraph;
    private EditText serialSendText;
    private TextView serialReceivedText;
    private LineChart lineChart;
    private LineDataSet lineDataSet;
    private LineData lineData;

    private ArrayList<Entry> realtimeData;
    ArrayList<String> tempArray;
    private int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);

        onCreateProcess();                                                        //onCreate Process by BlunoLibrary

        // TODO: 2017-11-17 BLE 관련 Permission 주기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons & receive monitoring data.");
                builder.setPositiveButton("Ok", null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    }
                });
                builder.show();
            }
        }

        serialBegin(115200);                                                    //set the Uart Baudrate on BLE chip to 115200

        realtimeData = new ArrayList<>();
        tempArray = new ArrayList<>();
        lineChart = (LineChart) findViewById(R.id.mLinchart);


        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Paper.init(this);

        serialReceivedText = (TextView) findViewById(R.id.serialReveicedText);    //initial the EditText of the received data
        serialSendText = (EditText) findViewById(R.id.serialSendText);            //initial the EditText of the sending data
        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);        //initial the button for sending the data
        buttonSave = (Button) findViewById(R.id.buttonSave);        //initial the button for sending the data
        buttonScan = (Button) findViewById(R.id.buttonScan);                    //initial the button for scanning the BLE device
        buttonGraph = (Button) findViewById(R.id.buttonGraph);
        buttonGraph.setVisibility(View.GONE);
        buttonSave.setEnabled(false);

        buttonSerialSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                serialSend(serialSendText.getText().toString());                //send the data to the BLUNO
            }
        });

        buttonScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                buttonScanOnClickProcess();                                        //Alert Dialog for selecting the BLE device
            }
        });

        buttonSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realtimeData.size() == 0) { //실시간 데이터가 없으면
                    Toast.makeText(MonitoringActivity.this, "저장할 데이터가 없어요 ㅠㅠ", Toast.LENGTH_SHORT).show();
                } else { // 축적된 실시간 데이터가 있다면
                    if (Paper.book("realtime").read("data") == null) { // 만약 sql에 데이터가 없다면
                        Paper.book("realtime").write("data", realtimeData); // 데이터를 저장한다.
                        Toast.makeText(MonitoringActivity.this, "데이터가 없어 저장했어요", Toast.LENGTH_SHORT).show();
                        buttonSave.setText("저장완료:)");
                        buttonSave.setEnabled(false);
                        buttonGraph.setVisibility(View.VISIBLE);

                    } else { // 그렇지 않고 데이터가 있다면
                        Paper.book("realtime").delete("data"); //세로운 데이터를 저장하기 위해 기존 것을 다 지운다.
                        Paper.book("realtime").write("data", realtimeData); // 세로운 데이터를 다시 넣는다.
                        Toast.makeText(MonitoringActivity.this, "기존 데이터를 지우고 다시 저장했어요", Toast.LENGTH_SHORT).show();
                        buttonSave.setText("저장완료:)");
                        buttonSave.setEnabled(false);
                        buttonGraph.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        buttonGraph.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realtimeData.size() == 0) {
                    Toast.makeText(MonitoringActivity.this, "데이터가 없네요?", Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < realtimeData.size(); i++) {
                        float st = realtimeData.get(i).getY();
                        String stt = String.valueOf(st);
                        tempArray.add(stt);
                        Log.e(TAG, "tempArray: " +  tempArray.get(i));
                    }

                    Intent intent = new Intent(getBaseContext(), DetailGraphActivity.class);
                    intent.putExtra("realdata", tempArray);
                    intent.putExtra("page", 10);
                    startActivity(intent);
                }
            }
        });
    }

    // TODO: 2017-11-17 마쉬멜로우 이상부터 권한 설정이 꼭 필요하다. 
    // TODO: 2017-11-17 권한이 설정되지 않으면 블루투스 스캔이 되지 않는다. 그래서 사용자의 권한을 꼭 얻어야 한다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 0: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, " +
                            "this app will not be able to discover beacons when in the background and receive data. ");
                    builder.setPositiveButton("Ok", null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    protected void onResume() {
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();                                                        //onResume Process by BlunoLibrary
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);                    //onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();                                                        //onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();                                                        //onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();                                                        //onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {                                            //Four connection state
            case isConnected:
                buttonScan.setText("Connected");
                break;
            case isConnecting:
                buttonScan.setText("Connecting");
                break;
            case isToScan:
                buttonScan.setText("Scan");
                break;
            case isScanning:
                buttonScan.setText("Scanning");
                break;
            case isDisconnecting:
                buttonScan.setText("isDisconnecting");
                serialReceivedText.setText("");
                buttonSave.setEnabled(true);

                break;
            default:
                break;
        }
    }

    //Once connection data received, this function will be called
    // TODO Auto-generated method stub
    @Override
    public void onSerialReceived(String theString) {

        serialReceivedText.append(theString);                            //append the text into the EditText
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        ((ScrollView) serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
        Log.e(TAG, "onSerialReceived: " + theString);
        realtimeData.add(new Entry(cnt, Float.parseFloat(theString)));
        Log.e(TAG, "realtimeData: " + realtimeData.get(cnt));
        lineDataSet = new LineDataSet(realtimeData, "실시간 데이터");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        // TODO: 2017-11-17 실시간 그래프를 그리는 핵심 코드.
        lineChart.moveViewToX(lineData.getEntryCount());
        lineChart.notifyDataSetChanged();
        ++cnt;
    }
}