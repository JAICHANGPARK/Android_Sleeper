package com.dreamwalker.sleeper.FragmentMain;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamwalker.sleeper.Adapter.Spo2Adapter;
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
 * {@link Spo2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Spo2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Spo2Fragment extends Fragment {
    private static final String TAG = "Spo2Fragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public String address, port;

    private LineChart mLineChart;
    private String nowDate;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Spo2Adapter adapter;
    List<HeartRate> spo2List = new ArrayList<>();

    ArrayList<Entry> yMaxValue = new ArrayList<>();
    ArrayList<Entry> yAvgValue = new ArrayList<>();
    ArrayList<Entry> yMinValue = new ArrayList<>();

    public Spo2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Spo2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Spo2Fragment newInstance(String param1, String param2) {
        Spo2Fragment fragment = new Spo2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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



        new BackgroundTask().execute();

        //mLineChart.setDragEnabled(true);
        //mLineChart.setScaleEnabled(false);

//        // TODO: 11/10/17 최고치 라인을 설정한다.
//
//        LimitLine upperLine = new LimitLine(65f, "Danger");
//        upperLine.setLineWidth(4f); // 선의 두께를 결정한다.
//        upperLine.setLineColor(Color.RED); // 선의 색상을 결정한다.
//        upperLine.enableDashedLine(10f, 10f, 5f); // 점선으로 만들기 위해 다음과 같이 설정한다.
//        upperLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP); // Danger라는 라벨의 위치를 설정한다
//        upperLine.setTextSize(15f); // 라벨의 텍스트 사이즈를 설정한다.
//
//        // TODO: 11/10/17 최저 한계치 라인을 설정한다
//        LimitLine lowerLine = new LimitLine(30f, "Too low");
//        lowerLine.setLineWidth(4f);
//        lowerLine.setLineColor(Color.BLUE);
//        lowerLine.enableDashedLine(10f, 10f, 5f);
//        lowerLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        lowerLine.setTextSize(15f);
//
//        YAxis leftYAxis = mLineChart.getAxisLeft();
//        leftYAxis.removeAllLimitLines();
//        leftYAxis.addLimitLine(upperLine);
//        leftYAxis.addLimitLine(lowerLine);
////        leftYAxis.setAxisMaximum(100f);
////        leftYAxis.setAxisMinimum(20f);
//        leftYAxis.setDrawLimitLinesBehindData(true); // 한계 라인을 y축 왼쪽 라인과 바인딩한다.
//
//        mLineChart.getAxisRight().setEnabled(false); // 그래프의 y축 오른쪽 라벨을 지운다
//
//        ArrayList<Entry> yValues = new ArrayList<>();
//
//        yValues.add(new Entry(0, 60f));
//        yValues.add(new Entry(1, 120f));
//        yValues.add(new Entry(2, 30f));
//        yValues.add(new Entry(3, 50f));
//        yValues.add(new Entry(4, 66f));
//        yValues.add(new Entry(5, 12f));
//        yValues.add(new Entry(6, 30f));
//
//        LineDataSet lineDataSet = new LineDataSet(yValues, "DataSet01");
//        // TODO: 11/10/17 하나의 라인 데이터 세트의 라인 속성을 설정한다 .( 그려지는 부분의 설정)
//        lineDataSet.setFillAlpha(110);
//        lineDataSet.setColor(Color.RED);
//        lineDataSet.setLineWidth(2f);
//        lineDataSet.setValueTextSize(11f);
//
//        // TODO: 11/10/17 복수의 라인 데이터 가 존재한다면 아래와 같이 리스트를 만들고 개별 라인 데이터를 추가한다.
//        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
//        dataSet.add(lineDataSet);
//
//        LineData lineData = new LineData(dataSet);
//        mLineChart.setData(lineData);
//
//        String[] values = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"};
//
//        XAxis xAxis = mLineChart.getXAxis();
//        xAxis.setValueFormatter(new XAxisValueFormatter(values));
//        xAxis.setGranularity(1); // x축의 표시 간격을 이곳에서 설정한다.
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
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
        View view = inflater.inflate(R.layout.fragment_spo2, container, false);
        mLineChart = (LineChart) view.findViewById(R.id.spo2Linechart);
        recyclerView = (RecyclerView) view.findViewById(R.id.spo2RecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Spo2Adapter(spo2List, getActivity());
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://" + address + "/UserSpo.php?maindate=" + URLEncoder.encode(nowDate, "UTF-8");
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
                        max = object.getString("max");
                        min = object.getString("min");
                        avg = object.getString("avg");
                        date = object.getString("maindate");
                        spo2List.add(new HeartRate(min, max, avg, date));

                        f_max = Float.parseFloat(spo2List.get(cnt).getMaxHeartRate());
                        f_avg = Float.parseFloat(spo2List.get(cnt).getAvgHeartRate());
                        f_min = Float.parseFloat(spo2List.get(cnt).getMinHeartRate());
                        yMaxValue.add(new Entry(cnt, f_max));
                        yAvgValue.add(new Entry(cnt, f_avg));
                        yMinValue.add(new Entry(cnt, f_min));

                        //H.add(new Environment(temp, humi, gas, fire, dust, date, time));
                        Log.e(TAG, "heartRateList: " + spo2List.get(cnt).getMinHeartRate());
                        Log.e(TAG, "heartRateList: " + spo2List.get(cnt).getMaxHeartRate());
                        Log.e(TAG, "heartRateList: " + spo2List.get(cnt).getDate());
                        cnt++;
                    }

                    adapter.notifyDataSetChanged();

                    LineDataSet lineDataSet01 = new LineDataSet(yMaxValue, "최대값");
                    lineDataSet01.setColor(Color.RED);      // 선의 색상을 결정한다.
                    lineDataSet01.setDrawCircles(false);    // 선 위에 표기되는 동그란 원을 제거하거나 표기한다. default는 true
                    lineDataSet01.setLineWidth(3f);         // 그려지는 선의 굵기를 결정하는 메소드
                    LineDataSet lineDataSet02 = new LineDataSet(yAvgValue, "평균값");
                    LineDataSet lineDataSet03 = new LineDataSet(yMinValue, "최저값");
                    lineDataSet03.setColor(Color.BLUE);

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet01);
                    dataSets.add(lineDataSet02);
                    dataSets.add(lineDataSet03);

                    LineData lineData = new LineData(dataSets);
                    mLineChart.setData(lineData);

                    mLineChart.animateX(1000);

                    String[] value = new String[spo2List.size()];
                    for (int i = 0; i < spo2List.size(); i++) {
                        value[(spo2List.size() - 1) - i] = spo2List.get(i).getDate();
                        Log.e(TAG, i + "x string value : " + value[i]);
                    }

                    XAxis xAxis = mLineChart.getXAxis();
                    xAxis.setValueFormatter(new XAxisValueFormatter(value));
                    xAxis.setGranularity(1f);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    mLineChart.getAxisRight().setEnabled(false); // 그래프의 y축 오른쪽 라벨을 지운다
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}