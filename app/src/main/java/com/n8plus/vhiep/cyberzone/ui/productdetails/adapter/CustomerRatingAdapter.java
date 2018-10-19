package com.n8plus.vhiep.cyberzone.ui.productdetails.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;

import java.text.DateFormat;
import java.util.List;

public class CustomerRatingAdapter extends RecyclerView.Adapter<CustomerRatingAdapter.ViewHolder> {

    private List<ReviewProduct> ratingList;

    public CustomerRatingAdapter(List<ReviewProduct> ratingList) {
        this.ratingList = ratingList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameCustomer, dateRating, contentRating;
        RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            nameCustomer = (TextView) itemView.findViewById(R.id.tv_name_customer_rating);
            dateRating = (TextView) itemView.findViewById(R.id.tv_date_customer_rating);
            contentRating = (TextView) itemView.findViewById(R.id.tv_content_customer_rating);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rbr_rate);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rating_custom, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewProduct ratingProduct = ratingList.get(position);
        holder.nameCustomer.setText(ratingProduct.getCustomer().getName());
        holder.dateRating.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(ratingProduct.getDateReview()));
        holder.contentRating.setText(ratingProduct.getReview());
        holder.ratingBar.setRating(ratingProduct.getRatingStar().getRatingStar());
    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

}
