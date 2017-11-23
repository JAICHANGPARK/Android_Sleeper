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

import com.dreamwalker.sleeper.Adapter.SoundAdapter;
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
 * {@link SoundFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SoundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoundFragment extends Fragment {

    private static final String TAG = "SoundFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    // TODO: 2017-11-16 우리만의 변수들
    public String address, port;

    private LineChart mLineChart;
    private String nowDate;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SoundAdapter adapter;
    List<HeartRate> soundList = new ArrayList<>();

    ArrayList<Entry> yMaxValue = new ArrayList<>();
    ArrayList<Entry> yAvgValue = new ArrayList<>();
    ArrayList<Entry> yMinValue = new ArrayList<>();

    public SoundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SoundFragment newInstance(String param1, String param2) {
        SoundFragment fragment = new SoundFragment();
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
        View view = inflater.inflate(R.layout.fragment_sound, container, false);
        mLineChart = (LineChart) view.findViewById(R.id.soundLinechart);
        recyclerView = (RecyclerView) view.findViewById(R.id.soundRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SoundAdapter(getActivity(), soundList);
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

   /* @Override
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

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://" + address + "/UserSound.php?maindate=" + URLEncoder.encode(nowDate, "UTF-8");
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
                        soundList.add(new HeartRate(min, max, avg, date));

                        f_max = Float.parseFloat(soundList.get(cnt).getMaxHeartRate());
                        f_avg = Float.parseFloat(soundList.get(cnt).getAvgHeartRate());
                        f_min = Float.parseFloat(soundList.get(cnt).getMinHeartRate());
                        yMaxValue.add(new Entry(cnt, f_max));
                        yAvgValue.add(new Entry(cnt, f_avg));
                        yMinValue.add(new Entry(cnt, f_min));

                        //H.add(new Environment(temp, humi, gas, fire, dust, date, time));
                        Log.e(TAG, "heartRateList: " + soundList.get(cnt).getMinHeartRate());
                        Log.e(TAG, "heartRateList: " + soundList.get(cnt).getMaxHeartRate());
                        Log.e(TAG, "heartRateList: " + soundList.get(cnt).getDate());
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

                    String[] xValueSound = new String[soundList.size()];
                    for (int i = 0; i < soundList.size(); i++) {
                        xValueSound[(soundList.size() - 1) - i] = soundList.get(i).getDate();
                        Log.e(TAG, i + "x string value : " + xValueSound[i]);
                    }

                    XAxis xAxis = mLineChart.getXAxis();
                    xAxis.setValueFormatter(new XAxisValueFormatter(xValueSound));
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
