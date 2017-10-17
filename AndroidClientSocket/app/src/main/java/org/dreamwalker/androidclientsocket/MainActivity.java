package org.dreamwalker.androidclientsocket;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cenkgun.chatbar.ChatBarView;
import com.naver.speech.clientapi.SpeechConfig;

import org.dreamwalker.androidclientsocket.Utils.AudioWriterPCM;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String TAG = "MainActivity";

    private TextView recieveText, txtSpeechInput;
    private EditText edtAddress, edtPort, edtMessage;
    private Button connectBtn, clearBtn, sttBtn;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private NaverRecognizer naverRecognizer;

    private String strResult;

    private AudioWriterPCM writer;

    private SpeechConfig.EndPointDetectType currentEpdType;
    private boolean isEpdTypeSelected;

    Socket socket = null;


    private ChatBarView chatBarView;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBtn = (Button) findViewById(R.id.buttonConnect);
        clearBtn = (Button) findViewById(R.id.buttonClear);
        edtAddress = (EditText) findViewById(R.id.addressText);
        edtPort = (EditText) findViewById(R.id.portText);
        recieveText = (TextView) findViewById(R.id.textViewReciev);
        edtMessage = (EditText) findViewById(R.id.messageText);
        sttBtn = (Button) findViewById(R.id.sttButton);
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        chatBarView = (ChatBarView) findViewById(R.id.chatbar);

        txtSpeechInput.setText("");

        tts = new TextToSpeech(this, this);


        sttBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        chatBarView.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO what you want..


            }
        });
        chatBarView.setOnMicListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO what you want..

                promptSpeechInput();

                return true;
            }
        });

        edtMessage.setText(chatBarView.getMessageText());

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask myClientTask = new MyClientTask(edtAddress.getText().toString(),
                        Integer.parseInt(edtPort.getText().toString()), edtMessage.getText().toString());
                myClientTask.execute();


            }
        });

        //clear Î≤ÑÌäº ?¥Î¶≠
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recieveText.setText("");
                edtMessage.setText("");
            }
        });


    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    speakOut();
                }
                break;
            }
        }
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.KOREAN);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                sttBtn.setEnabled(true);
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }


    }

    private void speakOut() {

        String text = txtSpeechInput.getText().toString();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor Î≤ÑÌäº???ÑÎ•¥Î©??ÑÎã¨ Î∞õÎäî ?ÑÏù¥?ºÏ? ?¨Ìä∏ Î©îÏãúÏßÄÎ•?Î∞õÎäî??        MyClientTask(String addr, int port, String message) {
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort); //?ùÏÑ±?êÏóê???ÖÎ†• Î∞õÏ? ?ÑÏù¥?ºÏ? ?¨Ìä∏Î°??åÏºì ?ùÏÑ±.
                //?°Ïã†
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //?òÏã†
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
    /*
     * notice:
     * inputStream.read() will block if no data return
     */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "?úÎ≤Ñ???ëÎãµ: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recieveText.setText(response);
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // NOTE : release() must be called on stop time.

    }


    @Override
    protected void onStart() {
        super.onStart();
        // NOTE : initialize() must be called on start time.

    }

    @Override
    protected void onResume() {
        super.onResume();

        strResult = "";
    }

    @Override
    protected void onDestroy() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}
