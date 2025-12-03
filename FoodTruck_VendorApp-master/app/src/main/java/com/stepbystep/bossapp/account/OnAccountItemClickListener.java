package com.stepbystep.bossapp.account;


import android.view.View;

// Account 리싸이클러뷰 클릭 이벤트를 위한 인터페이스
public interface OnAccountItemClickListener {
    public void onItemClick(AccountAdapter.ViewHolder holder , View view, int position);
}
