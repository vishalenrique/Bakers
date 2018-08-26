package com.example.bhati.bakers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipes;
    private HandleClick mHandleClick;


    public interface HandleClick{
        void onClick(Recipe recipe);
    }


    public RecipeAdapter(Context context, List<Recipe> recipes, HandleClick handleClick) {
        mContext = context;
        mRecipes = recipes;
        mHandleClick = handleClick;
    }

    public void setRecipes(List<Recipe> recipes) {
        if(recipes == null)
            return;

        mRecipes = recipes;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new RecipeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        final Recipe recipe = mRecipes.get(position);
        holder.mRecipeTitle.setText(recipe.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandleClick.onClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipes!=null?mRecipes.size():0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView mRecipeTitle;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeTitle = itemView.findViewById(R.id.tv_recipe_title);
        }
    }
}
