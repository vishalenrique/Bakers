package com.example.bhati.bakers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    Context mContext;
    List<Ingredient> mIngredients;


    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        holder.mIngredientDescription.setText(ingredient.getIngredient());
        String measure = ingredient.getQuantity() + " " + ingredient.getMeasure();
        holder.mIngredientQuantity.setText(measure);
        holder.mIngredientCount.setText(mContext
                        .getString(R.string.ingredient_identifier,++position,mIngredients.size()));
    }

    @Override
    public int getItemCount() {
        return mIngredients != null ? mIngredients.size() : 0;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        // ImageView mLeftArrow;
        TextView mIngredientDescription;
        TextView mIngredientQuantity;
        TextView mIngredientCount;
        // ImageView mRightArrow;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            //  mLeftArrow = itemView.findViewById(R.id.iv_ingredient_left);
            mIngredientDescription = itemView.findViewById(R.id.tv_ingredient_title);
            mIngredientQuantity = itemView.findViewById(R.id.tv_ingredient_quantity);
            mIngredientCount = itemView.findViewById(R.id.tv_ingredient_count);
            //  mRightArrow = itemView.findViewById(R.id.iv_ingredient_right);
        }
    }
}
