package com.stepbystep.bossapp.account;


import android.view.View;

import com.stepbystep.bossapp.account.NoticeAdapter;

public interface OnNoticeItemClickListener {
    public void onItemClick(NoticeAdapter.ViewHolder holder , View view, int position);
}
