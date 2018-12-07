package com.n8plus.vhiep.cyberzone.ui.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.n8plus.vhiep.cyberzone.util.LoadMoreRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoadMoreProductAdapter extends LoadMoreRecyclerViewAdapter<Product> {
    private static final String TAG = "LoadMoreProductAdapter";
    private static int TYPE_PROGRESS = 0xFFFF;
    private static int TYPE_ITEM = 1;
    private static final int GRID_LAYOUT = 1;
    private static final int LINEAR_LAYOUT = 2;
    private int layout;

    public LoadMoreProductAdapter(@NonNull Context context, ItemClickListener itemClickListener, @NonNull RetryLoadMoreListener retryLoadMoreListener, int layoutManager) {
        super(context, itemClickListener, retryLoadMoreListener);
        layout = layoutManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != TYPE_PROGRESS) {
            View view = mInflater.inflate(layout == GRID_LAYOUT ? R.layout.row_product_grid_layout : R.layout.row_product_item_vertical, parent, false);
            if (layout == GRID_LAYOUT) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (int) (parent.getWidth() * 0.5);
                view.setLayoutParams(layoutParams);
            }
            return new ProductViewHolder(view);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ProductViewHolder) {
            ProductViewHolder holder = (ProductViewHolder) viewHolder;
            Product product = mDataList.get(position);
            DecimalFormat df = new DecimalFormat("#.000");

            if (product.getImageList() != null) {
                if (product.getImageList().get(0).getImageURL() != null && !product.getImageList().get(0).getImageURL().isEmpty()) {
                    Uri uriImage = Uri.parse(product.getImageList().get(0).getImageURL());
                    System.out.println("Image url: " + uriImage);
                    Picasso.get().load(uriImage).error(R.drawable.ic_image_gray_24dp).into(holder.mImageProduct);
                } else {
                    holder.mImageProduct.setImageResource(R.drawable.ic_image_gray_24dp);
                }
            }
            holder.mTextProductName.setText(product.getProductName());
            if (product.getSaleOff() != null && product.getSaleOff().getDiscount() > 0) {
                int basicPrice = Integer.valueOf(product.getPrice());
                int discount = product.getSaleOff().getDiscount();
                int salePrice = basicPrice - (basicPrice * discount / 100);

                holder.mTextProductBasicPrice.setText(basicPrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(basicPrice))).replace(",", ".") : String.valueOf(basicPrice));
                holder.mTextProductPrice.setText(salePrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(salePrice))).replace(",", ".") : String.valueOf(salePrice));

                holder.mTextProductDiscount.setText(String.valueOf(product.getSaleOff().getDiscount()));
                holder.mLinearDiscount.setVisibility(View.VISIBLE);
            } else {
                holder.mTextProductPrice.setText(Integer.valueOf(product.getPrice()) > 1000 ? df.format(Product.convertToPrice(product.getPrice())).replace(",", ".") : product.getPrice());
                holder.mLinearDiscount.setVisibility(View.GONE);
            }

            if (product.getReviewProducts() != null && product.getReviewProducts().size() > 0) {
                holder.mLinearProductRating.setVisibility(View.VISIBLE);
                holder.mTextProductRatingCount.setText(String.valueOf(product.getReviewProducts().size()));

                holder.mRatingProduct.setRating(product.getAverageReview());
            }

            holder.mTextStoreLocation.setText(product.getStore().getLocation());

            holder.mTextProductQuantity.setVisibility(product.getQuantity() == 0 ? View.VISIBLE : View.INVISIBLE);

        }
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    protected int getCustomItemViewType(int position) {
        if (position == bottomItemPosition()) {
            return TYPE_PROGRESS;
        } else return position;
    }


    private int bottomItemPosition() {
        return getItemCount() - 1;
    }

    private class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageProduct;
        private TextView mTextProductName, mTextProductPrice, mTextProductBasicPrice, mTextProductDiscount, mTextProductRatingCount, mTextStoreLocation, mTextProductQuantity;
        private LinearLayout mLinearDiscount, mLinearProductRating;
        private RatingBar mRatingProduct;

        ProductViewHolder(View itemView) {
            super(itemView);

            mImageProduct = (ImageView) itemView.findViewById(R.id.img_ver_product);
            mTextProductName = (TextView) itemView.findViewById(R.id.txt_ver_productName);
            mTextProductPrice = (TextView) itemView.findViewById(R.id.txt_ver_productPrice);
            mTextProductBasicPrice = (TextView) itemView.findViewById(R.id.txt_ver_productBasicPrice);
            mTextProductDiscount = (TextView) itemView.findViewById(R.id.txt_ver_discount);
            mTextProductRatingCount = (TextView) itemView.findViewById(R.id.tv_product_rating_count_grid);
            mTextStoreLocation = (TextView) itemView.findViewById(R.id.tv_store_location_product);
            mTextProductQuantity = (TextView) itemView.findViewById(R.id.tv_product_quantity_none);
            mLinearDiscount = (LinearLayout) itemView.findViewById(R.id.layout_ver_discount);
            mLinearProductRating = (LinearLayout) itemView.findViewById(R.id.lnr_product_rating_grid);
            mRatingProduct = (RatingBar) itemView.findViewById(R.id.rbr_product_rating_gird);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void searchProduct(String text) {
        mDataList = new ArrayList<>();
        if (text.isEmpty()) {
            mDataList.addAll(mDataListCopy);
        } else {
            String keyword = text.toLowerCase();
            for (Product item : mDataListCopy) {
                if (item.getProductName().toLowerCase().contains(keyword)) {
                    mDataList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterProduct(List<FilterChild> filters) {
        mDataList = new ArrayList<>();
        Log.d(TAG, "mDataListSize: " + mDataList.size());
        Log.d(TAG, "mDataListCopySize: " + mDataListCopy.size());

        int count = 0;
        if (filters.size() == 0) {
            mDataList.addAll(mDataListCopy);
            Log.d(TAG, "mDataListSize: " + mDataList.size());
            notifyDataSetChanged();
        } else {
            List<Product> filterPriceProduct = new ArrayList<>();
            for (int i = 0; i < filters.size(); i++) {
                if (filters.get(i).getName().contains("₫")) {
                    count++;
                    for (Product item : mDataListCopy) {
                        String[] value = filters.get(i).getValue().split("-");
                        if (value.length > 1) {
                            int min = Integer.valueOf(value[0].replaceAll("\\s", ""));
                            int max = Integer.valueOf(value[1].replaceAll("\\s", ""));
                            if (checkPrice(item, min, max)) {
                                filterPriceProduct.add(item);
                            }
                        } else {
                            int min = Integer.valueOf(value[0].replaceAll("\\s", ""));
                            if (checkPrice(item, min)) {
                                filterPriceProduct.add(item);
                            }
                        }
                    }
                }

                // Last filter
                if (i == filters.size() - 1) {
                    Log.d(TAG, "count: " + count);
                    Log.d(TAG, "filters: " + filters.size());
                    Log.d(TAG, "FilterPriceProductSize: " + filterPriceProduct.size());

                    // If no filter price
                    if (count == 0) {
                        Log.d(TAG, "mDataListCopySize: " + mDataListCopy.size());
                        for (int j = 0; j < filters.size(); j++) {
                            for (Product item : mDataListCopy) {
                                if (item.getProductType().getId().equals(filters.get(j).getValue())) {
                                    mDataList.add(item);
                                    Log.d(TAG, "mDataList: " + mDataList.size());
                                }
                                notifyDataSetChanged();
                            }
                        }
                    } else {
                        // If only filter price
                        if (count == filters.size()) {
                            mDataList.addAll(filterPriceProduct);
                            Log.d(TAG, "mDataList: " + mDataList.size());
                            notifyDataSetChanged();
                        } else {
                            for (int j = 0; j < filters.size(); j++) {
                                for (Product item : filterPriceProduct) {
                                    if (item.getProductType().getId().equals(filters.get(j).getValue())) {
                                        mDataList.add(item);
                                        Log.d(TAG, "mDataList: " + mDataList.size());
                                    }
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }
        }
        onReachEnd();
    }

    public boolean checkPrice(Product product, int min, int max) {
        if (product.getSaleOff() != null && product.getSaleOff().getDiscount() > 0) {
            int basicPrice = Integer.valueOf(product.getPrice());
            int discount = product.getSaleOff().getDiscount();
            int salePrice = basicPrice - (basicPrice * discount / 100);

            if (salePrice > min && salePrice < max) {
                return true;
            } else return false;
        } else {
            if (Integer.valueOf(product.getPrice()) > min && Integer.valueOf(product.getPrice()) < max) {
                return true;
            } else return false;
        }
    }

    public boolean checkPrice(Product product, int min) {
        if (product.getSaleOff() != null && product.getSaleOff().getDiscount() > 0) {
            int basicPrice = Integer.valueOf(product.getPrice());
            int discount = product.getSaleOff().getDiscount();
            int salePrice = basicPrice - (basicPrice * discount / 100);

            if (salePrice > min) {
                return true;
            } else return false;

        } else {
            if (Integer.valueOf(product.getPrice()) > min) {
                return true;
            } else return false;
        }
    }
}
