package com.dreamwalker.sleeper.FragmentMain;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamwalker.sleeper.R;
import com.dreamwalker.sleeper.Utils.XAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Spo2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Spo2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Spo2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    private LineChart mLineChart;

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

        mLineChart = (LineChart)getView().findViewById(R.id.lineChart);

        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);

        // TODO: 11/10/17 최고치 라인을 설정한다.

        LimitLine upperLine = new LimitLine(65f, "Danger");
        upperLine.setLineWidth(4f); // 선의 두께를 결정한다.
        upperLine.setLineColor(Color.RED); // 선의 색상을 결정한다.
        upperLine.enableDashedLine(10f,10f,5f); // 점선으로 만들기 위해 다음과 같이 설정한다.
        upperLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP); // Danger라는 라벨의 위치를 설정한다
        upperLine.setTextSize(15f); // 라벨의 텍스트 사이즈를 설정한다.

        // TODO: 11/10/17 최저 한계치 라인을 설정한다 
        LimitLine lowerLine = new LimitLine(30f, "Too low");
        lowerLine.setLineWidth(4f);
        lowerLine.setLineColor(Color.BLUE);
        lowerLine.enableDashedLine(10f,10f,5f);
        lowerLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerLine.setTextSize(15f);

        YAxis leftYAxis = mLineChart.getAxisLeft();
        leftYAxis.removeAllLimitLines();
        leftYAxis.addLimitLine(upperLine);
        leftYAxis.addLimitLine(lowerLine);
//        leftYAxis.setAxisMaximum(100f);
//        leftYAxis.setAxisMinimum(20f);
        leftYAxis.setDrawLimitLinesBehindData(true); // 한계 라인을 y축 왼쪽 라인과 바인딩한다.

        mLineChart.getAxisRight().setEnabled(false); // 그래프의 y축 오른쪽 라벨을 지운다

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60f));
        yValues.add(new Entry(1, 120f));
        yValues.add(new Entry(2, 30f));
        yValues.add(new Entry(3, 50f));
        yValues.add(new Entry(4, 66f));
        yValues.add(new Entry(5, 12f));
        yValues.add(new Entry(6, 30f));

        LineDataSet lineDataSet = new LineDataSet(yValues, "DataSet01");
        // TODO: 11/10/17 하나의 라인 데이터 세트의 라인 속성을 설정한다 .( 그려지는 부분의 설정) 
        lineDataSet.setFillAlpha(110);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setValueTextSize(11f);

        // TODO: 11/10/17 복수의 라인 데이터 가 존재한다면 아래와 같이 리스트를 만들고 개별 라인 데이터를 추가한다.
        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(lineDataSet);

        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);

        String[] values = new String[]{"Jan","Feb","Mar","Apr","May","Jun", "Jul"};

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(values));
        xAxis.setGranularity(1); // x축의 표시 간격을 이곳에서 설정한다.
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
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
        return inflater.inflate(R.layout.fragment_spo2, container, false);
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
}
