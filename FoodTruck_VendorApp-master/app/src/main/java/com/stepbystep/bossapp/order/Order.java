package com.stepbystep.bossapp.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.stepbystep.bossapp.R;

public class Order extends Fragment {

    private TabLayout tabLayout;
    private ProcessOrderFragment processOrderFragment;
    private ApproveOrderFragment approveOrderFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order,container,false);
        //프래그먼트 객체 생성
        processOrderFragment = new ProcessOrderFragment();
        approveOrderFragment = new ApproveOrderFragment();
        //탭레이아웃 설정
        tabLayout = rootView.findViewById(R.id.fragment_order_tab);
        tabLayout.addTab(tabLayout.newTab().setText("수락 대기 주문"));
        tabLayout.addTab(tabLayout.newTab().setText("진행 중인 주문"));
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_order_container, approveOrderFragment).commit();
        setTabLayout();
        return rootView;
    }

    private void setTabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_order_container, approveOrderFragment).commit();
                }
                else if(position == 1){
                    getChildFragmentManager().beginTransaction().replace(R.id.fragment_order_container, processOrderFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
