package com.dreamwalker.sleeper.FragmentMain;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamwalker.sleeper.Adapter.HeartRateAdapter;
import com.dreamwalker.sleeper.Model.HeartRate;
import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Utils.XAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HRFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HRFragment extends Fragment {
    private static final String TAG = "HRFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public String address, port, userID;

    private LineChart lineChart;
    private String nowDate;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HeartRateAdapter adapter;
    List<HeartRate> heartRateList = new ArrayList<>();

    ArrayList<Entry> yMaxValue = new ArrayList<>();
    ArrayList<Entry> yAvgValue = new ArrayList<>();
    ArrayList<Entry> yMinValue = new ArrayList<>();

    public HRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HRFragment newInstance(String param1, String param2) {
        HRFragment fragment = new HRFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hr, container, false);
        // TODO: 2017-11-16 라인차트를 인플레이트 합니다.
        lineChart = (LineChart)view.findViewById(R.id.linechart);
        recyclerView = (RecyclerView) view.findViewById(R.id.hr_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HeartRateAdapter(heartRateList, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: 2017-11-16  sharedPreferences에서 저장되어있는 주소 값과 포트값을 가져와야합니다.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SettingInit", Context.MODE_PRIVATE);
        address = sharedPreferences.getString("addressInit", "");
        port = sharedPreferences.getString("portInit", "");

        // TODO: 2017-11-15 만약 서버와 포트 값이 없다면 설정 화면으로 가도록 해야한다.
        if (!address.equals("") && !port.equals("")) {
            Log.e(TAG, "주소및포트값: " + "정상적으로 가져옴");

        } else {
            Log.e(TAG, "주소및포트값: " + "가져오지 못함.");
//            Toast.makeText(this, "서버의 주소와 포트를 설정해 주세요.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//            startActivity(intent);
        }

        // TODO: 2017-11-16 현재시간을 가져와야 합니다.  
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat dateYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateDay = new SimpleDateFormat("dd");

        String year = dateYear.format(date);
        String Month = dateMonth.format(date);
        String day = dateDay.format(date);

        year = year.substring(2);
        nowDate = year + "-" + Month + "-" + day;
        //Log.e(TAG, "onCreate: " + nowDate );
        Toast.makeText(getActivity(), nowDate, Toast.LENGTH_SHORT).show();

        // TODO: 2017-11-16 서버와 백그라운드 싱크 작업을 진행해야 합니다.
        new BackgroundGetTask().execute();

        //setData(40, 40);

    }
    /*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * 랜덤으로 데이터를 생성하여 ArrayList에 데이터를 넣는 코드이다
     * 표기할 그래프의 수만큼 ArrayList를 생성하면 된다.
     *
     * @param count
     * @param range
     */
    private void setData(int count, int range) {

        // TODO: 10/5/17 첫 번째 라인의 그래프를 그리기 위한 데이터 생성.
        ArrayList<Entry> yValue01 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) ((Math.random() * range) + 250);
            yValue01.add(new Entry(i, val));
        }

        // TODO: 10/5/17 두 번째 라인의 그래프를 그리기 위한 데이터 생성.
        ArrayList<Entry> yValue02 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) ((Math.random() * range) + 150);
            yValue02.add(new Entry(i, val));
        }

        // TODO: 10/5/17 세 번째 라인의 그래프를 그리기 위한 데이터 생성.
        ArrayList<Entry> yValue03 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) ((Math.random() * range) + 50); // +값은 offset 이다.
            yValue03.add(new Entry(i, val));
        }

        // TODO: 10/5/17 LineDataSet 객체를 생성하고 이곳에 위에서 생성한 데이터를 넣는다.

        LineDataSet lineDataSet01 = new LineDataSet(yValue01, "Data Set 01");

        lineDataSet01.setColor(Color.RED);      // 선의 색상을 결정한다.
        lineDataSet01.setDrawCircles(false);    // 선 위에 표기되는 동그란 원을 제거하거나 표기한다. default는 true
        lineDataSet01.setLineWidth(3f);         // 그려지는 선의 굵기를 결정하는 메소드

        LineDataSet lineDataSet02 = new LineDataSet(yValue02, "Data Set 02");

        LineDataSet lineDataSet03 = new LineDataSet(yValue03, "Data Set 03");


        // TODO: 10/5/17 LineData 객체 인스턴스를 생성한다 넣을 파라미터는 데이터의 LineDataSet Value이다.
        LineData lineData = new LineData(lineDataSet01, lineDataSet02, lineDataSet03);


        // TODO: 10/5/17 라인테이터를 차트 객체에 SetData 메소드를 호출하여 넣는다
        lineChart.setData(lineData);
    }


    class BackgroundGetTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://" + address + "/UserHR.php?maindate=" + URLEncoder.encode(nowDate, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null) {
                    Log.e(TAG, "temp: " + temp);
                    stringBuilder.append(temp + "\n");
                    Log.e(TAG, "stringBuilder: " + stringBuilder.toString());
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            int cnt = 0;
            JSONObject jsonObject;
            String max, min, avg, date;
            Float f_max, f_min, f_avg;

            try {
                jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("response");

                Log.e(TAG, "jsonArray: " + jsonArray);
                Log.e(TAG, "jsonArray: " + jsonArray.length());

                if (jsonArray.length() == 0) {

                } else {

                    while (cnt < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(cnt);
                        max = object.getString("heartrate");
                        min = object.getString("min");
                        avg = object.getString("avg");
                        date = object.getString("maindate");
                        heartRateList.add(new HeartRate(min, max, avg, date));

                        f_max = Float.parseFloat(heartRateList.get(cnt).getMaxHeartRate());
                        f_avg = Float.parseFloat(heartRateList.get(cnt).getAvgHeartRate());
                        f_min = Float.parseFloat(heartRateList.get(cnt).getMinHeartRate());
                        yMaxValue.add(new Entry(cnt, f_max));
                        yAvgValue.add(new Entry(cnt, f_avg));
                        yMinValue.add(new Entry(cnt, f_min));

                        //H.add(new Environment(temp, humi, gas, fire, dust, date, time));
                        Log.e(TAG, "heartRateList: " + heartRateList.get(cnt).getMinHeartRate());
                        Log.e(TAG, "heartRateList: " + heartRateList.get(cnt).getMaxHeartRate());
                        Log.e(TAG, "heartRateList: " + heartRateList.get(cnt).getDate());

                        cnt++;
                    }

                    adapter.notifyDataSetChanged();

                    LineDataSet lineDataSet01 = new LineDataSet(yMaxValue, "최대값");
                    lineDataSet01.setColor(Color.RED);      // 선의 색상을 결정한다.
                    lineDataSet01.setDrawCircles(false);    // 선 위에 표기되는 동그란 원을 제거하거나 표기한다. default는 true
                    lineDataSet01.setLineWidth(3f);         // 그려지는 선의 굵기를 결정하는 메소드
                    LineDataSet lineDataSet02 = new LineDataSet(yAvgValue, "평균값");
                    LineDataSet lineDataSet03 = new LineDataSet(yMinValue, "최저값");

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet01);
                    dataSets.add(lineDataSet02);
                    dataSets.add(lineDataSet03);

                    LineData lineData = new LineData(dataSets);
                    lineChart.setData(lineData);

                    lineChart.animateX(1000);

                    int arraySize = jsonArray.length();
                    int index = arraySize - 1;

                    String[] value = new String[arraySize];
                    for (int i = 0; i < arraySize; i++){
                        String temp = heartRateList.get(i).getDate();
                        value[index - i] = temp;
                        Log.e(TAG, (index - i) +","+ value[index- i] + ", " + i + "x string value : " + temp);
                    }

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setValueFormatter(new XAxisValueFormatter(value));
                    xAxis.setGranularity(1f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
