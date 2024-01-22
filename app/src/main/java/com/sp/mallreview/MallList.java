package com.sp.mallreview;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MallList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MallList extends Fragment {
    private RecyclerView list;
    private Cursor model = null;
    private mallAdapter adapter = null;
    private Malls helper = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MallList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MallList.
     */
    // TODO: Rename and change types and number of parameters
    public static MallList newInstance(String param1, String param2) {
        MallList fragment = new MallList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new Malls(getContext());
        model = helper.getAll();
        adapter = new mallAdapter(getContext(), model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mall_list, container, false);
        list = view.findViewById(R.id.mall_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (model != null) {
            model.close();
        }
        model = helper.getAll();
        adapter.swapCursor(model);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        helper.close();
    }

    public class mallAdapter extends RecyclerView.Adapter<mallAdapter.mallHolder> {
        private Context context;
        private Malls helper = null;
        private Cursor cursor;

        mallAdapter(Context context, Cursor cursor) {
            this.context = context;
            this.cursor = cursor;
            helper = new Malls(context);
        }

        public void swapCursor(Cursor newcursor) {
            Cursor oldCursor = this.cursor;
            this.cursor = newcursor;
            oldCursor.close();
        }


        @Override
        public mallAdapter.mallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new mallHolder(view);
        }
        //retrieves data from DB to display in list
        @Override
        public void onBindViewHolder(mallAdapter.mallHolder holder, final int position) {
            if (!cursor.moveToPosition(position)) {
                return;
            }
            holder.mallName.setText(helper.getmallName(cursor));
            holder.mallDate.setText(helper.getmallDate(cursor));
            holder.mallDesc.setText(helper.getmallDesc(cursor));
            //Parse Double values to String , as setText() only accepts String values
            String x = Double.toString(helper.getLongitude(cursor));
            String y = Double.toString(helper.getLatitude(cursor));
            holder.mallLat.setText(x);
            holder.mallLon.setText(y);
            if (helper.getmallScore(cursor).equals("1")) {
                holder.mallScore.setImageResource(R.drawable.one_red);
            } else if (helper.getmallScore(cursor).equals("2")) {
                holder.mallScore.setImageResource(R.drawable.two_yellow);
            } else if (helper.getmallScore(cursor).equals("3")) {
                holder.mallScore.setImageResource(R.drawable.three_green);
            } else if (helper.getmallScore(cursor).equals("4")) {
                holder.mallScore.setImageResource(R.drawable.four_cyan);
            } else {
                holder.mallScore.setImageResource(R.drawable.five_indigo);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                //retrieves corresponding data entry and returns to review fragment for editing
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    cursor.moveToPosition(position);
                    String recordID = helper.getID(cursor);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",recordID);
                    getParentFragmentManager().setFragmentResult("listToDetailKey", bundle);
                    BottomNavigationView nav = (BottomNavigationView) getActivity().findViewById(R.id.BottomNavigationView);
                    nav.setSelectedItemId(R.id.review);
                    //return false;
                }
            });
        }


        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class mallHolder extends RecyclerView.ViewHolder {
            private TextView mallName = null;
            private TextView mallDate = null;
            private TextView mallDesc = null;
            private ImageView mallScore = null;
            private TextView mallLat = null;
            private TextView mallLon =null;

            public mallHolder(View itemView) {
                super(itemView);
                mallName = itemView.findViewById(R.id.MallName);
                mallDate = itemView.findViewById(R.id.MallDate);
                mallDesc = itemView.findViewById(R.id.MallDesc);
                mallScore = itemView.findViewById(R.id.scoreview);
                mallLat = itemView.findViewById(R.id.malllat);
                mallLon = itemView.findViewById(R.id.malllon);
            }
        }
    }
}