package com.n8plus.vhiep.cyberzone.ui.store.information.reviews.statistics;

public class StatisticsPresenter implements StatisticsContract.Presenter {
    private StatisticsContract.View mStoreReviewView;

    public StatisticsPresenter(StatisticsContract.View mStoreReviewView) {
        this.mStoreReviewView = mStoreReviewView;
    }

    @Override
    public void loadData() {

    }

    private void prepareData(){

    }
}
