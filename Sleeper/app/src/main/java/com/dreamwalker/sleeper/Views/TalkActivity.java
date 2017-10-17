package com.dreamwalker.sleeper.Views;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamwalker.sleeper.Adapter.ChatAdapter;
import com.dreamwalker.sleeper.ChatBarView;
import com.dreamwalker.sleeper.Model.ChatModel;
import com.dreamwalker.sleeper.R;
import com.hendraanggrian.bundler.BindExtra;
import com.hendraanggrian.reveallayout.RevealableLayout;

import org.lunainc.chatbar.ViewChatBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TalkActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static final String EXTRA_RECT = "com.example.circularreveal.CustomActivity2";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @BindExtra(EXTRA_RECT)
    Rect rect;

    @BindView(R.id.revealLayout)
    RevealableLayout revealLayout;

    @BindView(R.id.layout)
    ViewGroup layout;

    private ViewChatBar viewChatBar;

    private static final String TAG = "TalkActivity";
    private TextView textView;

    private List<ChatModel> chatList = new ArrayList<>();
    public ChatAdapter adapter;
    private InputMethodManager imm;
    public Calendar calendar = Calendar.getInstance();

    private ChatBarView chatBarView;
    private TextToSpeech tts;

    public static String address, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        ButterKnife.bind(this); // 뷰 바인드

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 화면을 고정

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드를 사라지게 하기위함.

        //viewChatBar = (ViewChatBar) findViewById(R.id.charBar);
        //viewChatBar.setMessageBoxHint("Enter your Message");

        setUpMessage();

        // Setting으로 받은 주소값과 포트 값을 가져옴.
        // 서버 주소가 고정되어 있다면 이런 짓은 하지 않아도 되겠다.
        address = MainActivity.address;
        port = MainActivity.port;

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ChatAdapter(chatList, this);
        listView.setAdapter(adapter);

        tts = new TextToSpeech(this, this);

      /*  viewChatBar.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm"); // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                String formatDate = sdfNow.format(date); // nowDate 변수에 값을 저장한다.

                if(viewChatBar.getMessageText().equals("")){
                    Toast.makeText(TalkActivity.this, "메세지를 입력해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    chatList.add(new ChatModel(viewChatBar.getMessageText(),formatDate,true));
                    BackgroundTask myClientTask = new BackgroundTask("121.187.72.129", 8890, viewChatBar.getMessageText());
                    myClientTask.execute();
                }

                imm.hideSoftInputFromWindow(viewChatBar.getWindowToken(), 0);
                viewChatBar.setClearMessage(true);
                adapter.notifyDataSetChanged();
            }
        });*/

        chatBarView = (ChatBarView) findViewById(R.id.charBar);
        chatBarView.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO what you want..

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm"); // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                String formatDate = sdfNow.format(date); // nowDate 변수에 값을 저장한다.

                if (chatBarView.getMessageText().equals("")) {
                    Toast.makeText(TalkActivity.this, "메세지를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    chatList.add(new ChatModel(chatBarView.getMessageText(), formatDate, true));
                    BackgroundTask myClientTask = new BackgroundTask("121.187.72.129", 8890, chatBarView.getMessageText());
                    myClientTask.execute();
                }

                imm.hideSoftInputFromWindow(chatBarView.getWindowToken(), 0);
                chatBarView.setClear();
                adapter.notifyDataSetChanged();

                Log.e(TAG, "onClick: " + chatBarView.getMessageText());
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
    }
    
    private void setUpMessage() {
        chatList.add(new ChatModel("안녕하세요?", false));
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.KOREAN);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Snackbar.make(getCurrentFocus(), "This Language is not supported", Snackbar.LENGTH_LONG).setAction("확인.", null).show();
            } else {
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public class BackgroundTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor 버튼을 누르면 전달 받는 아이피와 포트 메시지를 받는다
        BackgroundTask(String addr, int port, String message) {
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            InputStream inputStream = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort); //생성자에서 입력 받은 아이피와 포트로 소켓 생성.
                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                inputStream = socket.getInputStream();
    /*
     * notice:
     * inputStream.read() will block if no data return
     */
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "서버의 응답: " + response;

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
                        inputStream.close();
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
            chatList.add(new ChatModel(response, false));
            adapter.notifyDataSetChanged();
            
            //Text를 Speech로 변환하여 말해주는 부분.
            // TODO: 2017-10-17 추구 사람이 따라 말하기 설정을 on/off 했을경우 처리를 해주면 좋을듯.
            String text = response;
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            
            super.onPostExecute(result);
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //chatBarView.setMessageEditText(result.get(0));

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm"); // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    String formatDate = sdfNow.format(date); // nowDate 변수에 값을 저장한다.

                    chatList.add(new ChatModel(result.get(0), formatDate, true));
                    BackgroundTask myClientTask = new BackgroundTask("121.187.72.129", 8890, result.get(0));
                    myClientTask.execute();

                    //speakOut();
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void speakOut() {
        String text = chatBarView.getMessageText();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
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
