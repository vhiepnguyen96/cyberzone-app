package com.n8plus.vhiep.cyberzone.ui.manage.myreview.historyreview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.HistoryReview;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.adapter.HistoryReviewAdapter;

import java.util.List;

public class HistoryReviewFragment extends Fragment implements HistoryReviewContract.View {
    private static final String TAG = "HistoryReviewFragment";
    private HistoryReviewPresenter mHistoryReviewPresenter;
    private HistoryReviewAdapter mHistoryReviewAdapter;
    private LinearLayout mLinearNone, mLinearLoading;
    private ListView mListHistoryReview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_review_frag, container, false);
        mHistoryReviewPresenter = new HistoryReviewPresenter(getContext(),this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHistoryReviewPresenter != null){
            mHistoryReviewPresenter.loadData();
        }
        Log.d(TAG, "onResume");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListHistoryReview = (ListView) view.findViewById(R.id.lsv_history_review);
        mLinearNone = (LinearLayout) view.findViewById(R.id.lnr_none);
        mLinearLoading = (LinearLayout) view.findViewById(R.id.lnr_loading);

        // Presnter
        mHistoryReviewPresenter.loadData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setLayoutReviewNone(boolean b) {
        mLinearNone.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setLayoutLoading(boolean b) {
        mLinearLoading.setVisibility(b ? View.VISIBLE : View.GONE);
    }


    @Override
    public void setAdapterHistoryReview(List<HistoryReview> historyReviewList) {
        mHistoryReviewAdapter = new HistoryReviewAdapter(mListHistoryReview.getContext(), R.layout.row_history_review, historyReviewList);
        mListHistoryReview.setAdapter(mHistoryReviewAdapter);
    }

    @Override
    public void setNotifyDataSetChanged() {
        mHistoryReviewAdapter.notifyDataSetChanged();
    }
}
