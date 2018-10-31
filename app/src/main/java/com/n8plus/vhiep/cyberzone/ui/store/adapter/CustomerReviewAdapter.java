package com.n8plus.vhiep.cyberzone.ui.store.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;

import java.text.DateFormat;
import java.util.List;

public class CustomerReviewAdapter extends RecyclerView.Adapter<CustomerReviewAdapter.ViewHolder> {

    private List<ReviewStore> reviewStoreList;

    public CustomerReviewAdapter(List<ReviewStore> reviewStoreList) {
        this.reviewStoreList = reviewStoreList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameCustomer, dateReview, contentReview;
        ImageView review;
        public ViewHolder(View itemView) {
            super(itemView);
            nameCustomer = (TextView) itemView.findViewById(R.id.tv_name_customer_review);
            dateReview = (TextView) itemView.findViewById(R.id.tv_date_customer_review);
            contentReview = (TextView) itemView.findViewById(R.id.tv_content_customer_review);
            review = (ImageView) itemView.findViewById(R.id.img_customer_rate);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_reviews_custom, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewStore reviewStore = reviewStoreList.get(position);
        holder.nameCustomer.setText(reviewStore.getCustomer().getName());
        holder.dateReview.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(reviewStore.getDateReview()));
        switch (reviewStore.getRatingLevel().getLevel()){
            case 1:
                holder.review.setImageResource(R.drawable.emoji_good);
                break;
            case 2:
                holder.review.setImageResource(R.drawable.emoji_normal);
                break;
            case 3:
                holder.review.setImageResource(R.drawable.emoji_notgood);
                break;
        }
        holder.contentReview.setText(reviewStore.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewStoreList.size();
    }

}
