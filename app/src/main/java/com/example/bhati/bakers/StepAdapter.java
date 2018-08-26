package com.example.bhati.bakers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Context mContext;
    private List<Step> mSteps;
    private HandleClick mHandleClick;


    public interface HandleClick{
        void onClick(int position);
    }

    public StepAdapter(Context context, List<Step> steps, HandleClick handleClick) {
        mContext = context;
        mSteps = steps;
        mHandleClick = handleClick;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new StepViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, final int position) {
        final Step step = mSteps.get(position);
        holder.mStepDescription.setText(step.getShortDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandleClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSteps!=null?mSteps.size():0;
    }

    class StepViewHolder extends RecyclerView.ViewHolder{
        TextView mStepDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
            mStepDescription = itemView.findViewById(R.id.tv_recipe_title);
        }
    }
}
