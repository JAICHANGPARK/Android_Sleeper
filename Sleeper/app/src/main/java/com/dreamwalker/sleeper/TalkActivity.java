package com.dreamwalker.sleeper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamwalker.sleeper.Adapter.ChatAdapter;
import com.dreamwalker.sleeper.Model.ChatModel;

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

public class TalkActivity extends AppCompatActivity {

    private static final String TAG = "TalkActivity";
    private TextView textView;
    private ViewChatBar viewChatBar;
    private List<ChatModel> chatList = new ArrayList<>();
    public ChatAdapter adapter;
    private InputMethodManager imm;
    public Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //textView = (TextView) findViewById(R.id.textView);
        viewChatBar = (ViewChatBar) findViewById(R.id.charBar);
        viewChatBar.setMessageBoxHint("Enter your Message");

        setUpMessage();

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ChatAdapter(chatList, this);
        listView.setAdapter(adapter);

        viewChatBar.setSendClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                SimpleDateFormat sdfNow = new SimpleDateFormat("MM/dd HH:mm");
                // nowDate 변수에 값을 저장한다.
                String formatDate = sdfNow.format(date);
                //String time = calendar.getTime().toString();
                //textView.setText(viewChatBar.getMessageText());
                BackgroundTask myClientTask = new BackgroundTask("your Ip address", 8889, viewChatBar.getMessageText());
                myClientTask.execute();
                chatList.add(new ChatModel(viewChatBar.getMessageText(),formatDate,true));
                adapter.notifyDataSetChanged();
                imm.hideSoftInputFromWindow(viewChatBar.getWindowToken(), 0);
                viewChatBar.setClearMessage(true);
            }
        });
    }

    private void setUpMessage() {
        chatList.add(new ChatModel("Hello", true));
        chatList.add(new ChatModel("hi", false));
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
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
