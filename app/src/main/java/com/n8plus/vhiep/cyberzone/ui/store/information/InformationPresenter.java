package com.n8plus.vhiep.cyberzone.ui.store.information;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Review;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InformationPresenter implements InformationContract.Presenter{

    private InformationContract.View mInformationStoreView;
    private List<Review> reviewList;

    public InformationPresenter(InformationContract.View mInformationStoreView) {
        this.mInformationStoreView = mInformationStoreView;
    }

    @Override
    public void loadData() {
        prepareData();
        mInformationStoreView.setAdapterCustomerReview(reviewList);
    }

    private void prepareData(){
        reviewList = new ArrayList<>();
        reviewList.add(new Review("rv1", "Huỳnh Khắc Duy", "Hàng dở quá anh êi!", R.drawable.emoji_notgood, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv2", "Võ Hoài Phong", "Hàng tốt, đóng gói cẩn thận!", R.drawable.emoji_good, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv5", "Đặng Minh Nhựt", "Hàng kém chất lượng, đóng gói ẩu, giao hàng lâu, đừng mua shop này nhé, qua shop mình mua nè! Ahihi", R.drawable.emoji_notgood, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv3", "Nguyễn Văn Hiệp", "Hàng chất lượng, vận chuyển hơi lâu!", R.drawable.emoji_normal, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv4", "Võ Nguyễn Đại Phúc", "Hàng dở quá anh êi!", R.drawable.emoji_notgood, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv6", "Nguyễn Văn Tài", "Hàng chất lượng, vận chuyển hơi lâu!", R.drawable.emoji_normal, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv3", "Nguyễn Văn Hiệp", "Hàng chất lượng, vận chuyển hơi lâu!", R.drawable.emoji_normal, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv4", "Võ Nguyễn Đại Phúc", "Hàng dở quá anh êi!", R.drawable.emoji_notgood, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv6", "Nguyễn Văn Tài", "Hàng chất lượng, vận chuyển hơi lâu!", R.drawable.emoji_normal, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv3", "Nguyễn Văn Hiệp", "Hàng chất lượng, vận chuyển hơi lâu!", R.drawable.emoji_normal, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv4", "Võ Nguyễn Đại Phúc", "Hàng dở quá anh êi!", R.drawable.emoji_notgood, Calendar.getInstance().getTime()));
        reviewList.add(new Review("rv6", "Nguyễn Văn Tài", "Hàng chất lượng, vận chuyển hơi lâu!", R.drawable.emoji_normal, Calendar.getInstance().getTime()));
    }
}
