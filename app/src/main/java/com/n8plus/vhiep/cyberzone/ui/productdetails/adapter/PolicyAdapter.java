package com.n8plus.vhiep.cyberzone.ui.productdetails.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.Policy;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.MyViewHolder> {
    private List<Policy> policyList;

    public PolicyAdapter(List<Policy> policyList) {
        this.policyList = policyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_policy_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Policy policy = policyList.get(position);
        holder.img_policyIcon.setImageResource(policy.getIcon());
        holder.txt_policyName.setText(policy.getName());
    }

    @Override
    public int getItemCount() {
        return policyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_policyIcon;
        TextView txt_policyName;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_policyIcon = (ImageView) itemView.findViewById(R.id.img_policyIcon);
            txt_policyName = (TextView) itemView.findViewById(R.id.txt_policyName);
        }
    }
}
