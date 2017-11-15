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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HRFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HRFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private LineChart lineChart;

    public HRFragment() {
        // Required empty public constructor
    }


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        lineChart = getView().findViewById(R.id.linechart);
        setData(40,40);
        lineChart.animateX(1000);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
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
        return inflater.inflate(R.layout.fragment_hr, container, false);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * 랜덤으로 데이터를 생성하여 ArrayList에 데이터를 넣는 코드이다
     * 표기할 그래프의 수만큼 ArrayList를 생성하면 된다.
     * @param count
     * @param range
     */
    private void setData(int count, int range){

        // TODO: 10/5/17 첫 번째 라인의 그래프를 그리기 위한 데이터 생성.
        ArrayList<Entry> yValue01 = new ArrayList<>();
        for (int i = 0; i < count; i++){
            float val = (float) ((Math.random() * range)+250);
            yValue01.add(new Entry(i,val));
        }

        // TODO: 10/5/17 두 번째 라인의 그래프를 그리기 위한 데이터 생성.
        ArrayList<Entry> yValue02 = new ArrayList<>();
        for (int i = 0; i < count; i++){
            float val = (float) ((Math.random() * range)+150);
            yValue02.add(new Entry(i,val));
        }

        // TODO: 10/5/17 세 번째 라인의 그래프를 그리기 위한 데이터 생성.
        ArrayList<Entry> yValue03 = new ArrayList<>();
        for (int i = 0; i < count; i++){
            float val = (float) ((Math.random() * range)+50); // +값은 offset 이다.
            yValue03.add(new Entry(i,val));
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


}
