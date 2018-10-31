package com.n8plus.vhiep.cyberzone.ui.manage.registersale.loadregistersale;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.RegisterSale;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.adapter.RegisterSaleAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.addregistersale.AddRegisterSaleFragment;

import java.util.List;

public class LoadRegisterSaleFragment extends Fragment implements LoadRegisterSaleContract.View {
    private LoadRegisterSalePresenter mLoadRegisterSalePresenter;
    private RecyclerView mRecyclerRegisterSale;
    private RegisterSaleAdapter mRegisterSaleAdapter;
    private DividerItemDecoration mDividerItemDecoration;
    private Button mRegisterSale;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mLoadRegisterSalePresenter = new LoadRegisterSalePresenter(this);
        return inflater.inflate(R.layout.load_register_sale_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerRegisterSale = (RecyclerView) view.findViewById(R.id.rcv_all_register_sale);
        mRegisterSale = (Button) view.findViewById(R.id.btn_add_register_sale);
        customRecyclerView();

        // Presenter
        mLoadRegisterSalePresenter.loadData();

        // Listener
        mRegisterSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frm_register_sale, new AddRegisterSaleFragment());
                fragmentTransaction.commit();
                ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                actionbar.setTitle("Đăng ký bán hàng");
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterRegisterSale(List<RegisterSale> registerSales) {
        mRegisterSaleAdapter = new RegisterSaleAdapter(registerSales);
        mRecyclerRegisterSale.setLayoutManager(new LinearLayoutManager(mRecyclerRegisterSale.getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerRegisterSale.addItemDecoration(mDividerItemDecoration);
        mRecyclerRegisterSale.setAdapter(mRegisterSaleAdapter);
    }

    private void customRecyclerView(){
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerRegisterSale.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_linear_layout));
    }
}
