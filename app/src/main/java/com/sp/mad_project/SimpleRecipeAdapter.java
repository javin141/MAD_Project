package com.sp.mad_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SimpleRecipeAdapter extends RecyclerView.Adapter<SimpleRecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public SimpleRecipeAdapter(List<Recipe> recipeList) {
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
        holder.caloriesAndPrepTimeTextView.setText(recipe.getCaloriesAndPrepTime());
        holder.recipeImageView.setImageResource(recipe.getImageResource());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // Provide a direct reference to each of the views within a data item
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeNameTextView;
        public TextView caloriesAndPrepTimeTextView;
        public ImageView recipeImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            // Your item views
            recipeNameTextView = itemView.findViewById(R.id.recipeName);
            caloriesAndPrepTimeTextView = itemView.findViewById(R.id.caloriesAndPrepTime);
            recipeImageView = itemView.findViewById(R.id.recipeImage);
        }
    }
}
