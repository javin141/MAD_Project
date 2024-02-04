package com.sp.mad_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private OnItemClickListener onItemClickListener;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);

        // Return a new holder instance
        return new RecipeViewHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // Get the data model based on position
        Recipe recipe = recipeList.get(position);

        // Set item views based on your views and data model
        holder.recipeNameTextView.setText(recipe.getName());

        // Combine calories and prepTime with " / " in between
        String caloriesAndPrepTime = recipe.getCalories() + " / " + recipe.getPrepTime();
        holder.caloriesAndPrepTimeTextView.setText(caloriesAndPrepTime);

        // Set the username and adjust visibility accordingly
        holder.usernameTextView.setText(recipe.getUsername());
        if (recipe.getUsername() != null && !recipe.getUsername().isEmpty()) {
            holder.usernameCardView.setVisibility(View.VISIBLE);
        } else {
            holder.usernameCardView.setVisibility(View.GONE);
        }

        // Use BitmapUtils to convert byte array to Bitmap and set to ImageView
        if (recipe.getImage() != null) {
            Bitmap bitmap = BitmapUtils.getImage(recipe.getImage());
            holder.recipeImageView.setImageBitmap(bitmap);
        } else {
            // Set a default image if the byte array is null
            holder.recipeImageView.setImageResource(R.drawable.sample_food);
        }

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(recipe.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(long recipeId);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeNameTextView;
        public TextView caloriesAndPrepTimeTextView;
        public ImageView recipeImageView;
        public TextView usernameTextView;
        public View usernameCardView;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            // Your item views
            recipeNameTextView = itemView.findViewById(R.id.recipeName);
            caloriesAndPrepTimeTextView = itemView.findViewById(R.id.caloriesAndPrepTime);
            recipeImageView = itemView.findViewById(R.id.recipeImage);
            usernameTextView = itemView.findViewById(R.id.username);
            usernameCardView = itemView.findViewById(R.id.usernameCardView);
        }
    }
}
