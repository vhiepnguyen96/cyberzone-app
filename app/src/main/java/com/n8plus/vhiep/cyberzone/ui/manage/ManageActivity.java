package com.n8plus.vhiep.cyberzone.ui.manage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;

public class ManageActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_act);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_manage, new MainManageFragment());
        fragmentTransaction.commit();
    }
}
