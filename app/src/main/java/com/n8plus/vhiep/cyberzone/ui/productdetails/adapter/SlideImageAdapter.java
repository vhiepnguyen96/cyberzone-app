package com.n8plus.vhiep.cyberzone.ui.productdetails.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.n8plus.vhiep.cyberzone.util.ViewPagerFixed;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class SlideImageAdapter extends PagerAdapter {
    private Context context;
    private List<ProductImage> images;
    private LayoutInflater inflater;

    public SlideImageAdapter(Context context, List<ProductImage> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @SuppressLint("ResourceType")
    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_image_custom, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.img_image);

        final ProgressBar progressBar = (ProgressBar) myImageLayout.findViewById(R.id.loading_image_progress);

        Picasso.get().load(images.get(position).getImageURL()).into(myImage, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
//        myImage.setImageResource(images.get(position));
        view.addView(myImageLayout, 0);

        myImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context ,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                View mView = inflater.inflate(R.layout.dialog_photo_view_layout, null);

                final ViewPagerFixed viewPager = mView.findViewById(R.id.vpg_slide_in_dialog);
                CircleIndicator indicatorSlide = mView.findViewById(R.id.indicator_slide_in_dialog);
                TextView closeDialog = mView.findViewById(R.id.tv_close_dialog_photoview);

                final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(context, images);
                viewPager.setAdapter(pagerAdapter);
                indicatorSlide.setViewPager(viewPager);

                mBuilder.setView(mView);
                final AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
            }
        });

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
