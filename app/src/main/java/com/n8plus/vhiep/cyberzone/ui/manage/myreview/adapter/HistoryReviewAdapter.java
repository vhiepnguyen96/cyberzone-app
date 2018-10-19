package com.n8plus.vhiep.cyberzone.ui.manage.myreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.HistoryReview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryReviewAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<HistoryReview> historyReviewList;

    public HistoryReviewAdapter(Context context, int resource, List<HistoryReview> historyReviewList) {
        this.context = context;
        this.resource = resource;
        this.historyReviewList = historyReviewList;
    }

    public class ViewHolder {
        public TextView dateReview, storeName, productName, reviewStore, reviewProduct;
        public ImageView ratingStore, imageProduct;
        public RatingBar ratingProduct;
    }

    @Override
    public int getCount() {
        return historyReviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyReviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.dateReview = (TextView) convertView.findViewById(R.id.tv_date_review);
            holder.storeName = (TextView) convertView.findViewById(R.id.tv_store_name_review);
            holder.productName = (TextView) convertView.findViewById(R.id.tv_product_name_review);
            holder.reviewStore = (TextView) convertView.findViewById(R.id.tv_review_store);
            holder.reviewProduct = (TextView) convertView.findViewById(R.id.tv_review_product);
            holder.ratingStore = (ImageView) convertView.findViewById(R.id.img_rating_store);
            holder.imageProduct = (ImageView) convertView.findViewById(R.id.img_product_review);
            holder.ratingProduct = (RatingBar) convertView.findViewById(R.id.rbr_rating_product);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HistoryReview historyReview = historyReviewList.get(position);
//        holder.dateReview.setText(historyReview.getReviewStore().getDateReview());
        holder.storeName.setText(historyReview.getReviewStore().getStore().getStoreName());
        holder.productName.setText(historyReview.getReviewProduct().getProduct().getProductName());
        holder.reviewStore.setText(historyReview.getReviewStore().getReview());
        holder.reviewProduct.setText(historyReview.getReviewProduct().getReview());
        holder.ratingStore.setImageResource(historyReview.getReviewStore().getRatingLevel());
        if (historyReview.getReviewProduct().getProduct().getImageList() != null && historyReview.getReviewProduct().getProduct().getImageList().size() > 0){
            Picasso.get().load(historyReview.getReviewProduct().getProduct().getImageList().get(0).getImageURL()).into(holder.imageProduct);
        }
//        holder.imageProduct.setImageResource(historyReview.getReviewProduct().getProduct().getImageList().get(0).getImageId());
        holder.ratingProduct.setRating(historyReview.getReviewProduct().getRatingStar().getRatingStar());
        return convertView;
    }
}
