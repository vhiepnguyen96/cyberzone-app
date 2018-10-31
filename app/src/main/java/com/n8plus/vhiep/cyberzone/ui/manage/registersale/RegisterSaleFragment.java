package com.n8plus.vhiep.cyberzone.ui.manage.registersale;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileContract;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.loadregistersale.LoadRegisterSaleFragment;

public class RegisterSaleFragment extends Fragment implements RegisterSaleContract.View {
    private Toolbar mToolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_sale_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_register);
        setHasOptionsMenu(true);
        customActionBar();

        // Set fragment
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_register_sale, new LoadRegisterSaleFragment());
        fragmentTransaction.commit();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String title = ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString();
                if (title.equals("Đơn đăng ký bán hàng")) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frm_manage, new MainManageFragment());
                    fragmentTransaction.commit();
                } else if (title.equals("Đăng ký bán hàng")) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frm_register_sale, new LoadRegisterSaleFragment());
                    fragmentTransaction.commit();
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Đơn đăng ký bán hàng");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void customActionBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Đơn đăng ký bán hàng");
    }
}
