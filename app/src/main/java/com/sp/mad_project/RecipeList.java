package com.sp.mad_project;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Outlets extends AppCompatActivity {

    private RecyclerView recyclerViewOutlets;
    private OutletAdapter adapter;
    private DBHelper dbHelper;

    private int visibleThreshold = 5; // Adjust this threshold as needed
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outlets);

        dbHelper = new DBHelper(this);

        recyclerViewOutlets = findViewById(R.id.recyclerViewOutlets);
        recyclerViewOutlets.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty list
        adapter = new OutletAdapter();
        recyclerViewOutlets.setAdapter(adapter);

        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewOutlets.getLayoutManager();
        recyclerViewOutlets.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached, load more data
                    loadMoreData();
                    loading = true;
                }
            }
        });

        // Load initial data
        loadMoreData();

        FloatingActionButton fabAddOutlet = findViewById(R.id.fabAddOutlet);
        fabAddOutlet.setOnClickListener(view -> {
            Intent intent = new Intent(Outlets.this, Add_Outlet.class);
            startActivity(intent);
        });

        // Button navigation logic
        findViewById(R.id.btnAbout).setOnClickListener(view -> {
            // Handle the "About" button click
            startActivity(new Intent(Outlets.this, About.class));
        });

        findViewById(R.id.btnHome).setOnClickListener(view -> {
            // Handle the "Home" button click
            startActivity(new Intent(Outlets.this, Homepage.class));
        });

        findViewById(R.id.btnExit).setOnClickListener(view -> {
            // Handle the "Exit" button click
            finishAffinity(); // Close the entire app
        });
    }

    private void loadMoreData() {
        List<Map<String, Object>> moreData = dbHelper.getAllOutlets();
        adapter.addData(moreData);
        loading = false;
    }

    // OutletAdapter class (assuming you have it)
    private class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.ViewHolder> {

        private final List<Map<String, Object>> outletList;

        OutletAdapter() {
            outletList = dbHelper.getAllOutlets();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.outlet_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Use modulo to get the correct position within the data size
            Map<String, Object> outlet = outletList.get(position % outletList.size());
            holder.bind(outlet);
        }

        @Override
        public int getItemCount() {
            // Return a large number to make it repeat indefinitely using modulo
            return outletList.size() > 0 ? Integer.MAX_VALUE : 0;
        }

        void addData(List<Map<String, Object>> newData) {
            outletList.addAll(newData);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textName;
            TextView textLocation;
            TextView textContact;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageOutlet);
                textName = itemView.findViewById(R.id.textName);
                textLocation = itemView.findViewById(R.id.textLocation);
                textContact = itemView.findViewById(R.id.textContact);

                // Set click listener for each item
                itemView.setOnClickListener(view -> {
                    // Get the outlet at the clicked position
                    Map<String, Object> outlet = dbHelper.getAllOutlets().get(getAdapterPosition());

                    // Create an intent to open Add_Outlet with the selected outlet's details
                    Intent intent = new Intent(Outlets.this, Add_Outlet.class);
                    HashMap<String, Object> outletMap = new HashMap<>(outlet);

                    intent.putExtra("outletDetails", outletMap);
                    startActivity(intent);
                });
            }

            void bind(Map<String, Object> outlet) {
                // Set the outlet information to the TextViews
                textName.setText(outlet.get(DBHelper.COLUMN_NAME).toString());
                textLocation.setText(outlet.get(DBHelper.COLUMN_AREA).toString());
                textContact.setText(outlet.get(DBHelper.COLUMN_PHONE_NO).toString());

                // For loading the image from the database
                byte[] imageBytes = (byte[]) outlet.get(DBHelper.COLUMN_IMAGE);
                if (imageBytes != null) {
                    imageView.setImageBitmap(BitmapUtils.getImage(imageBytes));
                }
            }
        }
    }
}