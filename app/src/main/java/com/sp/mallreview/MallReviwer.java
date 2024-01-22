package com.sp.mallreview;

import static android.text.method.TextKeyListener.clear;

import static androidx.core.view.PointerIconCompat.load;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MallReviwer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MallReviwer extends Fragment {
    private EditText mallName;
    private EditText mallDate;
    private EditText mallDesc;
    private Button buttonSave;
    private Button buttonMap;

    private Malls helper = null;
    private String mallID = null;
    private RadioGroup mallScore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView location = null;
    private GPSTracker gpsTracker = null;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private double MyLatitude = 0.0d;
    private double MyLongitude = 0.0d;
    private Button buttonlocation;

    public MallReviwer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MallReviwer.
     */
    // TODO: Rename and change types and number of parameters
    public static MallReviwer newInstance(String param1, String param2) {
        MallReviwer fragment = new MallReviwer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mallID = null;
       helper = new Malls(getContext());

        gpsTracker = new GPSTracker(getActivity())  ;


       getParentFragmentManager().setFragmentResultListener("listToDetailKey", this, new FragmentResultListener() {
            @Override
           public void onFragmentResult(String key, Bundle bundle) {
                mallID = bundle.getString("id");
                if (mallID != null) {
                    load();
                } else {
                    clear();
                }
            }
       });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        helper.close();
        gpsTracker.stopUsingGPS();
    }
    //inflates and identifies assets
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mall_reviwer, container, false);
        mallName = view.findViewById(R.id.mall_name_input);
        mallDate = view.findViewById(R.id.date_input);
        mallDesc = view.findViewById(R.id.description_input);
        buttonSave = view.findViewById(R.id.button);
        buttonSave.setOnClickListener(onSave);
        buttonlocation = view.findViewById(R.id.location_button);
        buttonlocation.setOnClickListener(onLocation);
        buttonMap = view.findViewById(R.id.get_map);
        buttonMap.setOnClickListener(onMap);
        mallScore = view.findViewById(R.id.RadioG);
        location = view.findViewById(R.id.location_view);

        return view;
    }
   //clears spaces for new entry
    private void clear() {
        mallName.setText("");
        mallDate.setText("");
        mallDesc.setText("");
        mallScore.clearCheck();
    }

    //retrieves entry
    private void load() {
        Cursor c = helper.getById(mallID);
        c.moveToFirst();
        mallName.setText(helper.getmallName(c));
        mallDate.setText(helper.getmallDate(c));
        mallDesc.setText(helper.getmallDesc(c));

        if (helper.getmallScore(c).equals("1")) {
            mallScore.check(R.id.radioButton);
        } else if (helper.getmallScore(c).equals("2")) {
            mallScore.check(R.id.radioButton2);
        } else if (helper.getmallScore(c).equals("3")) {
            mallScore.check(R.id.radioButton3);
        } else if (helper.getmallScore(c).equals("4")) {
            mallScore.check(R.id.radioButton4);
        } else {
            mallScore.check(R.id.radioButton5);
        }

        latitude = helper.getLatitude(c);
        longitude = helper.getLongitude(c);
        location.setText(String.valueOf(latitude) + "," + String.valueOf(longitude));
    }

    //retrieves location from gps and displays it in textview
    private View.OnClickListener onLocation = new View.OnClickListener(){
    @Override
            public void onClick(View v) {
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                location.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));
            }
        //return false;
    }
    };


    public View.OnClickListener onMap = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyLatitude = gpsTracker.getLatitude();
            MyLongitude = gpsTracker.getLongitude();

            Intent intent = new Intent(getActivity(), MallsLocation.class);
            intent.putExtra("LATITUDE", latitude);
            intent.putExtra("LONGITUDE", longitude);
            intent.putExtra("MYLATITUDE", MyLatitude);
            intent.putExtra("MYLONGITUDE", MyLongitude);
            intent.putExtra("NAME", mallName.getText().toString());
            startActivity(intent);
          //  return (true);
        }
    };




    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nameStr = mallName.getText().toString();
            String dateStr = mallDate.getText().toString();
            String descStr = mallDesc.getText().toString();
            String scoreStr = "";
            int radioID = mallScore.getCheckedRadioButtonId();
            if (radioID == R.id.radioButton){
                 scoreStr = "1";
            } else if (radioID == R.id.radioButton2){
                scoreStr = "2";
            } else if (radioID == R.id.radioButton3){
                scoreStr = "3";
            } else if (radioID == R.id.radioButton4){
                scoreStr = "4";
            } else {
                scoreStr = "5";
            }

            if (mallID == null) {
               helper.insert(nameStr, dateStr, descStr, scoreStr, latitude, longitude);
            } else {
                helper.update(mallID, nameStr, dateStr, descStr, scoreStr, latitude, longitude);
            }
            BottomNavigationView nav = (BottomNavigationView)getActivity().findViewById(R.id.BottomNavigationView);
            nav.setSelectedItemId(R.id.mall_recyclerview);
            Toast.makeText(getActivity(), "Data Saved", Toast.LENGTH_LONG).show();
            //return (false);
        }
    };
}

