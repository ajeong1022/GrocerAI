package com.example.grocerai;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroceryListAdapter extends RecyclerView.Adapter {

    private ArrayList<Ingredient> ingredients;
    private int unchecked;

    public GroceryListAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
        unchecked = ingredients.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View textView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_grocery_list, parent, false);
        return new GroceryListViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Ingredient ingredient = ingredients.get(position);
        final GroceryListViewHolder viewHolder = (GroceryListViewHolder) holder;
        final TextView textView = viewHolder.ingredientView;
        textView.setText(ingredient.getName());
        textView.setTypeface(textView.getTypeface(),
                ingredient.isChecked() ? Typeface.NORMAL : Typeface.BOLD);
        viewHolder.checkView.setVisibility(ingredient.isChecked() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toggle item bold
                ingredient.setChecked(!ingredient.isChecked());
                if (ingredient.isChecked()) {
                    textView.setTypeface(Typeface.create(textView.getTypeface(),Typeface.NORMAL));
                    viewHolder.checkView.setVisibility(View.VISIBLE);
                    unchecked--;
                } else {
                    textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.BOLD));
                    viewHolder.checkView.setVisibility(View.INVISIBLE);
                    unchecked++;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public int getUnchecked() {
        return unchecked;
    }

    public class GroceryListViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientView;
        private ImageView checkView;

        public GroceryListViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientView = itemView.findViewById(R.id.tv_grocery_list_item_name);
            checkView = itemView.findViewById(R.id.iv_grocery_list_item_check);
        }
    }
}
